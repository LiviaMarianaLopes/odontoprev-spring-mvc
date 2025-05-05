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
public class DentistaService {

    @Autowired
    private DentistaRepository dentistaRepository;

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

    public List<Dentista> buscarDentistas() {
        return dentistaRepository.findAll();
    }

    public Dentista buscarDentista(Long id) {
        Optional<Dentista> dentista = dentistaRepository.findById(id);
        return dentista.orElse(null);
    }

    @Transactional
    public Dentista cadastrarDentista(@Valid DentistaRequest dentistaRequest) {
        Dentista dentista = requestToDentista(dentistaRequest);
        return dentistaRepository.save(dentista);
    }

    @Transactional
    public void atualizarDentista(Long id, @Valid DentistaRequest dentistaRequest) {
        Optional<Dentista> dentistaOptional = dentistaRepository.findById(id);
        Optional<Login> loginOptional = loginRepository.findById(dentistaOptional.get().getLogin().getId());
        if (loginOptional.isPresent()) {
            Login login = loginOptional.get();
            login.setEmail(dentistaRequest.getEmail());
            login.setSenha(dentistaRequest.getLogin().getSenha());
            loginRepository.save(login);
        }
        if (dentistaOptional.isPresent()) {
            Dentista dentistaAtualizado = requestToDentista(dentistaRequest);
            dentistaAtualizado.setId(id);
            dentistaRepository.save(dentistaAtualizado);
        }
    }

    @Transactional
    public void deletarDentista(Long id) {
        Dentista dentista = dentistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dentista não encontrado"));
        loginRepository.deleteByEmail(dentista.getEmail());
        dentistaRepository.delete(dentista);
    }

    private Dentista requestToDentista(DentistaRequest dentistaRequest) {
        UserDetails userDetails = loginRepository.findByEmail(dentistaRequest.getEmail());
        Login login = null;

        if (userDetails == null) {
            login = loginRepository.save(new Login(dentistaRequest.getLogin().getEmail(), dentistaRequest.getLogin().getSenha()));
        } else {
            login = (Login) userDetails;
        }
        String siglaEstado = dentistaRequest.getEndereco().getBairro().getCidade().getEstado().getSigla();
        String nomeEstado = SIGLAS_ESTADOS.getOrDefault(siglaEstado, "Estado Desconhecido");

        Estado estado = estadoRepository.findByNome(nomeEstado)
                .orElseGet(() -> estadoRepository.save(new Estado(nomeEstado, siglaEstado)));

        Cidade cidade = cidadeRepository.findByNomeAndEstado(dentistaRequest.getEndereco().getBairro().getCidade().getNome(), estado)
                .orElseGet(() -> {
                    Cidade novaCidade = new Cidade();
                    novaCidade.setNome(dentistaRequest.getEndereco().getBairro().getCidade().getNome());
                    novaCidade.setEstado(estado);
                    return cidadeRepository.save(novaCidade);
                });

        Bairro bairro = bairroRepository.findByNomeAndCidade(dentistaRequest.getEndereco().getBairro().getNome(), cidade)
                .orElseGet(() -> {
                    Bairro novoBairro = new Bairro();
                    novoBairro.setNome(dentistaRequest.getEndereco().getBairro().getNome());
                    novoBairro.setCidade(cidade);
                    return bairroRepository.save(novoBairro);
                });

        // Verifica ou cria endereço
        Optional<Endereco> enderecoExistente = enderecoRepository.findByLogradouroAndNumeroAndCepAndComplementoAndBairro(
                dentistaRequest.getEndereco().getLogradouro(),
                dentistaRequest.getEndereco().getNumero(),
                dentistaRequest.getEndereco().getCep(),
                dentistaRequest.getEndereco().getComplemento(),
                bairro
        );

        Endereco endereco = enderecoExistente.orElseGet(() -> {
            Endereco novoEndereco = new Endereco();
            novoEndereco.setLogradouro(dentistaRequest.getEndereco().getLogradouro());
            novoEndereco.setNumero(dentistaRequest.getEndereco().getNumero());
            novoEndereco.setCep(dentistaRequest.getEndereco().getCep());
            novoEndereco.setComplemento(dentistaRequest.getEndereco().getComplemento());
            novoEndereco.setBairro(bairro);
            return enderecoRepository.save(novoEndereco);
        });

        // Verifica ou cria gênero
        Genero genero = generoRepository.findByTitulo(dentistaRequest.getGenero().getTitulo())
                .orElseGet(() -> {
                    Genero novoGenero = new Genero();
                    novoGenero.setTitulo(dentistaRequest.getGenero().getTitulo());
                    return generoRepository.save(novoGenero);
                });

        Dentista dentista = new Dentista();
        dentista.setNome(dentistaRequest.getNome());
        dentista.setCro(dentistaRequest.getCro());
        dentista.setDataNascimento(dentistaRequest.getDataNascimento());
        dentista.setEmail(dentistaRequest.getEmail());

        try {
            dentista.setTelefone(Long.parseLong(dentistaRequest.getTelefone()));
            dentista.setCpf(Long.parseLong(dentistaRequest.getCpf()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("CPF ou telefone inválidos: devem conter apenas números.");
        }

        dentista.setLogin(login);
        dentista.setEndereco(endereco);
        dentista.setGenero(genero);

        return dentista;
    }

    public DentistaRequest dentistaToRequest(Dentista dentista) {
        DentistaRequest request = new DentistaRequest();
        request.setNome(dentista.getNome());
        request.setCro(dentista.getCro());
        request.setEmail(dentista.getEmail());
        request.setDataNascimento(dentista.getDataNascimento());
        request.setTelefone(String.valueOf(dentista.getTelefone()));
        request.setCpf(String.valueOf(dentista.getCpf()));

        // Passando o Login para LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(dentista.getEmail());
        loginRequest.setSenha(dentista.getLogin().getSenha());
        request.setLogin(loginRequest);

        // Passando o endereço para endereço request
        EstadoRequest estadoRequest = new EstadoRequest();
        estadoRequest.setNome(dentista.getEndereco().getBairro().getCidade().getEstado().getNome());
        estadoRequest.setSigla(dentista.getEndereco().getBairro().getCidade().getEstado().getSigla());
        CidadeRequest cidadeRequest = new CidadeRequest();
        cidadeRequest.setNome(dentista.getEndereco().getBairro().getCidade().getNome());
        cidadeRequest.setEstado(estadoRequest);
        BairroRequest bairroRequest = new BairroRequest();
        bairroRequest.setNome(dentista.getEndereco().getBairro().getNome());
        bairroRequest.setCidade(cidadeRequest);
        EnderecoRequest enderecoRequest = new EnderecoRequest();
        enderecoRequest.setLogradouro(dentista.getEndereco().getLogradouro());
        enderecoRequest.setNumero(dentista.getEndereco().getNumero());
        enderecoRequest.setCep(dentista.getEndereco().getCep());
        enderecoRequest.setComplemento(dentista.getEndereco().getComplemento());
        enderecoRequest.setBairro(bairroRequest);
        request.setEndereco(enderecoRequest);

        // Passando o genero para genero request
        GeneroRequest generoRequest = new GeneroRequest();
        generoRequest.setTitulo(dentista.getGenero().getTitulo());
        request.setGenero(generoRequest);
        return request;
    }
    public Boolean isEmailCadastrado(String email) {
        if (loginRepository.findByEmail(email) != null) {
            return true;
        }
        return false;
    }

    public boolean isEmailUtilizado(DentistaRequest dentistaRequest, Long id) {
        Optional<Dentista> dentista = dentistaRepository.findByEmail(dentistaRequest.getEmail());
        return dentista.isPresent() && !dentista.get().getId().equals(id);
    }

}
