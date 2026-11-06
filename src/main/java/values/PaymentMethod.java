package values;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PaymentMethod {
    public static final String PIX_STR = "PIX";
    public static final String BANK_SLIP_STR = "BOLETO";
    public static final String CREDIT_CARD_STR = "CARTAO_CREDITO";
    public static final String DEBIT_CARD_STR = "CARTAO_DEBITO";
    public static final String CASH_STR = "DINHEIRO";

    public static final PaymentMethod PIX = new PaymentMethod(PIX_STR);
    public static final PaymentMethod BANK_SLIP = new PaymentMethod(BANK_SLIP_STR);
    public static final PaymentMethod CREDIT_CARD = new PaymentMethod(CREDIT_CARD_STR);
    public static final PaymentMethod DEBIT_CARD = new PaymentMethod(DEBIT_CARD_STR);
    public static final PaymentMethod CASH = new PaymentMethod(CASH_STR);

    private static final List<String> VALID_METHODS = Arrays.asList(
            PIX_STR, BANK_SLIP_STR, CREDIT_CARD_STR, DEBIT_CARD_STR, CASH_STR
    );

    private final String value;

    public PaymentMethod(String value) {
        if (!validate(value)) {
            throw new IllegalArgumentException("Forma de pagamento inválida: " + value);
        }
        this.value = normalize(value);
    }

    public boolean validate() {
        return validate(this.value);
    }

    public static boolean validate(String method) {
        if (method == null || method.isBlank()) {
            return false;
        }
        String normalized = normalize(method);
        return VALID_METHODS.contains(normalized);
    }

    public static String normalize(String method) {
        if (method == null) {
            return "";
        }
        String clean = method.trim().toUpperCase()
                .replace("Ã", "A")
                .replace("Â", "A")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Ê", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ô", "O")
                .replace("Ú", "U")
                .replace("Ç", "C")
                .replace(" ", "_")
                .replace("-", "_");

        if (clean.equals("CARTAO_DE_CREDITO") || clean.equals("CREDIT_CARD")) {
            return CREDIT_CARD_STR;
        }
        if (clean.equals("CARTAO_DE_DEBITO") || clean.equals("DEBIT_CARD")) {
            return DEBIT_CARD_STR;
        }
        if (clean.equals("BANK_SLIP")) {
            return BANK_SLIP_STR;
        }
        if (clean.equals("CASH")) {
            return CASH_STR;
        }
        return clean;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return switch (value) {
            case PIX_STR -> "Pix";
            case BANK_SLIP_STR -> "Boleto Bancário";
            case CREDIT_CARD_STR -> "Cartão de Crédito";
            case DEBIT_CARD_STR -> "Cartão de Débito";
            case CASH_STR -> "Dinheiro";
            default -> value;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
