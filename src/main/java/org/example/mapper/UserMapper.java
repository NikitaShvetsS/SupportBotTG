package org.example.mapper;

import org.example.dto.UserRequestDTO;
import org.example.dto.UserResponseDTO;
import org.example.entity.User;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(Date.valueOf(dto.getDateOfBirth()))
                .gender(dto.getGender())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .telegramChatId(dto.getChatId())
                .isRegistered(dto.isRegistered())
                .build();
    }

    public UserRequestDTO toRequestDto(User user) {
        if (user == null) {
            return null;
        }
        return UserRequestDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth().toLocalDate())
                .chatId(user.getTelegramChatId())
                .isRegistered(user.isRegistered())
                .gender(user.getGender())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public UserResponseDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        String fullName = "";
        if (user.getFirstName() != null) fullName += user.getFirstName();
        if (user.getLastName() != null) fullName += (fullName.isEmpty() ? "" : " ") + user.getLastName();

        return UserResponseDTO.builder()
                .id(user.getId())
                .fullName(fullName)
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .build();
    }


}
