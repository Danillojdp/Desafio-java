package com.example.desafiojava.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "tb_alunos")
public class AlunoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_aluno")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "sexo", nullable = true)
    private String sexo;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone", nullable = true)
    private String telefone;





}
