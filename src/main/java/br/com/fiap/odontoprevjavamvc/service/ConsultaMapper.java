package br.com.fiap.odontoprevjavamvc.service;

import br.com.fiap.odontoprevjavamvc.dto.ConsultaRequest;
import br.com.fiap.odontoprevjavamvc.model.Consulta;
import br.com.fiap.odontoprevjavamvc.model.Dentista;
import br.com.fiap.odontoprevjavamvc.model.Paciente;
import br.com.fiap.odontoprevjavamvc.model.Unidade;
import br.com.fiap.odontoprevjavamvc.repository.DentistaRepository;
import br.com.fiap.odontoprevjavamvc.repository.PacienteRepository;
import br.com.fiap.odontoprevjavamvc.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaMapper {
    @Autowired
    private DentistaRepository dentistaRepository;
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private UnidadeRepository unidadeRepository;
public Consulta requestToConsulta(ConsultaRequest consultaRequest){
    Paciente paciente = pacienteRepository.findById(consultaRequest.idPaciente())
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

    Dentista dentista = dentistaRepository.findById(consultaRequest.idDentista())
            .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));

    Unidade unidade = unidadeRepository.findById(consultaRequest.idUnidade())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

    Consulta consulta = new Consulta();
    consulta.setData(consultaRequest.data());
    consulta.setPaciente(paciente);
    consulta.setDentista(dentista);
    consulta.setUnidade(unidade);
    consulta.setMotivo(consultaRequest.motivo());
    return consulta;
}
}
