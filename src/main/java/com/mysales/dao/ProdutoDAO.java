package com.mysales.dao;

import com.mysales.entity.Produtos;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Stateless
public class ProdutoDAO {
    
    @PersistenceContext
    private EntityManager em;

    public List<Produtos> carregaProdutos() {
        TypedQuery<Produtos> query = em.createQuery(
                "SELECT p FROM Produtos p", Produtos.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public Map<Produtos, Integer> finalizarCompra(Map<Produtos, Integer> carrinho) {
        try {
            for (Map.Entry<Produtos, Integer> entry : carrinho.entrySet()) {
                Produtos produto = em.find(Produtos.class, entry.getKey().getId());
                
                if (produto != null && produto.getQuantidade() >= entry.getValue()) {
                    // Atualiza a quantidade do produto
                    produto.setQuantidade(produto.getQuantidade() - entry.getValue());
                    em.merge(produto);
                } else if (produto != null) {
                    // Gerenciar caso de estoque insuficiente
                    throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
                }
            }
            carrinho.clear();
            // Adicionar mensagens de sucesso
        } catch (Exception e) {
            // Adicionar mensagens de erro, logar a exceção, etc.
            e.printStackTrace(); // Para fins de depuração
        }
        return carrinho;
    }

    public Map<Produtos, Integer> finalizarCompraAtualizaEstoque(Map<Produtos, Integer> carrinho) {
        return null;
    }
    
    public List<Produtos> listarProdutos() {
        return em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
    }

    @Transactional
    public void inserirProduto(Produtos produto) {
        em.persist(produto);
    }

    @Transactional
    public void atualizarProduto(Produtos produto) {
        em.merge(produto);
    }

    @Transactional
    public void deletarProduto(Produtos produto) {
        em.remove(em.contains(produto) ? produto : em.merge(produto));
    }
}
