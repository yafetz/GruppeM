package Server.Repository;

import Server.Modell.Lehrmaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LehrmaterialRepository extends JpaRepository<Lehrmaterial, Long> {

}
