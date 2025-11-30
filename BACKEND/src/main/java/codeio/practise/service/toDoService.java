package codeio.practise.service;
import java.util.List;
import codeio.practise.models.Todo;
import codeio.practise.repository.toDoRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class toDoService {
    @Autowired
    private toDoRepository todoRepository;
    public Todo createTodo(Todo todo){
        return todoRepository.save(todo);
        //return todo;
    }

    public Todo getTodoById(Long id){
        return todoRepository.findById(id).orElseThrow(()-> new RuntimeException("todo  not found"));
    }
    public List<Todo> getTodos(){
        return todoRepository.findAll();
    }

    public Todo updateTodo(Todo todo){
        return todoRepository.save(todo);
    }

    public void deleteTodoById(Long id){
        todoRepository.delete(getTodoById(id));
    }

    public void deleteTodo(Todo todo){
        todoRepository.delete(todo);
    }
    public Page<Todo> getAllTodosPage(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findAll(pageable);
    
    }
}
