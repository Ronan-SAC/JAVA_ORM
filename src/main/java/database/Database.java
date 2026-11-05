package database;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String url = "jdbc:postgresql://localhost:5433/java";
    private String usuario = "java";
    private String senha = "java";

    private Connection conexao;
    private Statement ponteiro;

    private String tabela;
    private String query = "";

    public Database(String nomeTabela) {
        this.tabela = nomeTabela;
        this.conectar();
    }

    public void conectar() {
        try {
            this.conexao = DriverManager.getConnection(this.url, this.usuario, this.senha);
            this.ponteiro = this.conexao.createStatement();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

    public void criarTabelaSeNaoExistir(String ddl) {
        try {
            this.ponteiro.execute(ddl);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage(), e);
        }
    }

    public Database selecionar() {
        this.query = "SELECT * FROM " + this.tabela;
        return this;
    }

    public Database selecionar(List<String> nomeColunas) {
        this.query = "SELECT ";

        if (nomeColunas.isEmpty()) {
            this.query += "*";
        }

        for (int i = 0; i < nomeColunas.size(); i++) {
            this.query += nomeColunas.get(i);
            if (i < nomeColunas.size() - 1) {
                this.query += ", ";
            }
        }

        this.query += " FROM " + this.tabela;
        return this;
    }

    public Database criar(HashMap<String, Object> colunaValor) {
        StringBuilder colunas = new StringBuilder();
        StringBuilder valores = new StringBuilder();
        int contador = 0;

        for (Map.Entry<String, Object> entry : colunaValor.entrySet()) {
            contador++;
            colunas.append(entry.getKey());
            valores.append(formatarValorSql(entry.getValue()));

            if (contador < colunaValor.size()) {
                colunas.append(", ");
                valores.append(", ");
            }
        }

        this.query = "INSERT INTO " + this.tabela + " (" + colunas + ") VALUES (" + valores + ")";
        return this;
    }

    public Database atualizar(HashMap<String, Object> camposAlterados, int id) {
        StringBuilder atualizacoes = new StringBuilder();
        int contador = 0;

        for (Map.Entry<String, Object> entry : camposAlterados.entrySet()) {
            contador++;
            atualizacoes.append(entry.getKey())
                    .append(" = ")
                    .append(formatarValorSql(entry.getValue()));

            if (contador < camposAlterados.size()) {
                atualizacoes.append(", ");
            }
        }

        this.query = "UPDATE " + this.tabela + " SET " + atualizacoes;

        HashMap<String, Object> condicao = new HashMap<>();
        condicao.put("id =", id);
        return this.where(condicao);
    }

    public Database deletar(int id) {
        this.query = "DELETE FROM " + this.tabela;
        HashMap<String, Object> condicao = new HashMap<>();
        condicao.put("id =", id);
        return this.where(condicao);
    }

    public Database where(HashMap<String, Object> regras) {
        this.query += " WHERE ";
        int contador = 0;

        for (Map.Entry<String, Object> entry : regras.entrySet()) {
            contador++;
            String chave = entry.getKey().trim();
            Object valor = entry.getValue();

            if (!chave.contains("=")) {
                this.query += chave + " LIKE '%" + valor + "%'";
            }

            if (chave.contains("=")) {
                this.query += chave + " " + formatarValorSql(valor);
            }

            if (contador < regras.size()) {
                this.query += " AND ";
            }
        }

        return this;
    }

    public ResultSet executarConsulta() {
        this.query += ";";
        try {
            return this.ponteiro.executeQuery(this.query);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar consulta: " + e.getMessage(), e);
        } finally {
            this.query = "";
        }
    }

    public int executarComando() {
        this.query += ";";
        try {
            return this.ponteiro.executeUpdate(this.query);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar comando: " + e.getMessage(), e);
        } finally {
            this.query = "";
        }
    }

    private String formatarValorSql(Object valor) {
        if (valor == null) {
            return "NULL";
        }
        if (valor instanceof Number) {
            return String.valueOf(valor);
        }
        return "'" + valor.toString().replace("'", "''") + "'";
    }

    public String getQuery() {
        return query;
    }
}
