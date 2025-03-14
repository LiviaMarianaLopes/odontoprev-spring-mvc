package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Bairro;
import br.com.fiap.odontoprevjavamvc.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByLogradouroAndNumeroAndCepAndComplementoAndBairro(
            String logradouro, int numero, int cep, String complemento, Bairro bairro
    );
}
