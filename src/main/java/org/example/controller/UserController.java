package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
     public Long addUser(@RequestBody UserRequestDTO userRequestDTO){
        log.info("run method add user");
        return userService.addUser(userRequestDTO);
    }

    @PutMapping("/updateInfo")
    public Long updateUserInfo(@RequestParam Long id, @RequestBody UserRequestDTO userRequestDTO){
        log.info("run method update user information");
        return userService.updateUser(id, userRequestDTO);
    }

    @GetMapping("/all")
    public List<UserResponseDTO> getAllUsers(){
        log.info("run method get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/getByChatId")
    public UserRequestDTO getUserByChatId(@RequestParam Long chatId){
        log.info("run method get user by chat id");
        return userService.getUserByChatId(chatId);
    }

    @DeleteMapping("/delete")
    public void deleteUser(@RequestParam Long id){
        ResponseEntity.ok();
        userService.deleteUser(id);
    }

}
