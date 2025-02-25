package br.com.fiap.odontoprevjavamvc.repository;

import br.com.fiap.odontoprevjavamvc.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
}
