/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import DataClasses.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jarno
 */
public class UserManager implements Serializable {

    private String filename = "UserManagerStorage"; //Must match UserHolder.getManager() deserialization filename.
    private HashSet<Person> users = new HashSet<Person>(); //A single user shows up in multiple sets. This is for searchability reasons.
    private HashSet<Customer> customers = new HashSet<Customer>();
    private HashSet<Employee> employees = new HashSet<Employee>();

    public static UserManager getInstance() {
        return UserHolder.INSTANCE;
    }

    //An attempt at a self-loading Singleton class. If this does not work a separate loader class for the singletons will be used instead.
    private static class UserHolder {

        private static final UserManager INSTANCE = getManager();

        private static UserManager getManager() {
            try {
                return (UserManager) Serializer.deserialize("UserManagerStorage");
            } catch (Exception e) {
                return new UserManager();
            }
        }
    }

    public void addCustomer(Customer cust) {
        users.add(cust);
        customers.add(cust);
        saveMyself();
    }

    public void addEmployee(Employee emp) {
        users.add(emp);
        employees.add(emp);
        saveMyself();
    }

    //resets the userbase
    public void nullAndVoid() {
        this.users = new HashSet<Person>(); 
        this.customers = new HashSet<Customer>();
        this.employees = new HashSet<Employee>();
        saveMyself();
    }

    public Set<Person> getUsers() {
        return users;
    }

    private void saveMyself() {
        try {
            Serializer.serialize(this, filename);
        } catch (Exception e) {
            System.out.println("Failed to save to " + filename);
        }

    }
}
