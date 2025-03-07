package br.com.fiap.odontoprevjavamvc.controller;

import br.com.fiap.odontoprevjavamvc.dto.PacienteRequest;
import br.com.fiap.odontoprevjavamvc.model.Paciente;
import br.com.fiap.odontoprevjavamvc.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/paciente")
public class PacienteController {
    @Autowired
    PacienteService pacienteService;
    @GetMapping("/lista")
    public ModelAndView listarPacientes(){
        ModelAndView mv = new ModelAndView("pacienteLista");
        List<Paciente> pacientes = pacienteService.buscarPacientes();
        mv.addObject("listaPacientes", pacientes);
        return mv;
    }
    @GetMapping("/cadastro")
    public ModelAndView pacienteCadastro(){
        ModelAndView mv = new ModelAndView("pacienteCadastro");
        mv.addObject("paciente",new Paciente());
        return mv;
    }
    @PostMapping("/cadastrar")
    public ModelAndView cadastrarPaciente(@Valid @ModelAttribute("Paciente") PacienteRequest pacienteRequest, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("pacienteCadastro"); // Retorna a página com erros
            modelAndView.addObject("paciente",new Paciente());
            modelAndView.addObject("errors",result.getAllErrors());
            return modelAndView;
        }

        pacienteService.cadastrarPaciente(pacienteRequest);
        modelAndView.setViewName("redirect:/paciente/lista"); // Redireciona para a página de sucesso

        return modelAndView;
    }
    @GetMapping("/edicao/{id}")
    public ModelAndView pacienteEdicao(@PathVariable Long id){
        Paciente paciente = pacienteService.buscarPaciente(id);
        if(paciente == null){
            return listarPacientes();
        }
        ModelAndView mv = new ModelAndView("pacienteEdicao");
        mv.addObject("idPaciente", id);
        mv.addObject("paciente", pacienteService.pacienteToRequest(paciente));
        System.out.println(pacienteService.pacienteToRequest(paciente));
        return mv;
    }
    @PostMapping("/editar/{id}")
    public ModelAndView editarPaciente(@PathVariable Long id, @Valid @ModelAttribute PacienteRequest pacienteRequest, BindingResult result){
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.setViewName("pacienteCadastro"); // Retorna a página com erros
            modelAndView.addObject("paciente",new Paciente());
            modelAndView.addObject("errors",result.getAllErrors());
            return modelAndView;
        }
        pacienteService.atualizarPaciente(id, pacienteRequest);
        modelAndView.setViewName("redirect:/paciente/lista"); // Redireciona para a página de sucesso

        return modelAndView;
    }

    @GetMapping("/deletar/{id}")
    public ModelAndView deletarPaciente(@PathVariable Long id){
        pacienteService.deletarPaciente(id);
        return listarPacientes();
    }
}
