package ru.kpfu.itis.paramonov.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "password_change_requests")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "previous_password")
    private String previousPassword;

    @Column(name = "new_password")
    private String newPassword;

    private String code;
}
