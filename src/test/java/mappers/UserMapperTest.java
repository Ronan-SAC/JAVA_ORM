package mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import values.Endereco;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMapperTest {

    @Test
    @DisplayName("Deve converter ResultSet em entidade User corretamente")
    void deveConverterResultSetEmUser() throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("João da Silva");
        when(rs.getString("cpf")).thenReturn("529.982.247-25");
        when(rs.getString("email")).thenReturn("joao@email.com");
        when(rs.getString("phone")).thenReturn("11987654321");
        when(rs.getString("birth_date")).thenReturn("1990-05-15");
        when(rs.getString("address")).thenReturn("Rua das Flores, 123 - Centro - CEP: 01001-000");

        UserMapper mapper = new UserMapper();
        User user = mapper.toEntity(rs);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("João da Silva", user.getName());
        assertEquals("52998224725", user.getCpf().getDigitos());
        assertEquals("joao@email.com", user.getEmail().getValor());
        assertEquals("11", user.getPhone().getDDD());
        assertEquals("1990-05-15", user.getBirthDate());
        assertEquals("Rua das Flores", user.getAddress().getRua());
        assertEquals("123", user.getAddress().getNumero());
    }

    @Test
    @DisplayName("Deve formatar Endereco em String para gravação no banco")
    void deveFormatarEnderecoParaString() {
        UserMapper mapper = new UserMapper();
        Endereco endereco = new Endereco("Rua A", "42", "Bairro Novo", "13080-000");

        String formatted = mapper.toAddressString(endereco);
        assertNotNull(formatted);
        assertTrue(formatted.contains("Rua A"));
        assertTrue(formatted.contains("42"));
        assertTrue(formatted.contains("13080-000"));
    }
}
