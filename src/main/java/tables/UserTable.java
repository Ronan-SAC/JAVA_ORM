package tables;

import database.Database;
import entities.User;
import mappers.UserMapper;
import values.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserTable {
    private Database db;
    private UserMapper mapper;

    public UserTable() {
        this.db = new Database("users");
        this.mapper = new UserMapper();
    }

    public UserTable(Database db) {
        this.db = db;
        this.mapper = new UserMapper();
    }

    public int save(User user) {
        int result = this.db.create(this.mapper.toMap(user)).executeUpdate();
        saveAddress(user);
        return result;
    }

    public List<User> list() {
        List<User> users = new ArrayList<>();
        ResultSet rs = this.db.select().executeQuery();

        try {
            while (rs.next()) {
                users.add(this.mapper.toEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return users;
    }

    public User get_id(int id) {
        List<String> columns = List.of(
                "users.id",
                "users.name",
                "users.cpf",
                "users.email",
                "users.phone",
                "users.birth_date",
                "users.address",
                "addresses.street",
                "addresses.number",
                "addresses.neighborhood",
                "addresses.zip_code"
        );

        ResultSet rs = this.db.select(columns)
                .innerJoin("addresses", "users.id", "=", "addresses.user_id")
                .where("users.id", "=", id)
                .executeQuery();

        try {
            if (rs.next()) {
                String street = getColumnOrNull(rs, "street", "rua");
                String number = getColumnOrNull(rs, "number", "numero");
                String neighborhood = getColumnOrNull(rs, "neighborhood", "bairro");
                String zipCode = getColumnOrNull(rs, "zip_code", "cep");

                Address address = new Address(street, number, neighborhood, zipCode);
                User user = this.mapper.toEntity(rs);
                user.setAddress(address);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id com inner join de endereço: " + e.getMessage(), e);
        }

        return findOnlyUserById(id);
    }

    public User getId(int id) {
        return get_id(id);
    }

    public User find(int id) {
        return get_id(id);
    }

    public User find(String field, Object value) {
        List<String> columns = List.of(
                "users.id",
                "users.name",
                "users.cpf",
                "users.email",
                "users.phone",
                "users.birth_date",
                "users.address",
                "addresses.street",
                "addresses.number",
                "addresses.neighborhood",
                "addresses.zip_code"
        );

        ResultSet rs = this.db.select(columns)
                .innerJoin("addresses", "users.id", "=", "addresses.user_id")
                .where("users." + field, "=", value)
                .executeQuery();

        try {
            if (rs.next()) {
                String street = getColumnOrNull(rs, "street", "rua");
                String number = getColumnOrNull(rs, "number", "numero");
                String neighborhood = getColumnOrNull(rs, "neighborhood", "bairro");
                String zipCode = getColumnOrNull(rs, "zip_code", "cep");

                Address address = new Address(street, number, neighborhood, zipCode);
                User user = this.mapper.toEntity(rs);
                user.setAddress(address);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário em find com inner join: " + e.getMessage(), e);
        }

        return null;
    }

    public User findById(int id) {
        return get_id(id);
    }

    private User findOnlyUserById(int id) {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id =", id);

        ResultSet rs = this.db.select().where(condition).executeQuery();
        try {
            if (rs.next()) {
                return this.mapper.toEntity(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id: " + e.getMessage(), e);
        }
        return null;
    }

    public User findByCpf(String cpf) {
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("cpf =", cpf.replaceAll("\\D", ""));

        ResultSet rs = this.db.select().where(condition).executeQuery();
        try {
            if (rs.next()) {
                return this.mapper.toEntity(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por CPF: " + e.getMessage(), e);
        }
        return null;
    }

    public int update(User user, int id) {
        return this.db.update(this.mapper.toMap(user), id).executeUpdate();
    }

    public int delete(int id) {
        return this.db.delete(id).executeUpdate();
    }

    private void saveAddress(User user) {
        if (user.getAddress() == null) {
            return;
        }
        try {
            Integer userId = user.getId();
            if (userId == null) {
                User saved = findByCpf(user.getCpf().getDigits());
                if (saved != null) {
                    userId = saved.getId();
                    user.setId(userId);
                }
            }

            if (userId != null && this.db.getConnection() != null) {
                Database dbAddress = new Database("addresses", this.db.getConnection(), this.db.getStatement());
                Address address = user.getAddress();
                HashMap<String, Object> data = new HashMap<>();
                data.put("user_id", userId);
                data.put("street", address.getStreet());
                data.put("number", address.getNumber());
                data.put("neighborhood", address.getNeighborhood());
                data.put("zip_code", address.getZipCode());
                dbAddress.create(data).executeUpdate();
            }
        } catch (Exception ignored) {
        }
    }

    private String getColumnOrNull(ResultSet rs, String primary, String fallback) {
        try {
            String val = rs.getString(primary);
            if (val != null) {
                return val;
            }
        } catch (SQLException ignored) {
        }
        try {
            return rs.getString(fallback);
        } catch (SQLException ignored) {
            return null;
        }
    }

    public Database getDb() {
        return db;
    }

    public UserMapper getMapper() {
        return mapper;
    }
}
