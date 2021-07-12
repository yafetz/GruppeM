package Server.Repository;

import Server.Modell.ChatRaum;
import Server.Modell.DatumUndUhrzeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatumUhrzeitRepository extends JpaRepository<DatumUndUhrzeit, Long> {
}
