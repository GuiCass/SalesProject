package com.mysales.bean;

import com.mysales.dao.UsuarioDAO;
import com.mysales.entity.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class CadastroClientBean {
    
    private Usuario user; // Produto a ser manipulado
    private List<Usuario> users; // Lista de produtos
    private Usuario novoUser = new Usuario();
    
    @Inject
    private UsuarioDAO useroDAO;
    
    @PostConstruct
    public void init() {
        listarUsuarios(); // Carrega a lista de produtos ao iniciar
    }
    
    public CadastroClientBean() {
        user = new Usuario(); // Inicializa o objeto produto
    }
    
    public void listarUsuarios() {
        users = useroDAO.listarUser(); // Método que deve ser implementado no DAO
    }
    
    public void cadastrarCliente() {
        useroDAO.inserirCliente(novoUser); // Implemente o método no DAO para salvar o novo produto
        users.add(novoUser); // Adiciona o novo produto à lista local
        novoUser = new Usuario(); // Limpa o campo após o cadastro
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario cadastrado com sucesso!"));
    }
    
    public void deletarCliente(Usuario user) {
        useroDAO.deletarUser(user); // Método que deve ser implementado no DAO
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario deletado com sucesso!"));
        listarUsuarios(); // Atualiza a lista
    }
    
    public Usuario getUser() {
        return user;
    }
    
    public List<Usuario> getUsers() {
        return users;
    }
    
    public Usuario getNovoUser() {
        return novoUser;
    }

    public void setNovoProduto(Usuario novoUser) {
        this.novoUser = novoUser;
    }
}
