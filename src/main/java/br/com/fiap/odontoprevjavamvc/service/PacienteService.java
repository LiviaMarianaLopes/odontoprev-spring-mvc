package br.com.fiap.odontoprevjavamvc.service;

import br.com.fiap.odontoprevjavamvc.dto.PacienteRequest;
import br.com.fiap.odontoprevjavamvc.model.*;
import br.com.fiap.odontoprevjavamvc.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BairroRepository bairroRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private GeneroRepository generoRepository;

    private static final Map<String, String> SIGLAS_ESTADOS = new HashMap<>();

    static {
        SIGLAS_ESTADOS.put("AC", "Acre");
        SIGLAS_ESTADOS.put("AL", "Alagoas");
        SIGLAS_ESTADOS.put("AP", "Amapá");
        SIGLAS_ESTADOS.put("AM", "Amazonas");
        SIGLAS_ESTADOS.put("BA", "Bahia");
        SIGLAS_ESTADOS.put("CE", "Ceará");
        SIGLAS_ESTADOS.put("DF", "Distrito Federal");
        SIGLAS_ESTADOS.put("ES", "Espírito Santo");
        SIGLAS_ESTADOS.put("GO", "Goiás");
        SIGLAS_ESTADOS.put("MA", "Maranhão");
        SIGLAS_ESTADOS.put("MT", "Mato Grosso");
        SIGLAS_ESTADOS.put("MS", "Mato Grosso do Sul");
        SIGLAS_ESTADOS.put("MG", "Minas Gerais");
        SIGLAS_ESTADOS.put("PA", "Pará");
        SIGLAS_ESTADOS.put("PB", "Paraíba");
        SIGLAS_ESTADOS.put("PR", "Paraná");
        SIGLAS_ESTADOS.put("PE", "Pernambuco");
        SIGLAS_ESTADOS.put("PI", "Piauí");
        SIGLAS_ESTADOS.put("RJ", "Rio de Janeiro");
        SIGLAS_ESTADOS.put("RN", "Rio Grande do Norte");
        SIGLAS_ESTADOS.put("RS", "Rio Grande do Sul");
        SIGLAS_ESTADOS.put("RO", "Rondônia");
        SIGLAS_ESTADOS.put("RR", "Roraima");
        SIGLAS_ESTADOS.put("SC", "Santa Catarina");
        SIGLAS_ESTADOS.put("SP", "São Paulo");
        SIGLAS_ESTADOS.put("SE", "Sergipe");
        SIGLAS_ESTADOS.put("TO", "Tocantins");
    }

    public List<Paciente> buscarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPaciente(Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.orElse(null);
    }

    @Transactional
    public Paciente cadastrarPaciente(@Valid PacienteRequest pacienteRequest) {

        // Verifica se o login já existe pelo e-mail
        Optional<Login> loginExistente = loginRepository.findByEmail(pacienteRequest.getEmail());
        Login login = loginExistente.orElseGet(() -> {
            Login novoLogin = new Login();
            novoLogin.setEmail(pacienteRequest.getEmail());
            novoLogin.setSenha(pacienteRequest.getLogin().getSenha());
            return loginRepository.save(novoLogin);
        });

        String siglaEstado = pacienteRequest.getEndereco().getBairro().getCidade().getEstado().getSigla();
        String nomeEstado = SIGLAS_ESTADOS.getOrDefault(siglaEstado, "Estado Desconhecido");

        Estado estado = estadoRepository.findByNome(nomeEstado)
                .orElseGet(() -> estadoRepository.save(new Estado(nomeEstado, siglaEstado)));

        Cidade cidade = cidadeRepository.findByNomeAndEstado(pacienteRequest.getEndereco().getBairro().getCidade().getNome(), estado)
                .orElseGet(() -> {
                    Cidade novaCidade = new Cidade();
                    novaCidade.setNome(pacienteRequest.getEndereco().getBairro().getCidade().getNome());
                    novaCidade.setEstado(estado);
                    return cidadeRepository.save(novaCidade);
                });

        Bairro bairro = bairroRepository.findByNomeAndCidade(pacienteRequest.getEndereco().getBairro().getNome(), cidade)
                .orElseGet(() -> {
                    Bairro novoBairro = new Bairro();
                    novoBairro.setNome(pacienteRequest.getEndereco().getBairro().getNome());
                    novoBairro.setCidade(cidade);
                    return bairroRepository.save(novoBairro);
                });

        Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumeroAndCepAndBairro(
                pacienteRequest.getEndereco().getLogradouro(),
                pacienteRequest.getEndereco().getNumero(),
                pacienteRequest.getEndereco().getCep(),
                bairro
        );

        Endereco endereco = enderecoExistente.orElseGet(() -> {
            Endereco novoEndereco = new Endereco();
            novoEndereco.setLogradouro(pacienteRequest.getEndereco().getLogradouro());
            novoEndereco.setNumero(pacienteRequest.getEndereco().getNumero());
            novoEndereco.setCep(pacienteRequest.getEndereco().getCep());
            novoEndereco.setBairro(bairro);
            return enderecoRepository.save(novoEndereco);
        });

        Genero genero = generoRepository.findByTitulo(pacienteRequest.getGenero().getTitulo())
                .orElseGet(() -> {
                    Genero novoGenero = new Genero();
                    novoGenero.setTitulo(pacienteRequest.getGenero().getTitulo());
                    return generoRepository.save(novoGenero);
                });

        // Cria o paciente
        Paciente paciente = new Paciente();
        paciente.setNome(pacienteRequest.getNome());
        paciente.setDataNascimento(pacienteRequest.getDataNascimento());
        paciente.setEmail(pacienteRequest.getEmail());
        try {
            paciente.setTelefone(Long.parseLong(pacienteRequest.getTelefone()));
            paciente.setCpf(Long.parseLong(pacienteRequest.getCpf()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("CPF ou telefone inválidos: devem conter apenas números.");
        }

        paciente.setLogin(login);
        paciente.setEndereco(endereco);
        paciente.setGenero(genero);

        return pacienteRepository.save(paciente);
    }
}
