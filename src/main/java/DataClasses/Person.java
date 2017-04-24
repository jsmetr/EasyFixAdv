/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
@Entity
public class Person implements Serializable {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private Set<String> roles; //roles consist of: customer, clerk, technician, manager. Technically, a person can have all roles but generally either is a customer or a combination of non-customer roles.
    private int accessLevel; //level of access to system
    
    public Person(String fname, String lname, String uname, String psw, String email, String phone, int access,Set<String> roles){
     //handles filling in fields common to all Person-descendants, to be implemented
        this.firstName=fname;
        this.lastName=lname;
        this.userName=uname;
        this.password=psw;
        this.email=email;
        this.phone=phone;
        this.accessLevel=access;
        this.roles= roles; //descendant calling super constructor is responsible for passing correct role listing
    }
    
    public Person(){}
    
    @XmlElement
    @Id
    public String getUserName(){
        return this.userName;
    }
    
    @XmlElement
    @Basic
    public String getFirstName(){
        return this.firstName;
    }
    
    @XmlElement
    @Basic
    public String getLastName(){
        return this.lastName;
    }
    
    @XmlElement
    @Basic
    public String getEmail(){
        return this.email;
    }
    
    @XmlElement
    @Basic
    public String getPhone(){
        return this.phone;
    }
    
    @XmlElement
    @Basic
    public String getPassword(){
        return this.password;
    }
    
    @XmlElement
    @ElementCollection(fetch=FetchType.EAGER)
    public Set<String> getRoles(){
        return this.roles;
    }
    
    @XmlElement
    @Basic
    public int getAccess(){
        return this.accessLevel;
    }
    
    
    public void setUserName(String uname){
        this.userName=uname;
    }
    
    public void setFirstName(String fname){
        this.firstName=fname;
    }
    
    public void setLastName(String lname){
        this.lastName=lname;
    }
    
    public void setPassword(String psw){
        this.password =psw;
    }
    
    public void setEmail(String email){
        this.email =email;
    }
    
    public void setPhone(String phone){
        this.phone =phone;
    }
    
    public void setRoles(Set<String> roles){
        this.roles =roles;
    }
    
    public void setAccess(int access){
        this.accessLevel = access;
    }
    
    /* User addition in UserResource checks to make sure no two users have the same username. */
    @Override
    public int hashCode(){
        int hash=1+13*this.userName.hashCode();
        return hash;
    }
}
