package com.mysales.bean;

import com.mysales.dao.ProdutoDAO;
import com.mysales.entity.Produtos;
//import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class ProdutoCadastroBean implements Serializable {

    private Produtos produto; // Produto a ser manipulado
    private List<Produtos> produtos; // Lista de produtos
    private Produtos novoProduto = new Produtos();

    @Inject
    private ProdutoDAO produtoDAO;

    public ProdutoCadastroBean() {
        produto = new Produtos(); // Inicializa o objeto produto
    }

    @PostConstruct
    public void init() {
        listarProdutos(); // Carrega a lista de produtos ao iniciar
    }

    public void listarProdutos() {
        produtos = produtoDAO.listarProdutos(); // Método que deve ser implementado no DAO
    }

    public Produtos getNovoProduto() {
        return novoProduto;
    }

    public void setNovoProduto(Produtos novoProduto) {
        this.novoProduto = novoProduto;
    }

    public void cadastrarProduto() {
        produtoDAO.inserirProduto(novoProduto); // Implemente o método no DAO para salvar o novo produto
        produtos.add(novoProduto); // Adiciona o novo produto à lista local
        novoProduto = new Produtos(); // Limpa o campo após o cadastro
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto cadastrado com sucesso!"));
    }

    public void deletarProduto(Produtos produto) {
        produtoDAO.deletarProduto(produto); // Método que deve ser implementado no DAO
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto deletado com sucesso!"));
        listarProdutos(); // Atualiza a lista
    }

    public void editarProduto(Produtos produto) {
        this.produto = produto; // Carrega produto para edição
    }

    public List<Produtos> getProdutos() {
        return produtos;
    }

    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public void atualizarProduto(Produtos produto) {
        produtoDAO.atualizarProduto(produto); // Implemente este método no DAO
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto atualizado com sucesso!"));
        listarProdutos(); // Atualiza a lista após a edição
    }
}
