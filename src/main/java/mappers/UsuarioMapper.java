package mappers;

import entitys.Usuario;
import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UsuarioMapper implements Mapper<Usuario> {

    @Override
    public Usuario paraEntidade(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String nome = rs.getString("name");
            CPF cpf = new CPF(rs.getString("cpf"));
            Email email = new Email(rs.getString("email"));
            Telefone telefone = new Telefone(rs.getString("phone"));
            String dataNascimento = rs.getString("birth_date");
            Endereco endereco = Endereco.parse(rs.getString("address"));

            return new Usuario(id, nome, cpf, email, telefone, dataNascimento, endereco);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public HashMap<String, Object> paraMap(Usuario usuario) {
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("name", usuario.getNome());
        dados.put("cpf", usuario.getCpf().getDigitos());
        dados.put("email", usuario.getEmail().getValor());
        dados.put("phone", usuario.getTelefone().getDigitos());
        dados.put("birth_date", usuario.getDataNascimento());
        dados.put("address", usuario.getEndereco().getFormatado());
        return dados;
    }

    public String toAddressString(Endereco endereco) {
        if (endereco == null) {
            return "";
        }
        return endereco.getFormatado();
    }
}
