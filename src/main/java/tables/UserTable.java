package tables;

import database.DatabaseConnection;
import entitys.User;
import mappers.UserMapper;
import values.CPF;
import values.Email;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserTable {

    private final UserMapper mapper;

    public UserTable() {
        this.mapper = new UserMapper();
    }

    public UserTable(UserMapper mapper) {
        this.mapper = mapper;
    }

    public void createTableIfNotExists() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    cpf VARCHAR(14) NOT NULL UNIQUE,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    phone VARCHAR(20) NOT NULL,
                    birth_date DATE NOT NULL,
                    address TEXT NOT NULL
                );
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, cpf, email, phone, birth_date, address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getCpf().getDigitos());
            statement.setString(3, user.getEmail().getValor());
            statement.setString(4, user.getPhone().getDigitos());
            statement.setDate(5, Date.valueOf(user.getBirthDate()));
            statement.setString(6, mapper.toAddressString(user.getAddress()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new SQLException("Falha ao salvar usuário, nenhum ID gerado retornado.");
                }
                long generatedId = generatedKeys.getLong(1);
                return user.withId(generatedId);
            }
        }
    }

    public Optional<User> findById(Long id) throws SQLException {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, name, cpf, email, phone, birth_date, address FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapper.toEntity(resultSet));
            }
        }
    }

    public Optional<User> findByCpf(CPF cpf) throws SQLException {
        if (cpf == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, name, cpf, email, phone, birth_date, address FROM users WHERE cpf = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf.getDigitos());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapper.toEntity(resultSet));
            }
        }
    }

    public Optional<User> findByEmail(Email email) throws SQLException {
        if (email == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, name, cpf, email, phone, birth_date, address FROM users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.getValor());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapper.toEntity(resultSet));
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, name, cpf, email, phone, birth_date, address FROM users ORDER BY id ASC";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapper.toEntity(resultSet));
            }
        }
        return users;
    }

    public boolean update(User user) throws SQLException {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Usuário e ID devem ser informados para atualização.");
        }
        String sql = """
                UPDATE users
                SET name = ?, cpf = ?, email = ?, phone = ?, birth_date = ?, address = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getCpf().getDigitos());
            statement.setString(3, user.getEmail().getValor());
            statement.setString(4, user.getPhone().getDigitos());
            statement.setDate(5, Date.valueOf(user.getBirthDate()));
            statement.setString(6, mapper.toAddressString(user.getAddress()));
            statement.setLong(7, user.getId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean delete(Long id) throws SQLException {
        if (id == null) {
            return false;
        }
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public void truncate() throws SQLException {
        String sql = "TRUNCATE TABLE users RESTART IDENTITY CASCADE";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
