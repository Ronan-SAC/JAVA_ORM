package entitys;

import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

import java.time.LocalDate;

public class Usuario extends User {

    public Usuario(Long id, String nome, CPF cpf, Email email, Telefone telefone, LocalDate dataNascimento, Endereco endereco) {
        super(id, nome, cpf, email, telefone, dataNascimento, endereco);
    }

    public Usuario(String nome, CPF cpf, Email email, Telefone telefone, LocalDate dataNascimento, Endereco endereco) {
        super(nome, cpf, email, telefone, dataNascimento, endereco);
    }

    public String getNome() {
        return getName();
    }

    public Telefone getTelefone() {
        return getPhone();
    }

    public LocalDate getDataNascimento() {
        return getBirthDate();
    }

    public Endereco getEndereco() {
        return getAddress();
    }
}
