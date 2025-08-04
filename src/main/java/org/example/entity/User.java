package org.example.entity;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Data
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String phoneNumber;
    private String email;
    private Long telegramChatId;
    private boolean isRegistered;

    public Long getTelegramChatId() {
        return telegramChatId;
    }
}
