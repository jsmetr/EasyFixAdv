/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import DataClasses.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Jarno
 */
public class UserManager implements Serializable {

    private String filename = "UserManagerStorage"; //Must match UserHolder.getManager() deserialization filename.
    private TreeSet<Person> users = new TreeSet<Person>(); //A single user shows up in multiple sets. This is for searchability reasons.
    private TreeSet<Customer> customers = new TreeSet<Customer>();
    private TreeSet<Employee> employees = new TreeSet<Employee>();

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

    public boolean addCustomer(Customer cust) {
        if (users.add(cust)) {
            customers.add(cust);
            save();
            return true;
        }
        return false;
    }

    public boolean addEmployee(Employee emp) {
        if (users.add(emp)) {
            employees.add(emp);
            save();
            return true;
        }
        return false;
    }

    /*
    As Assignments & Devices do not store references to users (to avoid serialization duplication), fetching users by username is provided.
    Check roles and cast into appropriate class as needed.
     */
    public Person findUser(String username) {
        Iterator<Person> iter = users.iterator();
        while (iter.hasNext()) {
            Person crnt = iter.next();
            if (crnt.getUserName().equals(username)) {
                return crnt;
            }
        }
        return null;
    }

    //resets the userbase
    public void nullAndVoid() {
        this.users.clear();
        this.customers.clear();
        this.employees.clear();
        save();
    }

    public Set<Person> getUsers() {
        return users;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void save() {
        try {
            Serializer.serialize(this, filename);
        } catch (Exception e) {
            System.out.println("Failed to save to " + filename);
        }
    }
}
