package database;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String url = "jdbc:postgresql://localhost:5433/java";
    private String user = "java";
    private String password = "java";

    private Connection connection;
    private Statement statement;

    private String table;
    private String query = "";

    public Database(String nameTable) {
        this.table = nameTable;
        this.connect();
    }

    public Database(String nameTable, Connection connection, Statement statement) {
        this.table = nameTable;
        this.connection = connection;
        this.statement = statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            this.statement = this.connection.createStatement();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public Database select() {
        this.query = "SELECT * FROM " + this.table;
        return this;
    }

    public Database select(List<String> nameColumns) {
        this.query = "SELECT ";

        if (nameColumns.isEmpty()) {
            this.query += "*";
        }

        for (int i = 0; i < nameColumns.size(); i++) {
            this.query += nameColumns.get(i);
            if (i < nameColumns.size() - 1) {
                this.query += ", ";
            }
        }

        this.query += " FROM " + this.table;
        return this;
    }

    public Database select(String... nameColumns) {
        return select(Arrays.asList(nameColumns));
    }

    public Database innerJoin(String joinTable, String col1, String operator, String col2) {
        this.query += " INNER JOIN " + joinTable + " ON " + col1 + " " + operator + " " + col2;
        return this;
    }

    public Database innerJoin(String joinTable, String onCondition) {
        this.query += " INNER JOIN " + joinTable + " ON " + onCondition;
        return this;
    }

    public Database join(String joinTable, String col1, String operator, String col2) {
        return innerJoin(joinTable, col1, operator, col2);
    }

    public Database join(String joinTable, String onCondition) {
        return innerJoin(joinTable, onCondition);
    }

    public Database inner_join(String joinTable, String col1, String operator, String col2) {
        return innerJoin(joinTable, col1, operator, col2);
    }

    public Database inner_join(String joinTable, String onCondition) {
        return innerJoin(joinTable, onCondition);
    }

    public Database create(HashMap<String, Object> columnValues) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        int counter = 0;

        for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
            counter++;
            columns.append(entry.getKey());
            values.append(formatSqlValue(entry.getValue()));

            if (counter < columnValues.size()) {
                columns.append(", ");
                values.append(", ");
            }
        }

        this.query = "INSERT INTO " + this.table + " (" + columns + ") VALUES (" + values + ")";
        return this;
    }

    public Database update(HashMap<String, Object> changedFields, int id) {
        StringBuilder updates = new StringBuilder();
        int counter = 0;

        for (Map.Entry<String, Object> entry : changedFields.entrySet()) {
            counter++;
            updates.append(entry.getKey())
                    .append(" = ")
                    .append(formatSqlValue(entry.getValue()));

            if (counter < changedFields.size()) {
                updates.append(", ");
            }
        }

        this.query = "UPDATE " + this.table + " SET " + updates;

        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id =", id);
        return this.where(condition);
    }

    public Database delete(int id) {
        this.query = "DELETE FROM " + this.table;
        HashMap<String, Object> condition = new HashMap<>();
        condition.put("id =", id);
        return this.where(condition);
    }

    private void appendCondition(String connector, String clause) {
        if (!this.query.contains(" WHERE ")) {
            this.query += " WHERE " + clause;
        } else {
            this.query += " " + connector + " " + clause;
        }
    }

    public Database where(String condition) {
        appendCondition("AND", condition);
        return this;
    }

    public Database where(String column, String operator, Object value) {
        return where(column + " " + operator + " " + formatSqlValue(value));
    }

    public Database where(HashMap<String, Object> rules) {
        StringBuilder sb = new StringBuilder();
        int counter = 0;

        for (Map.Entry<String, Object> entry : rules.entrySet()) {
            counter++;
            String key = entry.getKey().trim();
            Object value = entry.getValue();

            if (!key.contains("=")) {
                sb.append(key).append(" LIKE '%").append(value).append("%'");
            } else {
                sb.append(key).append(" ").append(formatSqlValue(value));
            }

            if (counter < rules.size()) {
                sb.append(" AND ");
            }
        }

        appendCondition("AND", sb.toString());
        return this;
    }

    public Database and(String condition) {
        appendCondition("AND", condition);
        return this;
    }

    public Database and(String column, String operator, Object value) {
        return and(column + " " + operator + " " + formatSqlValue(value));
    }

    public Database and(HashMap<String, Object> rules) {
        for (Map.Entry<String, Object> entry : rules.entrySet()) {
            String key = entry.getKey().trim();
            Object value = entry.getValue();
            if (!key.contains("=")) {
                and(key + " LIKE '%" + value + "%'");
            } else {
                and(key + " " + formatSqlValue(value));
            }
        }
        return this;
    }

    public Database andWhere(String condition) {
        return and(condition);
    }

    public Database andWhere(String column, String operator, Object value) {
        return and(column, operator, value);
    }

    public Database or(String condition) {
        appendCondition("OR", condition);
        return this;
    }

    public Database or(String column, String operator, Object value) {
        return or(column + " " + operator + " " + formatSqlValue(value));
    }

    public Database or(HashMap<String, Object> rules) {
        for (Map.Entry<String, Object> entry : rules.entrySet()) {
            String key = entry.getKey().trim();
            Object value = entry.getValue();
            if (!key.contains("=")) {
                or(key + " LIKE '%" + value + "%'");
            } else {
                or(key + " " + formatSqlValue(value));
            }
        }
        return this;
    }

    public Database orWhere(String condition) {
        return or(condition);
    }

    public Database orWhere(String column, String operator, Object value) {
        return or(column, operator, value);
    }

    public ResultSet executeQuery() {
        this.query += ";";
        try {
            return this.statement.executeQuery(this.query);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar consulta: " + e.getMessage(), e);
        } finally {
            this.query = "";
        }
    }

    public int executeUpdate() {
        this.query += ";";
        try {
            return this.statement.executeUpdate(this.query);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar comando: " + e.getMessage(), e);
        } finally {
            this.query = "";
        }
    }

    private String formatSqlValue(Object value) {
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        return "'" + value.toString().replace("'", "''") + "'";
    }

    public String getQuery() {
        return query;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
