package values;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Value Object Endereco com métodos requeridos:
 * - getRua()
 * - getBairro()
 * - getNumero()
 * - getCEP()
 * - validarCEP()
 * 
 * Regras Object Calisthenics:
 * - Imutabilidade
 * - Sem a palavra-chave ELSE
 * - Apenas um nível de indentação por método
 */
public final class Endereco {

    private static final Pattern PADRAO_CEP = Pattern.compile("^\\d{5}-?\\d{3}$");
    private static final Pattern PADRAO_PARSER = Pattern.compile(
            "^(.*?)\\s*,\\s*(.*?)\\s*-\\s*(.*?)\\s*-\\s*CEP:\\s*(\\d{5}-?\\d{3})$"
    );

    private final String rua;
    private final String numero;
    private final String bairro;
    private final String cep;

    public Endereco(String rua, String numero, String bairro, String cep) {
        if (!validarCEP(cep)) {
            throw new IllegalArgumentException("CEP inválido: " + cep);
        }
        this.rua = sanitizar(rua, "Rua");
        this.numero = sanitizar(numero, "Número");
        this.bairro = sanitizar(bairro, "Bairro");
        this.cep = formatarCEP(cep);
    }

    public boolean validarCEP() {
        return validarCEP(this.cep);
    }

    public static boolean validarCEP(String cep) {
        if (cep == null) {
            return false;
        }
        return PADRAO_CEP.matcher(cep.trim()).matches();
    }

    public static Endereco parse(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Texto de endereço não pode ser vazio.");
        }
        Matcher matcher = PADRAO_PARSER.matcher(texto.trim());
        if (matcher.matches()) {
            return new Endereco(
                    matcher.group(1).trim(),
                    matcher.group(2).trim(),
                    matcher.group(3).trim(),
                    matcher.group(4).trim()
            );
        }
        return parsePorVirgula(texto);
    }

    private static Endereco parsePorVirgula(String texto) {
        String[] partes = texto.split(",");
        if (partes.length >= 4) {
            return new Endereco(
                    partes[0].trim(),
                    partes[1].trim(),
                    partes[2].trim(),
                    partes[3].trim()
            );
        }
        return new Endereco(texto.trim(), "S/N", "Não informado", "00000-000");
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCEP() {
        return cep;
    }

    public String getFormatado() {
        return String.format("%s, %s - %s - CEP: %s", rua, numero, bairro, cep);
    }

    private static String sanitizar(String campo, String nomeCampo) {
        if (campo == null || campo.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " não pode ser nulo ou vazio.");
        }
        return campo.trim();
    }

    private static String formatarCEP(String cep) {
        String apenasDigitos = cep.replaceAll("\\D", "");
        return String.format("%s-%s", apenasDigitos.substring(0, 5), apenasDigitos.substring(5, 8));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(rua, endereco.rua) &&
                Objects.equals(numero, endereco.numero) &&
                Objects.equals(bairro, endereco.bairro) &&
                Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rua, numero, bairro, cep);
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
