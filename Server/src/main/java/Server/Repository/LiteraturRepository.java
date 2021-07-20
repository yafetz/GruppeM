package Server.Repository;

import Server.Modell.Literatur;
import Server.Modell.Thema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteraturRepository extends JpaRepository<Literatur,Long> {
    Literatur findLiteraturById(Long id);
}
