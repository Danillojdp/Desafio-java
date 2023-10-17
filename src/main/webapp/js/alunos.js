$(document).ready(function() {
    // Lógica para atualizar a tabela de alunos com o novo aluno
    function atualizarTabelaComNovoAluno(aluno) {
        const newRow = `
            <tr>
                <td>${aluno.nome}</td>
                <td>${aluno.cpf}</td>
                <td>${aluno.sexo}</td>
                <td>${aluno.email}</td>
                <td>${aluno.telefone}</td>
                <td>
                    <button class="btn btn-danger excluir-btn" data-aluno-id="${aluno.id}">Excluir</button>
                </td>
            </tr>
        `;

        $('#alunosTable tbody').append(newRow);
    }

    // Lógica para preencher o modal com informações do aluno a ser excluído
    function preencherModalDeConfirmacao(aluno) {
        $('#confirmacaoModal').data('aluno-id', aluno.id);
        $('#confirmacaoModal .modal-body').text(`Tem certeza de que deseja excluir ${aluno.nome}?`);
    }

    // Lógica para remover o aluno da tabela
    function removerAlunoDaTabela(id) {
        $(`#alunosTable tr[data-aluno-id="${id}"]`).remove();
    }

    // Lógica para atualizar a tabela de alunos com os novos alunos
    function atualizarTabelaComNovosAlunos(novosAlunos) {
        $('#alunosTable tbody').empty();

        novosAlunos.forEach(function (aluno) {
            atualizarTabelaComNovoAluno(aluno);
        });
    }

    // Lógica para enviar o formulário de cadastro de aluno
    $('#cadastroAlunoForm').submit(function(event) {
        event.preventDefault();
        const formData = $(this).serialize();
        $.post('/cadastrar-aluno', formData, function(response) {
            atualizarTabelaComNovoAluno(response);
            $('#cadastroAlunoForm')[0].reset();
        });
    });

    // Lógica para abrir o modal de confirmação de exclusão
    $('#alunosTable').on('click', '.excluir-btn', function() {
        const id = $(this).data('id');
        const alunoNome = $(this).closest('tr').find('td:first').text();
        preencherModalDeConfirmacao({ id: id, nome: alunoNome });
        $('#confirmacaoModal').modal('show');
    });

    // Lógica para excluir o aluno
    $('#excluirAlunoBtn').click(function() {
        const id = $('#confirmacaoModal').data('aluno-id');
        $.ajax({
            url: `/excluir-aluno/${id}`,
            type: 'DELETE',
            success: function(response) {
                removerAlunoDaTabela(id);
                $('#confirmacaoModal').modal('hide'); // Feche o modal de confirmação
            }
        });
    });

    // Lógica para exportar alunos para PDF
    $('#exportarPdfBtn').click(function() {
        window.location.href = '/exportar-alunos-pdf';
    });

    // Lógica para exportar alunos para Excel
    $('#exportarExcelBtn').click(function() {
        window.location.href = '/exportar-alunos-excel';
    });

    // Lógica para importar alunos a partir de um arquivo
    $('#importarAlunosForm').submit(function(event) {
        event.preventDefault();
        const formData = new FormData(this);
        $.ajax({
            url: '/importar-alunos',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                atualizarTabelaComNovosAlunos(response); // Atualize a tabela com os novos alunos
                $('#arquivo').val(''); // Limpe o campo de seleção de arquivo após a importação
            }
        });
    });

});