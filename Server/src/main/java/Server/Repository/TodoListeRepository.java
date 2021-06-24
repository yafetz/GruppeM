package Server.Repository;

import Server.Modell.Gruppenmitglied;
import Server.Modell.Projektgruppe;
import Server.Modell.TeilnehmerListe;
import Server.Modell.ToDoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListeRepository extends JpaRepository<ToDoItem,Long> {
    List<ToDoItem> findAllByProjektgruppeId(long projektgruppeId);
    ToDoItem findTodoItemById(long ToDoItemId);
}
