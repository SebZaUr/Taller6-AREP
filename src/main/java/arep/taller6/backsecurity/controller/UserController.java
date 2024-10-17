package arep.taller6.backsecurity.controller;

import arep.taller6.backsecurity.model.UserDTO;
import arep.taller6.backsecurity.model.UserEntity;
import arep.taller6.backsecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller of all enpoints related to the user.
 * @author Sebastian Zamora Urrego
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method that returns all the users in the database.
     * @return A list of all the users in the database.
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers() {
        try{
            return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
        }catch(Exception e){
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method that create a new user in the database.
     * @param userDTO The user to be created.
     * @return A json with the session token.
     */
    @PostMapping("/create")
    public ResponseEntity<Object> user(@Valid @RequestBody UserDTO userDTO) {
        try{
            return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);
        }catch(Exception e){
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method that validate if the user is in the database.
     * @param userDTO The user to be validated.
     * @return If the user is in the database.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDTO userDTO) {
        try{
            return new ResponseEntity<>(userService.getUser(userDTO), HttpStatus.OK);
        }catch(Exception e){
            Logger.getLogger(UserEntity.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
