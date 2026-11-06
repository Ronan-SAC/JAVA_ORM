package entities;

import java.util.Objects;

public class Product {
    private String name;
    private double unitPrice;

    public Product(String name, double unitPrice) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser negativo.");
        }
        this.name = name.trim();
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        this.name = name.trim();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " (R$ " + String.format("%.2f", unitPrice) + ")";
    }
}
