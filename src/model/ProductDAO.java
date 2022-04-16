package model;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ProductDAO implements Persistable<Product>, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private EntityManager em;

    public ProductDAO(EntityManager em) {
    	this.em = em;
    }

    public Product add(Product obj) {
    	//System.out.println(obj.toString());
		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		em.persist(obj);

		tx.commit();
		return obj;
    }

    public Product delete(Product obj) {
		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		if (obj instanceof Pack) {
			em.remove(em.find(Pack.class, obj.getId()));
		} else {
			em.remove(em.find(Product.class, obj.getId()));
		}
		tx.commit();
    	return obj;
    }

    public Product get(Integer id, boolean isPack) {
    	if (isPack) {
    		//return (Pack) em.createQuery("SELECT p FROM Pack p where p.id = "+id, Pack.class).getResultList().get(0);
    		List<Product> result = (List<Product>) em.createNativeQuery("SELECT * FROM Pack where id ="+id, Pack.class).getResultList();
    		if (result.size() == 0) {
    			return null;
    		} else {
    			return result.get(0);
    		}
    	} else {
    		List<Product> result = (List<Product>) em.createNativeQuery("SELECT * FROM Product where id ="+id, Product.class).getResultList();
    		if (result.size() == 0) {
    			return null;
    		} else {
    			return result.get(0);
    		}
    		//return (Product) em.createNativeQuery("SELECT * FROM Product where id ="+id, Product.class).getResultList().get(0);
    	}
    }

    public List<Pack> getPackMap() {
        return em.createQuery("SELECT p FROM Product p WHERE p.discriminador like 'Pack'", model.Pack.class).getResultList();
    }

    public List<Product> getProdMap() {
    	return em.createQuery("SELECT p FROM Product p WHERE p.discriminador like 'Prod'", model.Product.class).getResultList();
    }

    public void modify(Product obj) {
		EntityTransaction tx = em.getTransaction(); 
		tx.begin();
		em.merge(obj);

		tx.commit();
    }

    public void save() {
    }

    public void load() throws IOException {
    }

    public List<Product> getDiscontinuedProducts(LocalDate date) {
        List<Product> list = new ArrayList<Product>();
        for (Product product : this.getProdMap()) {
            if (product.getEndCatalog().isBefore(date)) {
                list.add(product);
            }
        }

        return list;
    }
    @Override
    public Product get(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

}
