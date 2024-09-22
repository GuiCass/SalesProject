package com.mysales.bean;

import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import com.mysales.dao.UsuarioDAO;
import com.mysales.entity.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String message;
    private Usuario user;
    
    @Inject
    private UsuarioDAO usuarioDAO;
    
    @PostConstruct
    public void init() {
        // Limpa os dados do login
        username = "";
        password = "";
        message = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }
    
    public Usuario getUser() {
        return user;
    }
    
    public String login() {
        Usuario usuario = usuarioDAO.findUser(this.username, this.password);
        user = usuario;
        if (usuario != null) {
            // Login bem-sucedido
            return "homeSales.xhtml";
        }
        // Falha no login
        this.message = "Nome ou senha inválidos.";
        return null;
    }
}

