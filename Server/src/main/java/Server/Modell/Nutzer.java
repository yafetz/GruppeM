package Server.Modell;

import javax.persistence.*;

@Entity
@Table(name = "nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String Email;
}
