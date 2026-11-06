package values;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address {
    private String street;
    private String number;
    private String neighborhood;
    private String zipCode;

    public Address(String street, String number, String neighborhood, String zipCode) {
        if (!validateZipCode(zipCode)) {
            throw new IllegalArgumentException("CEP inválido: " + zipCode);
        }
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.zipCode = zipCode.replaceAll("\\D", "");
    }

    public boolean validateZipCode() {
        return validateZipCode(this.zipCode);
    }

    public static boolean validateZipCode(String zipCode) {
        if (zipCode == null) {
            return false;
        }
        String onlyDigits = zipCode.replaceAll("\\D", "");
        return onlyDigits.length() == 8;
    }

    public static Address parse(String text) {
        if (text == null || text.isBlank()) {
            return new Address("Não informado", "S/N", "Centro", "00000000");
        }
        Matcher matcher = Pattern.compile(
                "^(.*?)\\s*,\\s*(.*?)\\s*-\\s*(.*?)\\s*-\\s*CEP:\\s*(\\d{5}-?\\d{3})$"
        ).matcher(text.trim());

        if (matcher.matches()) {
            return new Address(matcher.group(1).trim(), matcher.group(2).trim(), matcher.group(3).trim(), matcher.group(4).trim());
        }

        String[] parts = text.split(",");
        if (parts.length >= 4) {
            return new Address(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
        }
        return new Address(text.trim(), "S/N", "Centro", "00000000");
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getZipCode() {
        if (zipCode.length() == 8) {
            return String.format("%s-%s", zipCode.substring(0, 5), zipCode.substring(5));
        }
        return zipCode;
    }

    public String getFormatted() {
        return String.format("%s, %s, %s, %s", street, number, neighborhood, getZipCode());
    }

    @Override
    public String toString() {
        return getFormatted();
    }
}
