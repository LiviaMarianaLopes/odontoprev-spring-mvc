package br.com.fiap.odontoprevjavamvc.controller;

import br.com.fiap.odontoprevjavamvc.dto.ConsultaRequest;
import br.com.fiap.odontoprevjavamvc.dto.LoginRequest;
import br.com.fiap.odontoprevjavamvc.dto.LoginResponse;
import br.com.fiap.odontoprevjavamvc.dto.PacienteRequest;
import br.com.fiap.odontoprevjavamvc.model.*;
import br.com.fiap.odontoprevjavamvc.repository.*;
import br.com.fiap.odontoprevjavamvc.security.TokenService;
import br.com.fiap.odontoprevjavamvc.service.ConsultaMapper;
import br.com.fiap.odontoprevjavamvc.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    UnidadeRepository unidadeRepository;
    @Autowired
    DentistaRepository dentistaRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    @Autowired
    ConsultaRepository consultaRepository;
    @Autowired
    ConsultaMapper consultaMapper;

    @GetMapping("dentistas")
    public ResponseEntity<List<Dentista>> readDentistas(){
        List<Dentista> listaDentistas = dentistaRepository.findAll();
        return new ResponseEntity<>(listaDentistas, HttpStatus.OK);
    }

    @GetMapping("/unidades")
    public ResponseEntity<List<Unidade>> readUnidades(){
        List<Unidade> listaUnidades = unidadeRepository.findAll();
        return new ResponseEntity<>(listaUnidades, HttpStatus.OK);
    }

    @PostMapping("/pacientes")
    public ResponseEntity<?> cadastrarPaciente(@RequestBody @Valid PacienteRequest pacienteRequest, BindingResult result) {

        if (pacienteService.isEmailCadastrado(pacienteRequest.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        pacienteService.cadastrarPaciente(pacienteRequest);
        Optional<Paciente> pacienteSalvo = pacienteRepository.findByEmail(pacienteRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteSalvo.get());
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            var usuarioSenha = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha());
            var auth = this.authenticationManager.authenticate(usuarioSenha);
            var token = tokenService.generateToken((Login) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Credenciais inválidas");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> logar(@RequestBody @Valid LoginRequest loginRequest) {
        Optional<Paciente> usuario = pacienteRepository.findByEmail(loginRequest.getEmail());

        if (usuario.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Usuário não encontrado");
        }

        Paciente paciente = usuario.get();
        System.out.println("Senha do banco: " + paciente.getLogin().getPassword());
        System.out.println("Senha enviada: " + loginRequest.getSenha());

        if (paciente.getLogin().getPassword().equals(loginRequest.getSenha())) {
            return ResponseEntity.ok(paciente.getId());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("Login ou senha incorretos");
        }
    }

    @GetMapping("/consulta/{id}")
    public ResponseEntity<List<Consulta>> getConsultasPorPaciente(@PathVariable("id") Long pacienteId) {
        List<Consulta> consultas = consultaRepository.findByPacienteId(pacienteId);
        if (consultas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consultas);
    }

    @PostMapping("/consultas")
    public ResponseEntity<Consulta> createConsulta(@Valid @RequestBody ConsultaRequest consultaRequest) {
        Consulta consultaConvertida = consultaMapper.requestToConsulta(consultaRequest);
        Consulta consulta = consultaRepository.save(consultaConvertida);
        if (consulta.getId() != null) {
            return new ResponseEntity<>(consulta, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}

