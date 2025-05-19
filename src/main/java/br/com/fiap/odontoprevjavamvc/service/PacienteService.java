package br.com.fiap.odontoprevjavamvc.service;

import br.com.fiap.odontoprevjavamvc.dto.*;
import br.com.fiap.odontoprevjavamvc.model.*;
import br.com.fiap.odontoprevjavamvc.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
        Paciente paciente = requestToPaciente(pacienteRequest);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public void atualizarPaciente(Long id, @Valid PacienteRequest pacienteRequest) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        // atualiazar login
        Optional<Login> loginOptional = loginRepository.findById(pacienteOptional.get().getLogin().getId());
        if (loginOptional.isPresent()) {
            Login login = loginOptional.get();
            login.setEmail(pacienteRequest.getEmail());
            login.setSenha(pacienteRequest.getLogin().getSenha());
            loginRepository.save(login);
        }
        if (pacienteOptional.isPresent()) {
            Paciente pacienteAtualizado = requestToPaciente(pacienteRequest);
            pacienteAtualizado.setId(id);
            pacienteRepository.save(pacienteAtualizado);
        }
    }

    @Transactional
    public void deletarPaciente(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        loginRepository.deleteByEmail(paciente.getEmail());
        pacienteRepository.delete(paciente);
    }

    private Paciente requestToPaciente(PacienteRequest pacienteRequest) {
        UserDetails userDetails = loginRepository.findByEmail(pacienteRequest.getEmail());
        Login login = null;

        if (userDetails == null) {
            login = loginRepository.save(new Login(pacienteRequest.getLogin().getEmail(), pacienteRequest.getLogin().getSenha()));
        } else {
            login = (Login) userDetails;
        }
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

        // Verifica ou cria endereço
        Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumeroAndCepAndComplementoAndBairro(
                pacienteRequest.getEndereco().getLogradouro(),
                pacienteRequest.getEndereco().getNumero(),
                pacienteRequest.getEndereco().getCep(),
                pacienteRequest.getEndereco().getComplemento(),
                bairro
        );

        Endereco endereco = enderecoExistente.orElseGet(() -> {
            Endereco novoEndereco = new Endereco();
            novoEndereco.setLogradouro(pacienteRequest.getEndereco().getLogradouro());
            novoEndereco.setNumero(pacienteRequest.getEndereco().getNumero());
            novoEndereco.setCep(pacienteRequest.getEndereco().getCep());
            novoEndereco.setComplemento(pacienteRequest.getEndereco().getComplemento());
            novoEndereco.setBairro(bairro);
            return enderecoRepository.save(novoEndereco);
        });

        // Verifica ou cria gênero
        Genero genero = generoRepository.findByTitulo(pacienteRequest.getGenero().getTitulo())
                .orElseGet(() -> {
                    Genero novoGenero = new Genero();
                    novoGenero.setTitulo(pacienteRequest.getGenero().getTitulo());
                    return generoRepository.save(novoGenero);
                });

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

        return paciente;
    }

    public PacienteRequest pacienteToRequest(Paciente paciente) {
        PacienteRequest request = new PacienteRequest();
        request.setNome(paciente.getNome());
        request.setEmail(paciente.getEmail());
        request.setDataNascimento(paciente.getDataNascimento());
        request.setTelefone(String.valueOf(paciente.getTelefone()));
        request.setCpf(String.valueOf(paciente.getCpf()));

        // Passando o Login para LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(paciente.getEmail());
        loginRequest.setSenha(paciente.getLogin().getSenha());
        request.setLogin(loginRequest);

        // Passando o endereço para endereço request
        EstadoRequest estadoRequest = new EstadoRequest();
        estadoRequest.setNome(paciente.getEndereco().getBairro().getCidade().getEstado().getNome());
        estadoRequest.setSigla(paciente.getEndereco().getBairro().getCidade().getEstado().getSigla());
        CidadeRequest cidadeRequest = new CidadeRequest();
        cidadeRequest.setNome(paciente.getEndereco().getBairro().getCidade().getNome());
        cidadeRequest.setEstado(estadoRequest);
        BairroRequest bairroRequest = new BairroRequest();
        bairroRequest.setNome(paciente.getEndereco().getBairro().getNome());
        bairroRequest.setCidade(cidadeRequest);
        EnderecoRequest enderecoRequest = new EnderecoRequest();
        enderecoRequest.setLogradouro(paciente.getEndereco().getLogradouro());
        enderecoRequest.setNumero(paciente.getEndereco().getNumero());
        enderecoRequest.setCep(paciente.getEndereco().getCep());
        enderecoRequest.setComplemento(paciente.getEndereco().getComplemento());
        enderecoRequest.setBairro(bairroRequest);
        request.setEndereco(enderecoRequest);

        // Passando o genero para genero request
        GeneroRequest generoRequest = new GeneroRequest();
        generoRequest.setTitulo(paciente.getGenero().getTitulo());
        request.setGenero(generoRequest);
        return request;
    }

    public Boolean isEmailCadastrado(String email) {

        if (loginRepository.findByEmail(email) != null) {
            return true;
        }
        return false;
    }

    public boolean isEmailUtilizado(PacienteRequest pacienteRequest, Long id) {
        // Verifica se o e-mail existe no banco de dados e se não pertence ao paciente atual
        Optional<Paciente> paciente = pacienteRepository.findByEmail(pacienteRequest.getEmail());
        return paciente.isPresent() && !paciente.get().getId().equals(id);
    }

}
