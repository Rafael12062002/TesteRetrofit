package com.example.testeretrofitactivit.model;

import java.io.Serializable;

public class Usuario implements Serializable {


    private String Email;
    private String Senha;
    private long id;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
