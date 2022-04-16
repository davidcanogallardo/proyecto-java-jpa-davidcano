package model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class PresenceRegisterDAO {
	private EntityManager em;

	public PresenceRegisterDAO(EntityManager em) {
		this.em = em;
	}
    public TreeSet<Presence> treeSet = new TreeSet<>();

    public List<Presence> getMap() {
        return em.createQuery("select p from Presence p", model.Presence.class).getResultList();
    }

    public Presence add(Presence obj) {
    	List l = em.createQuery(
    			"select p from Presence p WHERE p.id = :custId AND p.leaveTime is null")
    			.setParameter("custId", obj.getId())
    			.getResultList();
    	if (l.size()>0) {
    		return null;
    	}

		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		
		em.persist(obj);
		
		tx.commit();
		
		return obj;

    }

    public boolean addLeaveTime(int id) {
    	List<Presence> l = em.createQuery(
    			"select p from Presence p WHERE p.id = :custId AND p.leaveTime is null", model.Presence.class)
    			.setParameter("custId", id)
    			.getResultList();
    	if (l.size()==0) {
    		return false;
    	} 

		l.get(0).setLeaveTime(LocalDateTime.now());
		em.merge(l.get(0));
    	return true;
    }

    public void list() {
        for (Presence presence : this.getMap()) {
            System.out.println(presence.toString());
        }
    }

    public void save() {
    }

    public void load() throws IOException {

    }
}
