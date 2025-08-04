package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity

public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

}
