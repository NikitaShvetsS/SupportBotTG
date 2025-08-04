package org.example.service;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    Long addUser(UserRequestDTO user);
    Long updateUser(Long id, UserRequestDTO user);
    UserRequestDTO getUserByChatId(Long telegramChatId);
    UserResponseDTO getUserById(Long id);
    List<UserResponseDTO> getAllUsers();
    void deleteUser(Long id);
    boolean isUserExist(Long id);


}
