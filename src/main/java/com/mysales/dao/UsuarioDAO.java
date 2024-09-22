package com.mysales.dao;

import com.mysales.entity.Usuario;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;

@Stateless
public class UsuarioDAO {

    @PersistenceContext
    private EntityManager em;

    public Usuario findUser(String nome, String password) {
        TypedQuery<Usuario> query = em.createQuery(
                "SELECT u FROM Usuario u WHERE u.nome = :nome and u.password =:password", Usuario.class);
        query.setParameter("nome", nome);
        query.setParameter("password", password);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listarUser() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    @Transactional
    public void inserirCliente(Usuario novoUser) {
        em.persist(novoUser);
    }

    @Transactional
    public void deletarUser(Usuario user) {
        em.remove(em.contains(user) ? user : em.merge(user));
    }
}
