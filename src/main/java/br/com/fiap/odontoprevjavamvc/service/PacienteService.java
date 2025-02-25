package br.com.fiap.odontoprevjavamvc.service;

import br.com.fiap.odontoprevjavamvc.model.Paciente;
import br.com.fiap.odontoprevjavamvc.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {
    @Autowired
    PacienteRepository pacienteRepository;

    public List<Paciente> buscarPacientes(){
        return pacienteRepository.findAll();
    }
    public Paciente buscarPaciente(Long id){
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.orElse(null);
    }
}
