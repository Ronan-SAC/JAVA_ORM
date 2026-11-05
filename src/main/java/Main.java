import entitys.Usuario;
import tables.UsuarioTable;
import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Testando Value Objects (Object Calisthenics) ===");

        Email email = new Email("contato@exemplo.com.br");
        System.out.println("Email: " + email + " | Válido: " + email.validarEmail());

        CPF cpf = new CPF("529.982.247-25");
        System.out.println("CPF Formatado: " + cpf + " | Apenas dígitos: " + cpf.getDigitos() + " | Válido: " + cpf.validarCPF());

        Telefone telefone = new Telefone("(11) 98765-4321");
        System.out.println("Telefone: " + telefone + " | DDD: " + telefone.getDDD() + " | Válido: " + telefone.validarTelefone());

        Endereco endereco = new Endereco("Avenida Paulista", "1578", "Bela Vista", "01310-200");
        System.out.println("Endereço: " + endereco);
        System.out.println("Rua: " + endereco.getRua() + ", Bairro: " + endereco.getBairro() + ", Número: " + endereco.getNumero() + ", CEP: " + endereco.getCEP() + " | Válido: " + endereco.validarCEP());

        System.out.println("\n=== 2. Operações de CRUD com UsuarioTable (Baseado em db-java) ===");

        UsuarioTable tabela = new UsuarioTable();

        // 1. Criar tabela caso não exista
        tabela.criarTabelaSeNaoExistir();

        // 2. CREATE (Inserir)
        Usuario novoUsuario = new Usuario(
                "Carlos Silva",
                cpf,
                email,
                telefone,
                "1995-03-20",
                endereco
        );

        System.out.println("\n[CRUD: Create] Inserindo usuário...");
        tabela.salvar(novoUsuario);

        // 3. READ (Listar Todos)
        System.out.println("\n[CRUD: Read] Listando todos os usuários:");
        List<Usuario> lista = tabela.listar();
        for (Usuario u : lista) {
            System.out.println(" - " + u);
        }

        // 4. READ (Buscar por Id ou CPF)
        if (!lista.isEmpty()) {
            int id = lista.get(0).getId();
            System.out.println("\n[CRUD: Read] Buscando usuário pelo ID " + id + ":");
            Usuario encontrado = tabela.buscarPorId(id);
            System.out.println("Encontrado: " + encontrado);

            // 5. UPDATE (Atualizar)
            System.out.println("\n[CRUD: Update] Atualizando telefone e nome...");
            encontrado.setNome("Carlos Silva Atualizado");
            encontrado.setTelefone(new Telefone("(11) 91234-5678"));
            tabela.atualizar(encontrado, id);

            // 6. DELETE (Deletar)
            System.out.println("\n[CRUD: Delete] Deletando usuário ID " + id + "...");
            tabela.deletar(id);
            System.out.println("Deletado com sucesso!");
        }

        System.out.println("\n=== Execução concluída com sucesso! ===");
    }
}
