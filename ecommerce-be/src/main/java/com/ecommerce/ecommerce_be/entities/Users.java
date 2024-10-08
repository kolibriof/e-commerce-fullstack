package com.ecommerce.ecommerce_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users", schema = "ecommerce")
public class Users {

    @Id()
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @SequenceGenerator(name = "id_seq", allocationSize = 1)
    private Integer Id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private Float balance;

    private Users(){}

    public void setId(Integer id) {
        this.Id = id;
    }

    public Integer getId() {
        return Id;
    }

    public @NotBlank String getLogin() {
        return login;
    }

    public @NotBlank Float getBalance() {
        return balance;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public Users(Integer id, String login, String password, Float balance) {
        this.Id = id;
        this.balance = balance;
        this.login = login;
        this.password = password;
    }
}
