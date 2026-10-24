package values;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Value Object CPF com regras de validação conforme Object Calisthenics:
 * - Imutabilidade
 * - Sem a palavra-chave ELSE
 * - Apenas um nível de indentação por método (uso de streams funcionais)
 * - Métodos validarCPF() e getDigitos()
 */
public final class CPF {

    private final String digitos;

    public CPF(String valor) {
        String apenasDigitos = extrairDigitos(valor);
        if (!validarCPF(apenasDigitos)) {
            throw new IllegalArgumentException("CPF inválido: " + valor);
        }
        this.digitos = apenasDigitos;
    }

    public boolean validarCPF() {
        return validarCPF(this.digitos);
    }

    public static boolean validarCPF(String cpf) {
        String numeros = extrairDigitos(cpf);
        if (numeros.length() != 11) {
            return false;
        }
        if (temDigitosIguais(numeros)) {
            return false;
        }
        int primeiroDigito = calcularDigito(numeros, 10);
        if (primeiroDigito != (numeros.charAt(9) - '0')) {
            return false;
        }
        int segundoDigito = calcularDigito(numeros, 11);
        return segundoDigito == (numeros.charAt(10) - '0');
    }

    public String getDigitos() {
        return digitos;
    }

    public String getFormatado() {
        return String.format("%s.%s.%s-%s",
                digitos.substring(0, 3),
                digitos.substring(3, 6),
                digitos.substring(6, 9),
                digitos.substring(9, 11));
    }

    private static String extrairDigitos(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    private static boolean temDigitosIguais(String digitos) {
        return digitos.chars().distinct().count() == 1;
    }

    private static int calcularDigito(String digitos, int pesoInicial) {
        int soma = IntStream.range(0, pesoInicial - 1)
                .map(indice -> (digitos.charAt(indice) - '0') * (pesoInicial - indice))
                .sum();
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(digitos, cpf.digitos);
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
