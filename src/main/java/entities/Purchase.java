package entities;

import values.PaymentMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Purchase {
    public static final String STATUS_OPEN = "ABERTA";
    public static final String STATUS_COMPLETED = "FINALIZADA";
    public static final String STATUS_CANCELLED = "CANCELADA";

    private Integer id;
    private User user;
    private String product;
    private int quantity;
    private PaymentMethod paymentMethod;
    private Double totalAmount;
    private String status;
    private final List<PurchaseItem> items;

    public Purchase() {
        this.items = new ArrayList<>();
        this.status = STATUS_OPEN;
        this.quantity = 0;
        this.totalAmount = 0.0;
        this.product = "";
    }

    public Purchase(User user) {
        this();
        this.user = user;
    }

    public Purchase(User user, PaymentMethod paymentMethod) {
        this(user);
        this.paymentMethod = paymentMethod;
    }

    public Purchase(User user, String product, int quantity, PaymentMethod paymentMethod, Double totalAmount) {
        this(null, user, product, quantity, paymentMethod, totalAmount, STATUS_OPEN);
    }

    public Purchase(Integer id, User user, String product, int quantity, PaymentMethod paymentMethod, Double totalAmount, String status) {
        this.id = id;
        this.user = user;
        this.product = product != null ? product : "";
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount != null ? totalAmount : 0.0;
        this.status = status != null ? status : STATUS_OPEN;
        this.items = new ArrayList<>();

        if (product != null && !product.isBlank() && quantity > 0) {
            double unitPrice = (this.totalAmount > 0) ? (this.totalAmount / quantity) : 0.0;
            this.items.add(new PurchaseItem(product, quantity, unitPrice));
        }
    }

    public void addProduct(String productName, int quantity, double unitPrice) {
        validateNotCompleted("adicionar produtos a");

        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }

        PurchaseItem existing = findItem(productName);
        if (existing != null) {
            existing.addQuantity(quantity);
        } else {
            items.add(new PurchaseItem(productName, quantity, unitPrice));
        }

        recalculateTotals();
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        addProduct(product.getName(), quantity, product.getUnitPrice());
    }

    public void addProduct(PurchaseItem item) {
        if (item == null) {
            throw new IllegalArgumentException("ItemCompra não pode ser nulo.");
        }
        addProduct(item.getProductName(), item.getQuantity(), item.getUnitPrice());
    }

    public void addProduct(String productName, int quantity) {
        addProduct(productName, quantity, 0.0);
    }

    public boolean removeProduct(String productName) {
        validateNotCompleted("remover produtos de");

        if (productName == null) {
            return false;
        }

        PurchaseItem item = findItem(productName);
        if (item != null) {
            items.remove(item);
            recalculateTotals();
            return true;
        }
        return false;
    }

    public boolean removeProduct(Product product) {
        if (product == null) {
            return false;
        }
        return removeProduct(product.getName());
    }

    public boolean removeProduct(String productName, int quantity) {
        validateNotCompleted("remover produtos de");

        PurchaseItem item = findItem(productName);
        if (item == null) {
            return false;
        }

        if (quantity >= item.getQuantity()) {
            items.remove(item);
        } else {
            item.removeQuantity(quantity);
        }

        recalculateTotals();
        return true;
    }

    public void completePurchase() {
        validateNotCompleted("finalizar");

        if (this.user == null) {
            throw new IllegalStateException("Não é possível finalizar compra sem um usuário associado.");
        }
        if (this.quantity <= 0 || this.totalAmount == null || this.totalAmount <= 0.0) {
            throw new IllegalStateException("Não é possível finalizar compra sem produtos ou com valor zerado.");
        }
        if (this.paymentMethod == null) {
            throw new IllegalStateException("Não é possível finalizar compra sem definir a forma de pagamento.");
        }

        this.status = STATUS_COMPLETED;
    }

    public void completePurchase(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Forma de pagamento não pode ser nula.");
        }
        this.paymentMethod = paymentMethod;
        completePurchase();
    }

    public void completePurchase(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Forma de pagamento não pode ser vazia.");
        }
        this.paymentMethod = new PaymentMethod(paymentMethod);
        completePurchase();
    }

    private void recalculateTotals() {
        this.quantity = items.stream().mapToInt(PurchaseItem::getQuantity).sum();
        this.totalAmount = items.stream().mapToDouble(PurchaseItem::getSubtotal).sum();
        this.product = items.stream()
                .map(item -> item.getQuantity() + "x " + item.getProductName())
                .collect(Collectors.joining(", "));
    }

    private PurchaseItem findItem(String productName) {
        for (PurchaseItem item : items) {
            if (item.getProductName().equalsIgnoreCase(productName.trim())) {
                return item;
            }
        }
        return null;
    }

    private void validateNotCompleted(String action) {
        if (STATUS_COMPLETED.equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Não é possível " + action + " uma compra já finalizada.");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PurchaseItem> getItems() {
        return new ArrayList<>(items);
    }

    public boolean isCompleted() {
        return STATUS_COMPLETED.equalsIgnoreCase(this.status);
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", usuario=" + (user != null ? user.getName() : "null") +
                ", produto='" + product + '\'' +
                ", quantidade=" + quantity +
                ", formaPagamento=" + paymentMethod +
                ", valorCompra=" + String.format("%.2f", totalAmount) +
                ", status='" + status + '\'' +
                '}';
    }
}
