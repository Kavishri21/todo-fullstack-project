package codeio.practise.repository;

import codeio.practise.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;;
public interface toDoRepository extends JpaRepository<Todo, Long> {
    

}
