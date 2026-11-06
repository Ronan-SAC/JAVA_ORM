package values;

public class Cpf {
    private String digits;

    public Cpf(String value) {
        String onlyDigits = extractDigits(value);
        if (!validate(onlyDigits)) {
            throw new IllegalArgumentException("CPF inválido: " + value);
        }
        this.digits = onlyDigits;
    }

    public boolean validate() {
        return validate(this.digits);
    }

    public static boolean validate(String cpf) {
        String numbers = extractDigits(cpf);
        if (numbers.length() != 11) {
            return false;
        }
        if (numbers.chars().distinct().count() == 1) {
            return false;
        }
        int digit1 = calculateDigit(numbers, 10);
        if (digit1 != (numbers.charAt(9) - '0')) {
            return false;
        }
        int digit2 = calculateDigit(numbers, 11);
        return digit2 == (numbers.charAt(10) - '0');
    }

    public String getDigits() {
        return digits;
    }

    public String getFormatted() {
        return String.format("%s.%s.%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9, 11));
    }

    private static String extractDigits(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }

    private static int calculateDigit(String numbers, int weight) {
        int sum = 0;
        for (int i = 0; i < weight - 1; i++) {
            sum += (numbers.charAt(i) - '0') * (weight - i);
        }
        int remainder = 11 - (sum % 11);
        return (remainder > 9) ? 0 : remainder;
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
