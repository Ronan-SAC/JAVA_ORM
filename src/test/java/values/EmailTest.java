package values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "usuario@dominio.com",
            "teste.silva@empresa.com.br",
            "nome+alias@sub.dominio.org"
    })
    @DisplayName("Deve criar Email válido com sucesso")
    void deveCriarEmailValido(String endereco) {
        Email email = new Email(endereco);
        assertNotNull(email);
        assertTrue(email.validarEmail());
        assertEquals(endereco.trim().toLowerCase(), email.getValor());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "email_sem_arroba.com",
            "@semusuario.com",
            "usuario@",
            "usuario@dominio",
            "usuario@.com",
            ""
    })
    @DisplayName("Deve lançar exceção para emails inválidos")
    void deveFalharParaEmailInvalido(String enderecoInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Email(enderecoInvalido));
        assertFalse(Email.validarEmail(enderecoInvalido));
    }

    @Test
    @DisplayName("validarEmail estático deve retornar false para nulo")
    void deveRetornarFalseParaEmailNulo() {
        assertFalse(Email.validarEmail(null));
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }
}
