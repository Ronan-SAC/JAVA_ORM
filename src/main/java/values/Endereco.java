package values;

public class Endereco {
    private String rua;
    private String numero;
    private String bairro;
    private String cep;

    public Endereco(String rua, String numero, String bairro, String cep) {
        if (!validarCEP(cep)) {
            throw new IllegalArgumentException("CEP inválido: " + cep);
        }
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cep = cep.replaceAll("\\D", "");
    }

    public boolean validarCEP() {
        return validarCEP(this.cep);
    }

    public static boolean validarCEP(String cep) {
        if (cep == null) {
            return false;
        }
        String apenasDigitos = cep.replaceAll("\\D", "");
        return apenasDigitos.length() == 8;
    }

    public static Endereco parse(String texto) {
        if (texto == null || texto.isBlank()) {
            return new Endereco("Não informado", "S/N", "Centro", "00000000");
        }
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(
                "^(.*?)\\s*,\\s*(.*?)\\s*-\\s*(.*?)\\s*-\\s*CEP:\\s*(\\d{5}-?\\d{3})$"
        ).matcher(texto.trim());

        if (matcher.matches()) {
            return new Endereco(matcher.group(1).trim(), matcher.group(2).trim(), matcher.group(3).trim(), matcher.group(4).trim());
        }

        String[] partes = texto.split(",");
        if (partes.length >= 4) {
            return new Endereco(partes[0].trim(), partes[1].trim(), partes[2].trim(), partes[3].trim());
        }
        return new Endereco(texto.trim(), "S/N", "Centro", "00000000");
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
        if (cep.length() == 8) {
            return String.format("%s-%s", cep.substring(0, 5), cep.substring(5));
        }
        return cep;
    }

    public String getFormatado() {
        return String.format("%s, %s, %s, %s", rua, numero, bairro, getCEP());
    }

    @Override
    public String toString() {
        return getFormatado();
    }
}
