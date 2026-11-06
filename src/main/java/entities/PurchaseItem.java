package entities;

public class PurchaseItem {
    private Product product;
    private int quantity;
    private double unitPrice;

    public PurchaseItem(Product product, int quantity, double unitPrice) {
        if (product == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public PurchaseItem(String productName, int quantity, double unitPrice) {
        this(new Product(productName, unitPrice), quantity, unitPrice);
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return product.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade a adicionar deve ser maior que zero.");
        }
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantidade a remover deve ser maior que zero.");
        }
        this.quantity -= quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return this.quantity * this.unitPrice;
    }

    @Override
    public String toString() {
        return quantity + "x " + product.getName() + " (R$ " + String.format("%.2f", unitPrice) + " cada)";
    }
}
