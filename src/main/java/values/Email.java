package values;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object Email com regras de validação conforme Object Calisthenics:
 * - Imutabilidade
 * - Sem a palavra-chave ELSE
 * - Apenas um nível de indentação por método
 * - Envolve tipo primitivo String
 */
public final class Email {

    private static final Pattern PADRAO_EMAIL = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    private final String valor;

    public Email(String valor) {
        if (!validarEmail(valor)) {
            throw new IllegalArgumentException("Email inválido: " + valor);
        }
        this.valor = valor.trim().toLowerCase();
    }

    public boolean validarEmail() {
        return validarEmail(this.valor);
    }

    public static boolean validarEmail(String email) {
        if (email == null) {
            return false;
        }
        return PADRAO_EMAIL.matcher(email.trim()).matches();
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email outro = (Email) o;
        return Objects.equals(valor, outro.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
