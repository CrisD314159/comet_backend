package com.crisd.comet.model;

import com.crisd.comet.model.enums.UserState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User  implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, length = 60, unique = true)
    private String name;
    @Column(length = 200)
    private String biography;
    @Column(length = 60)
    private String country;
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String profilePicture;
    private boolean isVerified;
    private UserState state;
    private String verificationCode;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private boolean createdWithGoogle;
}
