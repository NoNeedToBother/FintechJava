package ru.kpfu.itis.paramonov.entity;

import jakarta.persistence.Table;
import lombok.*;

@Table(name = "user_tokens")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String token;

    private boolean invalidated;
}
