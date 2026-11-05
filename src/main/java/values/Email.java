package values;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern PADRAO = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private String valor;

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
        return PADRAO.matcher(email.trim()).matches();
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
