package values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "52998224725",
            "529.982.247-25",
            "11144477735",
            "111.444.777-35",
            "00000000191"
    })
    @DisplayName("Deve criar CPF válido com ou sem máscara")
    void deveCriarCpfValido(String numeroCpf) {
        CPF cpf = new CPF(numeroCpf);
        assertNotNull(cpf);
        assertTrue(cpf.validarCPF());
        assertEquals(11, cpf.getDigitos().length());
        assertFalse(cpf.getDigitos().contains("."));
        assertFalse(cpf.getDigitos().contains("-"));
    }

    @Test
    @DisplayName("getDigitos() deve retornar apenas os números sem formatação")
    void deveRetornarApenasDigitos() {
        CPF cpf = new CPF("529.982.247-25");
        assertEquals("52998224725", cpf.getDigitos());
        assertEquals("529.982.247-25", cpf.getFormatado());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "00000000000",
            "11111111111",
            "22222222222",
            "12345678900",
            "123",
            "1234567890123",
            "abcdefghijk",
            ""
    })
    @DisplayName("Deve rejeitar CPFs inválidos ou repetidos")
    void deveRejeitarCpfsInvalidos(String cpfInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new CPF(cpfInvalido));
        assertFalse(CPF.validarCPF(cpfInvalido));
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo")
    void deveRejeitarCpfNulo() {
        assertFalse(CPF.validarCPF(null));
        assertThrows(IllegalArgumentException.class, () -> new CPF(null));
    }
}
