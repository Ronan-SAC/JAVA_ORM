package mappers;

import entities.Purchase;
import entities.User;
import values.Address;
import values.Cpf;
import values.Email;
import values.PaymentMethod;
import values.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PurchaseMapper implements Mapper<Purchase> {

    @Override
    public Purchase toEntity(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String product = rs.getString("product");
            int quantity = rs.getInt("quantity");
            String paymentMethodStr = rs.getString("payment_method");
            double totalAmount = rs.getDouble("total_amount");
            String status = rs.getString("status");

            User user = null;
            try {
                int userId = rs.getInt("user_id");
                if (!rs.wasNull()) {
                    String userName = getColumnOrDefault(rs, "user_name", "Usuário #" + userId);
                    user = new User(
                            userId,
                            userName,
                            new Cpf("000.000.001-91"),
                            new Email("usuario" + userId + "@exemplo.com"),
                            new Phone("11999999999"),
                            "2000-01-01",
                            new Address("Rua Padrão", "1", "Centro", "01001-000")
                    );
                }
            } catch (SQLException ignored) {
            }

            PaymentMethod paymentMethod = null;
            if (paymentMethodStr != null && !paymentMethodStr.isBlank()) {
                paymentMethod = new PaymentMethod(paymentMethodStr);
            }

            return new Purchase(id, user, product, quantity, paymentMethod, totalAmount, status);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Compra: " + e.getMessage(), e);
        }
    }

    @Override
    public HashMap<String, Object> toMap(Purchase purchase) {
        HashMap<String, Object> data = new HashMap<>();
        if (purchase.getUser() != null) {
            if (purchase.getUser().getId() != null) {
                data.put("user_id", purchase.getUser().getId());
            }
            if (purchase.getUser().getName() != null) {
                data.put("user_name", purchase.getUser().getName());
            }
        }
        data.put("product", purchase.getProduct());
        data.put("quantity", purchase.getQuantity());
        data.put("payment_method", purchase.getPaymentMethod() != null ? purchase.getPaymentMethod().getValue() : null);
        data.put("total_amount", purchase.getTotalAmount());
        data.put("status", purchase.getStatus());
        return data;
    }

    private String getColumnOrDefault(ResultSet rs, String columnName, String defaultValue) {
        try {
            String val = rs.getString(columnName);
            return val != null ? val : defaultValue;
        } catch (SQLException e) {
            return defaultValue;
        }
    }
}
