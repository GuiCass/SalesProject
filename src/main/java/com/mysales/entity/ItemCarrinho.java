package com.mysales.entity;

public class ItemCarrinho {
    private Produtos produto;
    private Integer quantidade;
    private Integer qtdParaRemover;

    // Construtor
    public ItemCarrinho(Produtos produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.qtdParaRemover = 0; // Inicializa como 0
    }

    // Getters e setters
    public Produtos getProduto() {
        return produto;
    }

    public void setProduto(Produtos produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getQtdParaRemover() {
        return qtdParaRemover;
    }

    public void setQtdParaRemover(Integer qtdParaRemover) {
        this.qtdParaRemover = qtdParaRemover;
    }
}

