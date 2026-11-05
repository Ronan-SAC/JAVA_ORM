package values;

public class Telefone {
    private String digitos;

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
        String nums = extrairDigitos(telefone);
        if (nums.length() < 10 || nums.length() > 11) {
            return false;
        }
        return nums.matches("^[1-9]{2}(?:9[0-9]{8}|[2-8][0-9]{7})$");
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

    private static String extrairDigitos(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replaceAll("\\D", "");
    }

    @Override
    public String toString() {
        if (digitos.length() == 11) {
            return String.format("(%s) %s-%s", getDDD(), digitos.substring(2, 7), digitos.substring(7));
        }
        return String.format("(%s) %s-%s", getDDD(), digitos.substring(2, 6), digitos.substring(6));
    }
}
