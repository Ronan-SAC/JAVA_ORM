package values;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    @DisplayName("Deve instanciar Endereco com campos válidos")
    void deveCriarEnderecoValido() {
        Endereco endereco = new Endereco("Av. Paulista", "1000", "Bela Vista", "01310-100");

        assertEquals("Av. Paulista", endereco.getRua());
        assertEquals("1000", endereco.getNumero());
        assertEquals("Bela Vista", endereco.getBairro());
        assertEquals("01310-100", endereco.getCEP());
        assertTrue(endereco.validarCEP());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "01310-100",
            "01310100",
            "70002-900"
    })
    @DisplayName("validarCEP deve aceitar CEPs com 8 dígitos com ou sem traço")
    void deveAceitarCepsValidos(String cep) {
        assertTrue(Endereco.validarCEP(cep));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123",
            "01310-10",
            "01310-1000",
            "abcdefgh",
            ""
    })
    @DisplayName("validarCEP deve rejeitar CEPs com formato incorreto")
    void deveRejeitarCepsInvalidos(String cepInvalido) {
        assertFalse(Endereco.validarCEP(cepInvalido));
        assertThrows(IllegalArgumentException.class, () ->
                new Endereco("Rua A", "10", "Centro", cepInvalido));
    }

    @Test
    @DisplayName("Deve fazer parse de string formatada de endereço corretamente")
    void deveFazerParseDeStringFormatada() {
        String texto = "Rua das Acácias, 450 - Jardim Tropical - CEP: 13080-000";
        Endereco endereco = Endereco.parse(texto);

        assertEquals("Rua das Acácias", endereco.getRua());
        assertEquals("450", endereco.getNumero());
        assertEquals("Jardim Tropical", endereco.getBairro());
        assertEquals("13080-000", endereco.getCEP());
    }
}
