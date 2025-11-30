package codeio.practise.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class practiceController {
    @GetMapping("/hello")

    String sayHello(){
        return "Hello world";

    }

}
