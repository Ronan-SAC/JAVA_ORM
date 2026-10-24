package mappers;

import entitys.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    @Test
    @DisplayName("Deve converter ResultSet em entidade User corretamente")
    void deveConverterResultSetEmUser() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getString("name")).thenReturn("João da Silva");
        when(rs.getString("cpf")).thenReturn("529.982.247-25");
        when(rs.getString("email")).thenReturn("joao@email.com");
        when(rs.getString("phone")).thenReturn("11987654321");
        when(rs.getDate("birth_date")).thenReturn(Date.valueOf(LocalDate.of(1990, 5, 15)));
        when(rs.getString("address")).thenReturn("Rua das Flores, 123 - Centro - CEP: 01001-000");

        UserMapper mapper = new UserMapper();
        User user = mapper.toEntity(rs);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("João da Silva", user.getName());
        assertEquals("52998224725", user.getCpf().getDigitos());
        assertEquals("joao@email.com", user.getEmail().getValor());
        assertEquals("11", user.getPhone().getDDD());
        assertEquals(LocalDate.of(1990, 5, 15), user.getBirthDate());
        assertEquals("Rua das Flores", user.getAddress().getRua());
        assertEquals("123", user.getAddress().getNumero());
    }

    @Test
    @DisplayName("Deve formatar Endereco em String para gravação no banco")
    void deveFormatarEnderecoParaString() {
        UserMapper mapper = new UserMapper();
        Endereco endereco = new Endereco("Rua A", "42", "Bairro Novo", "13080-000");

        String formatted = mapper.toAddressString(endereco);
        assertEquals("Rua A, 42 - Bairro Novo - CEP: 13080-000", formatted);
    }
}
