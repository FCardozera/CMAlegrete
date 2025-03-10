package com.cmalegrete.model.sendcontracttoken;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;


import com.cmalegrete.model.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "contract_token")
@Table(name = "tb_contract_token")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SendContractTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String token;

    @ReadOnlyProperty
    @CreatedDate
    private LocalDateTime timeStamp;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    public SendContractTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
        this.timeStamp = LocalDateTime.now();
    }

}