package values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TelefoneTest {

    @ParameterizedTest
    @CsvSource({
            "11987654321, 11, 987654321",
            "(21) 99876-5432, 21, 998765432",
            "(31) 3214-5678, 31, 32145678",
            "4133445566, 41, 33445566"
    })
    @DisplayName("Deve criar telefone válido e extrair DDD corretamente")
    void deveCriarTelefoneValido(String entrada, String dddEsperado, String numeroEsperado) {
        Telefone telefone = new Telefone(entrada);
        assertNotNull(telefone);
        assertTrue(telefone.validarTelefone());
        assertEquals(dddEsperado, telefone.getDDD());
        assertEquals(numeroEsperado, telefone.getNumero());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "00987654321",
            "01987654321",
            "11187654321", // 11 dígitos começando com 1 em vez de 9
            "1198765432100", // excesso de dígitos
            "abcdefghij",
            ""
    })
    @DisplayName("Deve rejeitar telefones com formato inválido")
    void deveRejeitarTelefonesInvalidos(String telefoneInvalido) {
        assertThrows(IllegalArgumentException.class, () -> new Telefone(telefoneInvalido));
        assertFalse(Telefone.validarTelefone(telefoneInvalido));
    }

    @Test
    @DisplayName("Deve rejeitar telefone nulo")
    void deveRejeitarTelefoneNulo() {
        assertFalse(Telefone.validarTelefone(null));
        assertThrows(IllegalArgumentException.class, () -> new Telefone(null));
    }
}
