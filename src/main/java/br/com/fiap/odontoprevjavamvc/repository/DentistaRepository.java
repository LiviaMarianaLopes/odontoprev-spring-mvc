package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {
    Optional<Dentista> findByEmail(String nome);
}
