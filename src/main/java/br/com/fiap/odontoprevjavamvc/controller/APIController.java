package br.com.fiap.odontoprevjavamvc.controller;

import br.com.fiap.odontoprevjavamvc.dto.LoginRequest;
import br.com.fiap.odontoprevjavamvc.dto.LoginResponse;
import br.com.fiap.odontoprevjavamvc.dto.PacienteRequest;
import br.com.fiap.odontoprevjavamvc.model.Login;
import br.com.fiap.odontoprevjavamvc.repository.LoginRepository;
import br.com.fiap.odontoprevjavamvc.security.TokenService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/pacientes")
    public ResponseEntity<?> cadastrarPaciente(@RequestBody @Valid PacienteRequest pacienteRequest, BindingResult result) {

        if (pacienteService.isEmailCadastrado(pacienteRequest.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado");
        }

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        pacienteService.cadastrarPaciente(pacienteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Paciente cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            var usuarioSenha = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha());
            var auth = this.authenticationManager.authenticate(usuarioSenha);

            if (auth == null) {
                return ResponseEntity.badRequest().body("Credenciais inválidas");
            }

            return ResponseEntity.ok("Autenticado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na autenticação");
        }
    }



}

