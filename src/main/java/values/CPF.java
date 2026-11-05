package values;

public class CPF {
    private String digitos;

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
        String nums = extrairDigitos(cpf);
        if (nums.length() != 11) {
            return false;
        }
        if (nums.chars().distinct().count() == 1) {
            return false;
        }
        int digito1 = calcularDigito(nums, 10);
        if (digito1 != (nums.charAt(9) - '0')) {
            return false;
        }
        int digito2 = calcularDigito(nums, 11);
        return digito2 == (nums.charAt(10) - '0');
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

    private static int calcularDigito(String nums, int peso) {
        int soma = 0;
        for (int i = 0; i < peso - 1; i++) {
            soma += (nums.charAt(i) - '0') * (peso - i);
        }
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
