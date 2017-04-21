/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Jarno
 */
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
    
    public int getAccess(){
        return this.accessLevel;
    }
    
    public String getUserName(){
        return this.userName;
    }
    
    public String getPassword(){
    return this.password;
    }
}
