package mappers;

import entitys.User;
import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserMapper {

    public User toEntity(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        CPF cpf = new CPF(rs.getString("cpf"));
        Email email = new Email(rs.getString("email"));
        Telefone phone = new Telefone(rs.getString("phone"));

        Date sqlBirthDate = rs.getDate("birth_date");
        LocalDate birthDate = sqlBirthDate != null ? sqlBirthDate.toLocalDate() : null;

        String rawAddress = rs.getString("address");
        Endereco address = Endereco.parse(rawAddress);

        return new User(id, name, cpf, email, phone, birthDate, address);
    }

    public String toAddressString(Endereco endereco) {
        if (endereco == null) {
            return "";
        }
        return endereco.getFormatado();
    }
}
