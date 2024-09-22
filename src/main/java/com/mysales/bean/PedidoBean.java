package com.mysales.bean;

import com.mysales.dao.PedidoDAO;
import com.mysales.entity.Pedido; // Supondo que você tenha uma entidade Pedido
import com.mysales.entity.Usuario;
//import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class PedidoBean implements Serializable {

    private List<Pedido> pedidos; // Lista de pedidos do usuário
    private Usuario usuarioLogado;

    @Inject
    private PedidoDAO pedidoDAO;
    
    @Inject
    private LoginBean login;

    @PostConstruct
    public void init() {
        usuarioLogado = login.getUser();
        carregarPedidos(); // Carrega os pedidos ao iniciar
    }

    public void carregarPedidos() {
        // Suponha que você tenha um método no DAO para buscar os pedidos do usuário logado
        pedidos = pedidoDAO.listarPedidosDoUsuario(getUsuarioLogadoId());
    }

    private Long getUsuarioLogadoId() {
        return usuarioLogado.getId(); // Placeholder
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}

