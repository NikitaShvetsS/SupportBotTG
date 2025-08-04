package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private boolean isRegistered;
}
