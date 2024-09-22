package com.mysales.bean;

import com.mysales.dao.PedidoDAO;
import com.mysales.dao.ProdutoDAO;
import com.mysales.entity.Produtos;
import com.mysales.entity.Usuario;
import jakarta.annotation.PostConstruct;
//import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Named
@SessionScoped
public class ProdutoBean implements Serializable {

    private List<Produtos> produtos;
    private Map<Produtos, Integer> carrinho; // Mapa para armazenar produtos e suas quantidades
    private double totalGeral;
    private Usuario usuarioLogado;

    @Inject
    private ProdutoDAO produtoDAO;
    @Inject
    private PedidoDAO pedidoDAO;
    @Inject
    private LoginBean login;

    public ProdutoBean() {
        carrinho = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        carregarProdutos();
        usuarioLogado = login.getUser();
    }
    
    public String recarregar() {
        init();
        return "homeSales.xhtml";
    }

    public void carregarProdutos() {
        if (produtos != null) {
            produtos.clear();
        }
        produtos = produtoDAO.carregaProdutos();
    }

    public List<Produtos> getProdutos() {
        return produtos;
    }

    public Map<Produtos, Integer> getCarrinho() {
        return carrinho;
    }

    /**
     * Adiciona ou incrementa a quantidade de um produto no carrinho.
     *
     * @param produto O produto a ser adicionado.
     * @param qtd
     * @return null para permanecer na mesma página.
     */
    public String adicionarAoCarrinho(Produtos produto, Integer qtd) {
        Integer quantidade = qtd;
        if (quantidade != null && quantidade > 0 && quantidade <= produto.getQuantidade()) {
            if (carrinho.containsKey(produto)) {
                int quantidadeAtual = carrinho.get(produto);
                int novaQuantidade = quantidadeAtual + quantidade;
                if (novaQuantidade <= produto.getQuantidade()) {
                    carrinho.put(produto, novaQuantidade);
                } else {
                    carrinho.put(produto, produto.getQuantidade());
                }
            } else {
                carrinho.put(produto, quantidade);
            }
            totalGeral = getTotalGeral();
            // Adicionar mensagem de sucesso
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Produto adicionado ao carrinho com sucesso!"));
        } else {
            // Adicionar mensagem de erro
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Quantidade inválida!", null));
        }
        // Retornar null para permanecer na mesma página
        return null;
    }

    public void removerDoCarrinho(Produtos produto) {
        carrinho.remove(produto);
    }

    public void removerQuantidadeDoCarrinho(Produtos produto, Integer qtdParaRemover) {
        if (carrinho.containsKey(produto)) {
            int quantidadeAtual = carrinho.get(produto);
            int novaQuantidade = quantidadeAtual - (qtdParaRemover != null ? qtdParaRemover : 0);

            if (novaQuantidade > 0) {
                carrinho.put(produto, novaQuantidade);
            } else {
                carrinho.remove(produto);
            }
            totalGeral = getTotalGeral();
            // Adicionar mensagem de sucesso
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Quantidade removida do carrinho com sucesso!"));
        }
    }

    public String finalizarCompra() {
        pedidoDAO.finalizarCompra(carrinho, usuarioLogado); // Salvando o pedido
        carrinho = produtoDAO.finalizarCompra(carrinho);
        carrinho.clear(); // Limpando o carrinho após finalizar a compra
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Compra finalizada com sucesso!"));
        return recarregar();
    }

    public double getTotalGeral() {
        return carrinho.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPreco() * e.getValue())
                .sum();
    }

    public String limparCarrinho() {
//        carrinho.clear();
        return "homeSales.xhtml?faces-redirect=true";
    }

    public double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
