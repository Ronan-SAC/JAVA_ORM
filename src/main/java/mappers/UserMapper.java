package mappers;

import entities.User;
import values.Address;
import values.Cpf;
import values.Email;
import values.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserMapper implements Mapper<User> {

    @Override
    public User toEntity(ResultSet rs) {
        try {
            int id = extractId(rs);
            String name = rs.getString("name");
            Cpf cpf = new Cpf(rs.getString("cpf"));
            Email email = new Email(rs.getString("email"));
            Phone phone = new Phone(rs.getString("phone"));
            String birthDate = rs.getString("birth_date");

            Address address;
            try {
                String street = getColumnOrNull(rs, "street", "rua");
                String number = getColumnOrNull(rs, "number", "numero");
                String neighborhood = getColumnOrNull(rs, "neighborhood", "bairro");
                String zipCode = getColumnOrNull(rs, "zip_code", "cep");

                if (street != null && number != null && neighborhood != null && zipCode != null) {
                    address = new Address(street, number, neighborhood, zipCode);
                } else {
                    address = extractAddressFallback(rs);
                }
            } catch (SQLException e) {
                address = extractAddressFallback(rs);
            }

            return new User(id, name, cpf, email, phone, birthDate, address);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao mapear ResultSet para Usuario: " + e.getMessage(), e);
        }
    }

    private int extractId(ResultSet rs) throws SQLException {
        try {
            return rs.getInt("id");
        } catch (SQLException e) {
            return rs.getInt("user_id");
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

    private Address extractAddressFallback(ResultSet rs) {
        try {
            String addressStr = rs.getString("address");
            if (addressStr != null && !addressStr.isBlank()) {
                return Address.parse(addressStr);
            }
        } catch (SQLException ignored) {
        }
        return new Address("Não informado", "S/N", "Centro", "00000000");
    }

    @Override
    public HashMap<String, Object> toMap(User user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("cpf", user.getCpf().getDigits());
        data.put("email", user.getEmail().getValue());
        data.put("phone", user.getPhone().getDigits());
        data.put("birth_date", user.getBirthDate());
        data.put("address", user.getAddress().getFormatted());
        return data;
    }

    public String toAddressString(Address address) {
        if (address == null) {
            return "";
        }
        return address.getFormatted();
    }
}
