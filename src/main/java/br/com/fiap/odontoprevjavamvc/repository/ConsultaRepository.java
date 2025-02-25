package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
