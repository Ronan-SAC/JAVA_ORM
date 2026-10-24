package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5433/java";
    private static final String DEFAULT_USER = "java";
    private static final String DEFAULT_PASSWORD = "java";

    private static String url;
    private static String user;
    private static String password;

    static {
        carregarConfiguracoes();
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão com banco: " + e.getMessage());
        }
    }

    public static void setConfiguracoes(String novoUrl, String novoUser, String novoPassword) {
        url = novoUrl;
        user = novoUser;
        password = novoPassword;
    }

    private static void carregarConfiguracoes() {
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Aviso: db.properties não pôde ser carregado. Usando padrões. " + e.getMessage());
        }

        url = properties.getProperty("jdbc.url", DEFAULT_URL);
        user = properties.getProperty("jdbc.user", DEFAULT_USER);
        password = properties.getProperty("jdbc.password", DEFAULT_PASSWORD);
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }
}
