package values;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private String value;

    public Email(String value) {
        if (!validate(value)) {
            throw new IllegalArgumentException("Email inválido: " + value);
        }
        this.value = value.trim().toLowerCase();
    }

    public boolean validate() {
        return validate(this.value);
    }

    public static boolean validate(String email) {
        if (email == null) {
            return false;
        }
        return PATTERN.matcher(email.trim()).matches();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
