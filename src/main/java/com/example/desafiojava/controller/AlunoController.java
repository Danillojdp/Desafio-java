package com.example.desafiojava.controller;

import com.example.desafiojava.dto.AlunoDTO;
import com.example.desafiojava.entity.AlunoEntity;
import com.example.desafiojava.service.AlunoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Controller
@RequestMapping(value = "/alunos")
public class AlunoController {


    private final AlunoService alunoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<AlunoEntity> cadastrarAluno(@RequestBody AlunoDTO alunoDTO) {
        AlunoEntity novoAluno = alunoService.cadastrarAluno(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AlunoEntity>> listarAlunos() {
        List<AlunoEntity> alunos = alunoService.listarAlunos();
        return ResponseEntity.ok(alunos);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirAluno(@PathVariable UUID alunoId) {
        alunoService.excluirAluno(alunoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exportar-xls")
    public void exportarAlunosParaXLS(HttpServletResponse response) {
        alunoService.exportarAlunosParaXLS(response);
    }


    @PostMapping("/importar")
    public ResponseEntity<String> importarAlunos(@RequestParam("file") MultipartFile file) {
        alunoService.importarAlunos(file);
        return ResponseEntity.ok("Alunos importados com sucesso.");
    }

    @GetMapping("/pagina")
    public String exibirPagina() {
        return "alunos";
    }

}
