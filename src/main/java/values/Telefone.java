package values;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object Telefone com regras de validação conforme Object Calisthenics:
 * - Imutabilidade
 * - Sem a palavra-chave ELSE
 * - Apenas um nível de indentação por método
 * - Métodos validarTelefone() e getDDD()
 */
public final class Telefone {

    private static final Pattern PADRAO_TELEFONE = Pattern.compile(
            "^[1-9]{2}(?:9[0-9]{8}|[2-8][0-9]{7})$"
    );

    private final String digitos;

    public Telefone(String valor) {
        String apenasDigitos = extrairDigitos(valor);
        if (!validarTelefone(apenasDigitos)) {
            throw new IllegalArgumentException("Telefone inválido: " + valor);
        }
        this.digitos = apenasDigitos;
    }

    public boolean validarTelefone() {
        return validarTelefone(this.digitos);
    }

    public static boolean validarTelefone(String telefone) {
        String numeros = extrairDigitos(telefone);
        return PADRAO_TELEFONE.matcher(numeros).matches();
    }

    public String getDDD() {
        return digitos.substring(0, 2);
    }

    public String getNumero() {
        return digitos.substring(2);
    }

    public String getDigitos() {
        return digitos;
    }

    public String getFormatado() {
        String ddd = getDDD();
        String numero = getNumero();
        if (numero.length() == 9) {
            return String.format("(%s) %s-%s", ddd, numero.substring(0, 5), numero.substring(5));
        }
        return String.format("(%s) %s-%s", ddd, numero.substring(0, 4), numero.substring(4));
    }

    private static String extrairDigitos(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefone telefone = (Telefone) o;
        return Objects.equals(digitos, telefone.digitos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digitos);
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
