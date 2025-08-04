package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public Long addUser(UserRequestDTO userDto) {
        User user = userMapper.toEntity(userDto);
        return userRepository.save(user).getId();
    }

    @Override
    public Long updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLastName(userRequestDTO.getLastName());
        user.setDateOfBirth(Date.valueOf(userRequestDTO.getDateOfBirth()));
        user.setGender(userRequestDTO.getGender());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setEmail(userRequestDTO.getEmail());

        return userRepository.save(user).getId();
    }

    @Override
    public UserRequestDTO getUserByChatId(Long telegramChatId) {
        User user = userRepository.findByTelegramChatId(telegramChatId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toRequestDto(user);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUserExist(Long id){
        return userRepository.existsById(id);
    }
}
