package tables;

import database.Database;
import entitys.Usuario;
import mappers.UsuarioMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsuarioTable {
    private Database db;
    private UsuarioMapper mapper;

    public UsuarioTable() {
        this.db = new Database("users");
        this.mapper = new UsuarioMapper();
    }

    public void criarTabelaSeNaoExistir() {
        String ddl = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    cpf VARCHAR(14) NOT NULL UNIQUE,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    phone VARCHAR(20) NOT NULL,
                    birth_date VARCHAR(20) NOT NULL,
                    address TEXT NOT NULL
                );
                """;
        this.db.criarTabelaSeNaoExistir(ddl);
    }

    public int salvar(Usuario usuario) {
        return this.db.criar(this.mapper.paraMap(usuario)).executarComando();
    }

    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        ResultSet rs = this.db.selecionar().executarConsulta();

        try {
            while (rs.next()) {
                usuarios.add(this.mapper.paraEntidade(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }

        return usuarios;
    }

    public Usuario buscarPorId(int id) {
        HashMap<String, Object> condicao = new HashMap<>();
        condicao.put("id =", id);

        ResultSet rs = this.db.selecionar().where(condicao).executarConsulta();
        try {
            if (rs.next()) {
                return this.mapper.paraEntidade(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por id: " + e.getMessage(), e);
        }
        return null;
    }

    public Usuario buscarPorCpf(String cpf) {
        HashMap<String, Object> condicao = new HashMap<>();
        condicao.put("cpf =", cpf.replaceAll("\\D", ""));

        ResultSet rs = this.db.selecionar().where(condicao).executarConsulta();
        try {
            if (rs.next()) {
                return this.mapper.paraEntidade(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por CPF: " + e.getMessage(), e);
        }
        return null;
    }

    public int atualizar(Usuario usuario, int id) {
        return this.db.atualizar(this.mapper.paraMap(usuario), id).executarComando();
    }

    public int deletar(int id) {
        return this.db.deletar(id).executarComando();
    }
}
