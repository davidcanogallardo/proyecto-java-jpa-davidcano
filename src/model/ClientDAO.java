package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;


public class ClientDAO implements Persistable<Client>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManager em;

    public ClientDAO(EntityManager em) {
        this.em = em;
    }
    
    public Client add(Client obj) {
        if (em.find(Client.class, obj.getId()) != null) {
            return null;
        }

		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		
		em.persist(obj);
		
		tx.commit();
		return obj;
    }

    public Client delete(Client obj) {
		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		em.remove(em.find(Client.class, obj.getId()));
		tx.commit();
		return obj;
    }

    public Client get(Integer id) {
        return em.find(Client.class, id);
    }

    public List<Client> getPackMap() {
        return em.createQuery("select c from Client c", model.Client.class).getResultList();
    }

    public void modify(Client obj) {
		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		
		em.merge(obj);
		
		tx.commit();
    }

    public void save() {
    }

    public void load() throws IOException {
    }

}
