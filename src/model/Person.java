package model;

import java.io.Serializable;
import java.util.LinkedHashSet;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Inheritance(strategy = InheritanceType.JOINED) 
@Entity
public abstract class Person implements Identificable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    private Integer id;
    private String dni;
    private String name;
    private String surname;
    private LinkedHashSet<String> phoneNumber;
    @Embedded
    private Address fullAddress;
    private static int totalPeople;

    public Person() {
        this.id = null;
        this.dni = "";
        this.name = "";
        this.surname = "";
        this.fullAddress = null;

        totalPeople++;
    }
    
    protected Person(Integer id, String dni, String name, String surname, Address fullAddress) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.fullAddress = fullAddress;

        totalPeople++;
    }

    protected Person(Integer id, String dni, String name, String surname, Address fullAddress, LinkedHashSet<String> phoneNumber) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.fullAddress = fullAddress;
        this.phoneNumber = phoneNumber;

        totalPeople++;
    }

    @Override
    public String toString() {
        return "Person [" + "id=" + id + ", dni=" + dni + ", name=" + name + ", surname=" + surname
                + ", phoneNumber=" + phoneNumber + ", fullAddress="
                + fullAddress + ']';
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoneNumber(LinkedHashSet<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setFullAddress(Address fullAddress) {
        this.fullAddress = fullAddress;
    }

    public static void setTotalPeople(Integer totalPeople) {
        Person.totalPeople = totalPeople;
    }

    public Integer getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LinkedHashSet<String> getPhoneNumber() {
        return phoneNumber;
    }
    
    public String[] getPhonesAsArray() {
        String[] phones  = new String[this.phoneNumber.size()];
        return this.phoneNumber.toArray(phones);
    }
    public Address getFullAddress() {
        return fullAddress;
    }

    public static Integer getTotalPeople() {
        return totalPeople;
    }
}