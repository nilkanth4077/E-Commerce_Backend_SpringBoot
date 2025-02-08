package com.e_commerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String userEmail;

    @Column(length = 500)
    private String subject;

    @Column(length = 2000)
    private String message;
}
