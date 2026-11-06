package values;

public class Phone {
    private String digits;

    public Phone(String value) {
        String onlyDigits = extractDigits(value);
        if (!validate(onlyDigits)) {
            throw new IllegalArgumentException("Telefone inválido: " + value);
        }
        this.digits = onlyDigits;
    }

    public boolean validate() {
        return validate(this.digits);
    }

    public static boolean validate(String phone) {
        String numbers = extractDigits(phone);
        if (numbers.length() < 10 || numbers.length() > 11) {
            return false;
        }
        return numbers.matches("^[1-9]{2}(?:9[0-9]{8}|[2-8][0-9]{7})$");
    }

    public String getAreaCode() {
        return digits.substring(0, 2);
    }

    public String getNumber() {
        return digits.substring(2);
    }

    public String getDigits() {
        return digits;
    }

    private static String extractDigits(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }

    @Override
    public String toString() {
        if (digits.length() == 11) {
            return String.format("(%s) %s-%s", getAreaCode(), digits.substring(2, 7), digits.substring(7));
        }
        return String.format("(%s) %s-%s", getAreaCode(), digits.substring(2, 6), digits.substring(6));
    }
}
