package Server.Controller;

import Server.Modell.Gruppenmitglied;
import Server.Modell.Nutzer;
import Server.Modell.ToDoItem;
import Server.Repository.NutzerRepository;
import Server.Repository.TodoListeRepository;
import Server.Services.TodoListeService;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/todo/")
public class ToDoController {
    private NutzerRepository nutzerRepository;
    private TodoListeService todoListeService;
    private TodoListeRepository todoListeRepository;

    public ToDoController(NutzerRepository nutzerRepository, TodoListeService todoListeService, TodoListeRepository todoListeRepository) {
        this.nutzerRepository=nutzerRepository;
        this.todoListeService=todoListeService;
        this.todoListeRepository=todoListeRepository;
    }


    @PostMapping("create")
    public String neueTodo(@RequestParam("datum") String deadline,
                           @RequestParam("titel") String titel,
                           @RequestParam("verantwortliche") String gruppenmitglieder,
                           @RequestParam("erledigt") String done,
                           @RequestParam("projektgruppeId") long projektgruppeId,
                           @RequestParam("nutzerId") long nutzerId){


        ToDoItem toDoItem = new ToDoItem();
        toDoItem.setTitel(titel);
        toDoItem.setDeadline(deadline);
        toDoItem.setVerantwortliche(gruppenmitglieder);
        System.out.println(done);
        toDoItem.setErledigt(done);
        System.out.println(toDoItem.getErledigt());
        toDoItem.setProjektgruppeId(projektgruppeId);
        toDoItem.setNutzerId(nutzerId);

        todoListeRepository.save(toDoItem);
        System.out.println("created");

        return "Ok";
    }

    @GetMapping("{projektgruppeId}")
    public List<ToDoItem> getAlleTodoItem(@PathVariable long projektgruppeId){

        List<ToDoItem> todo= todoListeService.todos(projektgruppeId);


        return todo;


    }
    @PostMapping("update/")
    public String bearbeiteTodo(@RequestParam("datum") String deadline,
                                @RequestParam("titel") String titel,
                                @RequestParam("verantwortliche") String gruppenmitglieder,
                                @RequestParam("erledigt") String done,
                                @RequestParam("projektgruppeId") long projektgruppeId,
                                @RequestParam("todoItemId") long todoItemId,
                                @RequestParam("nutzerId") Long nutzerId)
    {


        ToDoItem toDoItem =todoListeRepository.findTodoItemById(todoItemId);

        toDoItem.setTitel(titel);
        toDoItem.setDeadline(deadline);
        toDoItem.setVerantwortliche(gruppenmitglieder);
        toDoItem.setErledigt(done);
        toDoItem.setNutzerId(nutzerId);
        toDoItem.setProjektgruppeId(projektgruppeId);


        todoListeRepository.save(toDoItem);
        System.out.println("update");




        return "Ok";
    }






}
