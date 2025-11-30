package codeio.practise.controller;

import org.springframework.web.bind.annotation.RestController;

import codeio.practise.models.User;
import codeio.practise.repository.UserRepository;
import codeio.practise.service.UserService;
import codeio.practise.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.var;

import java.util.Map;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userrepository;
    private final UserService userservice;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    //@Autowired whether requiredargsconstrucor or this can be used
    
    
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> body ) {
        String email = body.get("email");
        String password = body.get("password");
        password=passwordEncoder.encode(password);


        if(userrepository.findByEmail(email).isPresent()){
            return new ResponseEntity<>("email already exixts ", HttpStatus.CONFLICT);
        }
        userservice.createUser(User.builder().email(email).password(password).build());
        return new ResponseEntity<>("Successfully Registered ", HttpStatus.CREATED);
    }

    @PostMapping("/login")
public ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String, String> body ) {
    String email = body.get("email");
    String password = body.get("password");

    var userOptional = userrepository.findByEmail(email);
    if (userOptional.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", "User not registered"));
    }

    User user = userOptional.get();

    // NOTE: the '!' — if password does NOT match, respond with error
    if (!passwordEncoder.matches(password, user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(Map.of("error", "Invalid password"));
    }

    String token = jwtUtil.generateToken(email);
    return ResponseEntity.ok(Map.of("token", token));
}

    

}
