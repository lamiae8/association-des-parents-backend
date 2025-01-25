package com.ili.model;

import com.ili.dto.Adulte;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor @Getter
@Setter
@Entity
public class PasswordResetToken extends PanacheEntity {

    public String token;

    @OneToOne(targetEntity = AdulteEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    public AdulteEntity adulte;

    public LocalDateTime expiryDate;

    public PasswordResetToken(String token, AdulteEntity adulte) {
        this.token = token;
        this.adulte = adulte;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(1);  // token valid for 1 hour
    }
}
