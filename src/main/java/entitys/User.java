package entitys;

import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.time.LocalDate;
import java.util.Objects;
public class User {

    private final Long id;
    private final String name;
    private final CPF cpf;
    private final Email email;
    private final Telefone phone;
    private final LocalDate birthDate;
    private final Endereco address;

    public User(Long id, String name, CPF cpf, Email email, Telefone phone, LocalDate birthDate, Endereco address) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.id = id;
        this.name = name.trim();
        this.cpf = Objects.requireNonNull(cpf, "CPF não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo.");
        this.phone = Objects.requireNonNull(phone, "Telefone não pode ser nulo.");
        this.birthDate = Objects.requireNonNull(birthDate, "Data de nascimento não pode ser nula.");
        this.address = Objects.requireNonNull(address, "Endereço não pode ser nulo.");
    }

    public User(String name, CPF cpf, Email email, Telefone phone, LocalDate birthDate, Endereco address) {
        this(null, name, cpf, email, phone, birthDate, address);
    }

    public User withId(Long newId) {
        return new User(newId, this.name, this.cpf, this.email, this.phone, this.birthDate, this.address);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CPF getCpf() {
        return cpf;
    }

    public Email getEmail() {
        return email;
    }

    public Telefone getPhone() {
        return phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Endereco getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(cpf, user.cpf) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpf=" + cpf +
                ", email=" + email +
                ", phone=" + phone +
                ", birthDate=" + birthDate +
                ", address=" + address +
                '}';
    }
}
