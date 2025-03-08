package br.com.fiap.odontoprevjavamvc.controller;

import br.com.fiap.odontoprevjavamvc.dto.DentistaRequest;
import br.com.fiap.odontoprevjavamvc.model.Dentista;
import br.com.fiap.odontoprevjavamvc.service.DentistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dentista")
public class DentistaController {
    @Autowired
    DentistaService dentistaService;

    @GetMapping("/lista")
    public ModelAndView listarDentistas() {
        ModelAndView mv = new ModelAndView("dentistaLista");
        List<Dentista> dentistas = dentistaService.buscarDentistas();
        mv.addObject("listaDentistas", dentistas);
        return mv;
    }

    @GetMapping("/cadastro")
    public ModelAndView dentistaCadastro() {
        ModelAndView mv = new ModelAndView("dentistaCadastro");
        mv.addObject("dentista", new Dentista());
        return mv;
    }

    @PostMapping("/cadastrar")
    public ModelAndView cadastrarDentista(@Valid @ModelAttribute("Dentista") DentistaRequest dentistaRequest, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        if (dentistaService.isEmailCadastrado(dentistaRequest.getEmail())) {
            result.rejectValue("email", "email.cadastrado", "Este e-mail já está cadastrado.");
        }

        if (result.hasErrors()) {
            modelAndView.setViewName("dentistaCadastro");
            modelAndView.addObject("dentista", new Dentista());
            modelAndView.addObject("errors", result.getAllErrors());
            return modelAndView;
        }

        dentistaService.cadastrarDentista(dentistaRequest);
        modelAndView.setViewName("redirect:/dentista/lista");

        return modelAndView;
    }

    @GetMapping("/edicao/{id}")
    public ModelAndView dentistaEdicao(@PathVariable Long id) {
        Dentista dentista = dentistaService.buscarDentista(id);
        if (dentista == null) {
            return listarDentistas();
        }
        ModelAndView mv = new ModelAndView("dentistaEdicao");
        mv.addObject("idDentista", id);
        mv.addObject("dentista", dentistaService.dentistaToRequest(dentista));
        return mv;
    }

    @PostMapping("/editar/{id}")
    public  String  editarDentista(@PathVariable Long id, @Valid @ModelAttribute DentistaRequest dentistaRequest, BindingResult result, RedirectAttributes redirectAttributes) {
        if (dentistaService.isEmailUtilizado(dentistaRequest, id)) {
            result.rejectValue("email", "email.cadastrado", "Este e-mail já está cadastrado.");
        }
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/dentista/edicao/" + id;
        }
        dentistaService.atualizarDentista(id, dentistaRequest);
        return "redirect:/dentista/lista";
    }

    @GetMapping("/deletar/{id}")
    public ModelAndView deletarDentista(@PathVariable Long id) {
        dentistaService.deletarDentista(id);
        return listarDentistas();
    }
}
