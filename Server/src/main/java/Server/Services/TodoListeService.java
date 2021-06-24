package Server.Services;

import Server.Modell.ToDoItem;
import Server.Repository.ProjektgruppenRepository;
import Server.Repository.StudentRepository;
import Server.Repository.TodoListeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoListeService {
    private final ProjektgruppenRepository projektgruppenRepositoryepository;
    private final StudentRepository studentRepository;
    private final TodoListeRepository todoListeRepository;

    @Autowired
    public TodoListeService(ProjektgruppenRepository projektgruppenRepositoryepository, StudentRepository studentRepository, TodoListeRepository todoListeRepository) {
        this.projektgruppenRepositoryepository = projektgruppenRepositoryepository;
        this.studentRepository = studentRepository;
        this.todoListeRepository = todoListeRepository;
    }



    public List<ToDoItem> todos(Long projektgruppeId) {
        return todoListeRepository.findAllByProjektgruppeId(projektgruppeId);

    }
}
