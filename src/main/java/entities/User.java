package entities;

import values.Address;
import values.Cpf;
import values.Email;
import values.Phone;

public class User {
    private Integer id;
    private String name;
    private Cpf cpf;
    private Email email;
    private Phone phone;
    private String birthDate;
    private Address address;

    public User(Integer id, String name, Cpf cpf, Email email, Phone phone, String birthDate, Address address) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
    }

    public User(String name, Cpf cpf, Email email, Phone phone, String birthDate, Address address) {
        this(null, name, cpf, email, phone, birthDate, address);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + name + '\'' +
                ", cpf=" + cpf +
                ", email=" + email +
                ", telefone=" + phone +
                ", dataNascimento='" + birthDate + '\'' +
                ", endereco=" + address +
                '}';
    }
}
