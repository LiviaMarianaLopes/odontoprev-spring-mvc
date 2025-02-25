package br.com.fiap.odontoprevjavamvc.controller;

import br.com.fiap.odontoprevjavamvc.model.Paciente;
import br.com.fiap.odontoprevjavamvc.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
