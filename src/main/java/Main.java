import entities.Purchase;
import entities.User;
import tables.PurchaseTable;
import tables.UserTable;
import values.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Testando Value Objects (Object Calisthenics) ===");

        Email email = new Email("contato@exemplo.com.br");
        System.out.println("Email: " + email + " | Válido: " + email.validate());

        Cpf cpf = new Cpf("529.982.247-25");
        System.out.println("CPF Formatado: " + cpf + " | Apenas dígitos: " + cpf.getDigits() + " | Válido: " + cpf.validate());

        Phone phone = new Phone("(11) 98765-4321");
        System.out.println("Telefone: " + phone + " | DDD: " + phone.getAreaCode() + " | Válido: " + phone.validate());

        Address address = new Address("Avenida Paulista", "1578", "Bela Vista", "01310-200");
        System.out.println("Endereço: " + address);
        System.out.println("Rua: " + address.getStreet() + ", Bairro: " + address.getNeighborhood() + ", Número: " + address.getNumber() + ", CEP: " + address.getZipCode() + " | Válido: " + address.validateZipCode());

        System.out.println("\n=== 2. Operações de CRUD com UserTable ===");

        UserTable userTable = new UserTable();

        User newUser = new User(
                "Carlos Silva",
                cpf,
                email,
                phone,
                "1995-03-20",
                address
        );

        System.out.println("\n[CRUD: Create] Inserindo usuário...");
        userTable.save(newUser);

        System.out.println("\n[CRUD: Read] Listando todos os usuários:");
        List<User> list = userTable.list();
        for (User u : list) {
            System.out.println(" - " + u);
        }

        if (!list.isEmpty()) {
            int id = list.get(0).getId();
            System.out.println("\n[CRUD: Read - get_id com Inner Join] Buscando usuário pelo ID " + id + ":");
            User foundById = userTable.get_id(id);
            System.out.println("Encontrado com get_id: " + foundById);
            if (foundById != null && foundById.getAddress() != null) {
                System.out.println("Endereço carregado via Inner Join: " + foundById.getAddress());
            }

            System.out.println("\n[CRUD: Read - find com Inner Join] Buscando usuário pelo ID " + id + ":");
            User foundWithFind = userTable.find(id);
            System.out.println("Encontrado com find: " + foundWithFind);

            System.out.println("\n[CRUD: Update] Atualizando telefone e nome...");
            foundById.setName("Carlos Silva Atualizado");
            foundById.setPhone(new Phone("(11) 91234-5678"));
            userTable.update(foundById, id);
        }

        System.out.println("\n=== 3. Operações com a Tabela Purchases (ORM PurchaseTable & Purchase) ===");

        PurchaseTable purchaseTable = new PurchaseTable();

        User buyerUser = !list.isEmpty() ? list.get(0) : newUser;
        Purchase purchase = new Purchase(buyerUser);

        System.out.println("\n[Compra: Adicionar Produtos]");
        purchase.addProduct("Notebook Gamer", 1, 4500.00);
        purchase.addProduct("Mouse Gamer", 2, 150.00);
        purchase.addProduct("Suporte Articulado", 1, 200.00);
        System.out.println("Produtos no carrinho: " + purchase.getProduct());
        System.out.println("Quantidade total de itens: " + purchase.getQuantity());
        System.out.println("Valor total: R$ " + String.format("%.2f", purchase.getTotalAmount()));

        System.out.println("\n[Compra: Remover Produto]");
        purchase.removeProduct("Suporte Articulado");
        System.out.println("Produtos após remoção: " + purchase.getProduct());
        System.out.println("Quantidade total: " + purchase.getQuantity());
        System.out.println("Valor total atualizado: R$ " + String.format("%.2f", purchase.getTotalAmount()));

        System.out.println("\n[Compra: Finalizar Compra]");
        purchase.completePurchase(PaymentMethod.PIX);
        System.out.println("Status da compra: " + purchase.getStatus());
        System.out.println("Forma de pagamento: " + purchase.getPaymentMethod().getDescription());

        System.out.println("\n[CRUD Purchases: Salvar]");
        purchaseTable.save(purchase);

        System.out.println("\n[CRUD Purchases: Listar]");
        List<Purchase> dbPurchases = purchaseTable.list();
        for (Purchase p : dbPurchases) {
            System.out.println(" - " + p);
        }

        if (!list.isEmpty()) {
            int id = list.get(0).getId();
            System.out.println("\n[CRUD: Delete] Deletando usuário ID " + id + "...");
            userTable.delete(id);
            System.out.println("Deletado com sucesso!");
        }

        System.out.println("\n=== Execução concluída com sucesso! ===");
    }
}
