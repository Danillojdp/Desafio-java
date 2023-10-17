package com.example.desafiojava.service;

import com.example.desafiojava.dto.AlunoDTO;
import com.example.desafiojava.entity.AlunoEntity;
import com.example.desafiojava.exception.DuplicataException;
import com.example.desafiojava.repository.AlunoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    public AlunoEntity cadastrarAluno(AlunoDTO alunoDTO) {
        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome(alunoDTO.getNome());
        aluno.setCpf(alunoDTO.getCpf());
        aluno.setSexo(alunoDTO.getSexo());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setTelefone(alunoDTO.getTelefone());

        return alunoRepository.save(aluno);
    }

    public List<AlunoEntity> listarAlunos() {
        return alunoRepository.findAll();
    }

    public void excluirAluno(UUID id) {
        alunoRepository.deleteById(id);
    }

    public void exportarAlunosParaXLS(HttpServletResponse response) {
        List<AlunoEntity> alunos = alunoRepository.findAll();


        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=alunos.xls");

        try (OutputStream out = response.getOutputStream()) {
            HSSFWorkbook workbook = new HSSFWorkbook();

            HSSFSheet sheet = workbook.createSheet("Alunos");

            HSSFRow headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nome");
            headerRow.createCell(1).setCellValue("CPF");
            headerRow.createCell(2).setCellValue("Sexo");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Telefone");

            int rowIdx = 1;
            for (AlunoEntity aluno : alunos) {
                HSSFRow row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(aluno.getNome());
                row.createCell(1).setCellValue(aluno.getCpf());
                row.createCell(2).setCellValue(aluno.getSexo());
                row.createCell(3).setCellValue(aluno.getEmail());
                row.createCell(4).setCellValue(aluno.getTelefone());
            }
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importarAlunos(MultipartFile file) {
        try {
            if (file.getOriginalFilename().endsWith(".xls")) {
                importarAlunosDoXLS(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(".csv")) {
                importarAlunosDoCSV(file.getInputStream());
            } else {
                throw new IllegalArgumentException("Formato de arquivo não suportado!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void importarAlunosDoXLS(InputStream fileInputStream) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(fileInputStream));
        HSSFSheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row != null) {
                String nome = row.getCell(0).getStringCellValue();
                String cpf = row.getCell(1).getStringCellValue();
                String sexo = row.getCell(2).getStringCellValue();
                String email = row.getCell(3).getStringCellValue();
                String telefone = row.getCell(4).getStringCellValue();

                if (cpfJaExisteNoBancoDeDados(cpf)) {
                    throw new DuplicataException("CPF duplicado: " + cpf);
                } else {
                    AlunoEntity aluno = new AlunoEntity();
                    aluno.setNome(nome);
                    aluno.setCpf(cpf);
                    aluno.setSexo(sexo);
                    aluno.setEmail(email);
                    aluno.setTelefone(telefone);

                    alunoRepository.save(aluno);
                }
            }
        }

        workbook.close();
    }

    private void importarAlunosDoCSV(InputStream fileInputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 5) {
                String nome = data[0];
                String cpf = data[1];
                String sexo = data[2];
                String email = data[3];
                String telefone = data[4];

                if (cpfJaExisteNoBancoDeDados(cpf)) {
                    throw new DuplicataException("CPF duplicado: " + cpf);
                } else {
                    AlunoEntity aluno = new AlunoEntity();
                    aluno.setNome(nome);
                    aluno.setCpf(cpf);
                    aluno.setSexo(sexo);
                    aluno.setEmail(email);
                    aluno.setTelefone(telefone);

                    alunoRepository.save(aluno);
                }
            }
        }

        reader.close();
    }

    private boolean cpfJaExisteNoBancoDeDados(String cpf) {
        AlunoEntity aluno = alunoRepository.findByCpf(cpf);
        return aluno != null;
    }
}