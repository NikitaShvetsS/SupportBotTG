package org.example.service.impl;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.entity.Gender;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("Nikita");

        User userEntity = new User();
        userEntity.setFirstName("Nikita");

        when(userMapper.toEntity(dto)).thenReturn(userEntity);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userArg = invocation.getArgument(0);
            userArg.setId(1L);
            return userArg;
        });

        Long resultId = userService.addUser(dto);
        assertEquals(1L, resultId);
    }

    @Test
    void updateUser() {
        Long userId = 1L;

        UserRequestDTO dto = new UserRequestDTO();
        dto.setLastName("Ivanov");
        dto.setDateOfBirth(LocalDate.parse("2000-01-01"));
        dto.setGender(Gender.MALE);
        dto.setPhoneNumber("1234567890");
        dto.setEmail("test@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setLastName("OldLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });

        Long result = userService.updateUser(userId, dto);

        assertEquals(userId, result);

        assertEquals("Ivanov", existingUser.getLastName());
        assertEquals(Date.valueOf("2000-01-01"), existingUser.getDateOfBirth());
        assertEquals(Gender.MALE, existingUser.getGender());
        assertEquals("1234567890", existingUser.getPhoneNumber());
        assertEquals("test@example.com", existingUser.getEmail());


    }

    @Test
    void getUserByChatId_success() {
        Long chatId = 12345L;

        User user = new User();
        user.setId(1L);
        user.setTelegramChatId(chatId);
        user.setFirstName("Nikita");

        UserRequestDTO dto = new UserRequestDTO();
        dto.setFirstName("Nikita");

        when(userRepository.findByTelegramChatId(chatId)).thenReturn(Optional.of(user));
        when(userMapper.toRequestDto(user)).thenReturn(dto);

        UserRequestDTO result = userService.getUserByChatId(chatId);

        assertNotNull(result);
        assertEquals("Nikita", result.getFirstName());
    }

    @Test
    void getUserByChatId_userNotFound() {
        Long chatId = 12345L;

        when(userRepository.findByTelegramChatId(chatId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByChatId(chatId);
        });
    }


    @Test
    void getUserById_success() {
        Long userId = 1L;

        User user = new User();
        user.setId(userId);
        user.setFirstName("Nikita");

        UserResponseDTO dto = new UserResponseDTO();
        dto.setFullName("Nikita");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDTO result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("Nikita", result.getFullName());

    }

    @Test
    void getUserById_userNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }


    @Test
    void getAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Nikita");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Alex");

        List<User> users = List.of(user1, user2);

        UserResponseDTO dto1 = new UserResponseDTO();
        dto1.setFullName("Nikita");

        UserResponseDTO dto2 = new UserResponseDTO();
        dto2.setFullName("Alex");

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Nikita", result.get(0).getFullName());
        assertEquals("Alex", result.get(1).getFullName());

    }
    @Test
    void getAllUsers_emptyList() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }


    @Test
    void deleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }


    @Test
    void isUserExist() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        boolean result = userService.isUserExist(userId);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(userId);

    }
    @Test
    void isUserExist_whenUserDoesNotExist_shouldReturnFalse() {
        Long userId = 2L;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean result = userService.isUserExist(userId);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(userId);
    }
}