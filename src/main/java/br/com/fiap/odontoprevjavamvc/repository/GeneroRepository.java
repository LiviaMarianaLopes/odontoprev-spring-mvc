package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {
}
