package com.mysales.dao;

import com.mysales.entity.Pedido;
import com.mysales.entity.Produtos;
import com.mysales.entity.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

import java.util.Map;

@Stateless
public class PedidoDAO {
    
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void finalizarCompra(Map<Produtos, Integer> carrinho, Usuario usuario) {
        Pedido pedido = new Pedido();
        pedido.setData(java.time.LocalDate.now().toString()); // Ou outra forma de definir a data
        pedido.setValorTotal(calcularValorTotal(carrinho));
        pedido.setClient(usuario); // Definindo o usuário associado ao pedido

        em.persist(pedido); // Persistindo o pedido
    }

    private Double calcularValorTotal(Map<Produtos, Integer> carrinho) {
        return carrinho.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum();
    }

    public List<Pedido> listarPedidosDoUsuario(Long usuarioId) {
        String jpql = "SELECT p FROM Pedido p WHERE p.client.id = :usuarioId";
        TypedQuery<Pedido> query = em.createQuery(jpql, Pedido.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }
}

