package com.example.desafiojava.repository;

import com.example.desafiojava.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlunoRepository extends JpaRepository<AlunoEntity, UUID> {
    AlunoEntity findByCpf(String cpf);
}
