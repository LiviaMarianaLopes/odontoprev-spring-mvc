package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DentistaRepository extends JpaRepository<Dentista, Long> {
}
