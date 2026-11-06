package tables;

import database.Database;
import entities.Purchase;
import mappers.PurchaseMapper;
import values.PaymentMethod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PurchaseTable {
    private Database db;
    private PurchaseMapper mapper;

    public PurchaseTable() {
        this.db = new Database("purchases");
        this.mapper = new PurchaseMapper();
    }

    public PurchaseTable(Database db) {
        this.db = db;
        this.mapper = new PurchaseMapper();
    }

    public int save(Purchase purchase) {
        return this.db.create(this.mapper.toMap(purchase)).executeUpdate();
    }

    public List<Purchase> list() {
        List<Purchase> purchases = new ArrayList<>();
        ResultSet rs = this.db.select().executeQuery();

        try {
            while (rs.next()) {
                purchases.add(this.mapper.toEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar compras: " + e.getMessage(), e);
        }

        return purchases;
    }

    public Purchase find(int id) {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id =", id);

        ResultSet rs = this.db.select().where(condition).executeQuery();
        try {
            if (rs.next()) {
                return this.mapper.toEntity(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compra por id: " + e.getMessage(), e);
        }
        return null;
    }

    public Purchase findById(int id) {
        return find(id);
    }

    public List<Purchase> findByUserId(int userId) {
        List<Purchase> purchases = new ArrayList<>();
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("user_id =", userId);

        ResultSet rs = this.db.select().where(condition).executeQuery();
        try {
            while (rs.next()) {
                purchases.add(this.mapper.toEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar compras por id de usuário: " + e.getMessage(), e);
        }
        return purchases;
    }

    public int update(Purchase purchase, int id) {
        return this.db.update(this.mapper.toMap(purchase), id).executeUpdate();
    }

    public int delete(int id) {
        return this.db.delete(id).executeUpdate();
    }

    public int completePurchase(Purchase purchase) {
        purchase.completePurchase();
        if (purchase.getId() != null) {
            return update(purchase, purchase.getId());
        }
        return save(purchase);
    }

    public int completePurchase(Purchase purchase, PaymentMethod paymentMethod) {
        purchase.completePurchase(paymentMethod);
        if (purchase.getId() != null) {
            return update(purchase, purchase.getId());
        }
        return save(purchase);
    }

    public Database getDb() {
        return db;
    }

    public PurchaseMapper getMapper() {
        return mapper;
    }
}
