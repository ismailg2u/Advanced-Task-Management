package dev.definex.finalproject.controller;

import dev.definex.finalproject.dto.UserDto;
import dev.definex.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }
    @GetMapping("/findNotDeleted")
    public ResponseEntity<List<UserDto>> findNotDeleted(){
        return ResponseEntity.ok(userService.findByDeleted());
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.findById(id));
    }
    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.save(userDto));
    }
    @PutMapping("/update")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.update(userDto));
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
         userService.delete(id);
         return ResponseEntity.ok().build();
    }
}
