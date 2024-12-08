/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pp_fp07.arena.espacesistem;

import java.io.Serializable;
import java.io.*;
import java.util.Base64;

/**
 *
 * @author RyanS
 */
public class Admin implements Serializable {
    private String nomeDeUtilizador; // Nome de utilizador (username)
    private String email; // Email do utilizador
    private String password; // Senha do utilizador

    public Admin(String nomeDeUtilizador, String email, String password) {
        this.nomeDeUtilizador = nomeDeUtilizador;
        this.email = email;
        this.password = password;
    }

    public String getNomeDeUtilizador() {
        return nomeDeUtilizador;
    }

    public void setNomeDeUtilizador(String nomeDeUtilizador) {
        this.nomeDeUtilizador = nomeDeUtilizador;
    }

    public String getEmail(Criptografia criptografia) { // Armazena o email criptografado
        return criptografia.descriptografar(email); // Retorna o e-mail descriptografado
    }

    public void setEmail(String email, Criptografia criptografia) {
        this.email = criptografia.criptografar(email); // Salva o e-mail criptografado
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
   

    
}
