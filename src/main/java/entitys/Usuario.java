package entitys;

import values.CPF;
import values.Email;
import values.Endereco;
import values.Telefone;

public class Usuario {
    private Integer id;
    private String nome;
    private CPF cpf;
    private Email email;
    private Telefone telefone;
    private String dataNascimento;
    private Endereco endereco;

    public Usuario(Integer id, String nome, CPF cpf, Email email, Telefone telefone, String dataNascimento, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
    }

    public Usuario(String nome, CPF cpf, Email email, Telefone telefone, String dataNascimento, Endereco endereco) {
        this(null, nome, cpf, email, telefone, dataNascimento, endereco);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CPF getCpf() {
        return cpf;
    }

    public void setCpf(CPF cpf) {
        this.cpf = cpf;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf=" + cpf +
                ", email=" + email +
                ", telefone=" + telefone +
                ", dataNascimento='" + dataNascimento + '\'' +
                ", endereco=" + endereco +
                '}';
    }
}
