package com.bekrenov.clinic.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="activation_tokens")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationToken extends AbstractEntity {
    @Column(name = "token")
    private String token;

    @Column(name = "username")
    private String username;
}
