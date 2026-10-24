import database.DatabaseInitializer;
import entitys.User;
import tables.UserTable;
import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Aplicação ORM com Object Calisthenics ===");

        // 1. Demonstração dos Value Objects (Object Calisthenics)
        System.out.println("\n--- 1. Teste dos Value Objects ---");
        Email email = new Email("contato@exemplo.com.br");
        System.out.println("Email: " + email + " | Validação: " + email.validarEmail());

        CPF cpf = new CPF("529.982.247-25");
        System.out.println("CPF Formatado: " + cpf + " | Apenas dígitos: " + cpf.getDigitos() + " | Validação: " + cpf.validarCPF());

        Telefone telefone = new Telefone("(11) 98765-4321");
        System.out.println("Telefone: " + telefone + " | DDD: " + telefone.getDDD() + " | Validação: " + telefone.validarTelefone());

        Endereco endereco = new Endereco("Avenida Paulista", "1578", "Bela Vista", "01310-200");
        System.out.println("Endereço: " + endereco);
        System.out.println("Rua: " + endereco.getRua() + " | Número: " + endereco.getNumero() + " | Bairro: " + endereco.getBairro() + " | CEP: " + endereco.getCEP() + " | CEP Válido: " + endereco.validarCEP());

        // 2. Operações de CRUD com UserTable
        System.out.println("\n--- 2. Inicializando Tabela e Executando CRUD ---");
        UserTable userTable = new UserTable();

        try {
            // Inicializa DDL se tabela não existir
            System.out.println("Criando tabela 'users' se não existir...");
            userTable.createTableIfNotExists();
            System.out.println("Tabela 'users' pronta!");

            // CREATE (Insert)
            User novoUsuario = new User(
                    "Carlos Eduardo Silva",
                    cpf,
                    email,
                    telefone,
                    LocalDate.of(1995, 3, 20),
                    endereco
            );

            System.out.println("\n[CRUD: Create] Inserindo novo usuário...");
            User usuarioSalvo = userTable.save(novoUsuario);
            System.out.println("Usuário salvo com sucesso! ID gerado: " + usuarioSalvo.getId());

            // READ (findById)
            System.out.println("\n[CRUD: Read] Buscando usuário pelo ID: " + usuarioSalvo.getId());
            Optional<User> usuarioEncontrado = userTable.findById(usuarioSalvo.getId());
            usuarioEncontrado.ifPresent(u -> System.out.println("Usuário encontrado: " + u));

            // READ (findByCpf)
            System.out.println("\n[CRUD: Read] Buscando usuário pelo CPF: " + cpf.getDigitos());
            Optional<User> usuarioPorCpf = userTable.findByCpf(cpf);
            usuarioPorCpf.ifPresent(u -> System.out.println("Usuário encontrado por CPF: " + u.getName()));

            // UPDATE
            System.out.println("\n[CRUD: Update] Atualizando telefone e endereço...");
            Telefone novoTelefone = new Telefone("(11) 91234-5678");
            Endereco novoEndereco = new Endereco("Alameda Santos", "800", "Cerqueira César", "01418-000");
            User usuarioAtualizado = new User(
                    usuarioSalvo.getId(),
                    "Carlos Eduardo Silva Atualizado",
                    usuarioSalvo.getCpf(),
                    usuarioSalvo.getEmail(),
                    novoTelefone,
                    usuarioSalvo.getBirthDate(),
                    novoEndereco
            );
            boolean atualizado = userTable.update(usuarioAtualizado);
            System.out.println("Atualização realizada com sucesso? " + atualizado);

            // READ (findAll)
            System.out.println("\n[CRUD: Read] Listando todos os usuários...");
            List<User> todosUsuarios = userTable.findAll();
            for (User u : todosUsuarios) {
                System.out.println(" - " + u);
            }

            // DELETE
            System.out.println("\n[CRUD: Delete] Excluindo usuário ID: " + usuarioSalvo.getId());
            boolean excluido = userTable.delete(usuarioSalvo.getId());
            System.out.println("Usuário excluído com sucesso? " + excluido);

            System.out.println("\n=== Demonstração concluída com sucesso! ===");

        } catch (SQLException e) {
            System.err.println("Erro na comunicação com o banco de dados: " + e.getMessage());
            System.err.println("Certifique-se de que o PostgreSQL está em execução em localhost:5433 com a base 'java'!");
        }
    }
}
