package model;

import java.io.Serializable;
import java.util.LinkedHashSet;

import javax.persistence.Entity;

@Entity
public class Client extends Person implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Client() {
		super();
	}
    public Client(Integer id, String dni, String name, String surname, Address fullAddress) {
        super(id, dni, name, surname, fullAddress);
    }
    public Client(Integer id, String dni, String name, String surname, Address fullAddress, LinkedHashSet<String> phoneNumber) {
        super(id, dni, name, surname, fullAddress, phoneNumber);
    }
}
