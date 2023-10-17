package com.example.desafiojava.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class AlunoDTO {

    private UUID id;

    private String nome;

    private String cpf;

    private String sexo;

    private String email;

    private String telefone;
}
