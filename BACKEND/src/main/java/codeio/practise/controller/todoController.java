package codeio.practise.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import codeio.practise.models.Todo;
import codeio.practise.service.toDoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/todo")
@Slf4j
public class todoController {
    @Autowired
    private toDoService todoservice;

@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "todo retrieved successfully"),
    @ApiResponse(responseCode = "404", description = "todo was not found")
})

    @GetMapping("/get")
    String getTodo() {
        //todoservice.printTodos();
        return "todos";
    }
    
    @GetMapping("/{id}")
    ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        try{
            Todo saved2 = todoservice.getTodoById(id);
            return new ResponseEntity<>(saved2, HttpStatus.OK);
        }
        catch(RuntimeException exception){
            log.info("error");
            log.warn("");
            log.error("", exception);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        
        //return "the id given in the browser address is "+ id;
    }
/* 
    @GetMapping("")
    public String getTodoByKeyValue(@RequestParam ("todoId") Long id, @RequestParam String name) {
        return "key is "+id+" value is "+name;
        //localhost:8080/todo?todoId=24&name=kavishri
    }
*/
    // same like this postmapping putmapping and deletemapping are there, where the later 2 (/"id") on;y can be given
    // based on the rquest the browser otself will decide which one to call this should be tested in postman

    @GetMapping
    ResponseEntity<List<Todo>> getTodos(){
        return new ResponseEntity<List<Todo>>(todoservice.getTodos(), HttpStatus.OK);

    }

    @GetMapping("/page")
    ResponseEntity<Page<Todo>> getTodosPaged(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(todoservice.getAllTodosPage(page,size), HttpStatus.OK);
    }
    
    @PostMapping("/create")
    ResponseEntity<Todo> createUser(@RequestBody Todo todo) { //to create a user with passowrds which cannot be shown in the browset explicitly
        //TODO: process POST request
        //return new ResponseEntity<>(todoservice.createTodo(todo, HttpStatus.CREATED));
        //return todo;
        Todo saved = todoservice.createTodo(todo);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping
    ResponseEntity<Todo> updateTodoById(@RequestBody Todo todo){
        return new ResponseEntity<>(todoservice.updateTodo(todo), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    void deleteTodoById(@PathVariable Long id){
        todoservice.deleteTodoById(id);
    }
}