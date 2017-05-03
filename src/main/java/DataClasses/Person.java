/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
public class Person implements Serializable, Comparable<Person> {
    protected String userName;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String email;
    protected String phone;
    protected Set<String> roles = new TreeSet<String>(); //roles consist of: customer, clerk, technician, manager. Technically, a person can have all roles but generally either is a customer or a combination of non-customer roles.
    protected int accessLevel; //level of access to system
    
    public Person(String fname, String lname, String uname, String psw, String email, String phone, int access){
     //handles filling in fields common to all Person-descendants, to be implemented
        this.firstName=fname;
        this.lastName=lname;
        this.userName=uname;
        this.password=psw;
        this.email=email;
        this.phone=phone;
        this.accessLevel=access;
    }
    
    public Person(){}
    
    @XmlElement
    public String getUserName(){
        return this.userName;
    }
    
    @XmlElement
    public String getFirstName(){
        return this.firstName;
    }
    
    @XmlElement
    public String getLastName(){
        return this.lastName;
    }
    
    @XmlElement
    public String getEmail(){
        return this.email;
    }
    
    @XmlElement
    public String getPhone(){
        return this.phone;
    }
    
    @XmlElement
    public String getPassword(){
        return this.password;
    }
    
    @XmlElement
    public Set<String> getRoles(){
        return this.roles;
    }
    
    @XmlElement
    public int getAccess(){
        return this.accessLevel;
    }
    
    /* User addition in UserResource checks to make sure no two users have the same username. */
    @Override
    public int hashCode(){
        int hash=1+13*this.userName.hashCode();
        return hash;
    }
    
    @Override
    public int compareTo(Person other){
        return this.hashCode()-other.hashCode();
    }
}
