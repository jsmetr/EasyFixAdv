/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Employee extends Person{
    private HashMap<String,Integer> skills; //Technician skills, you either have it or you don't and the skill level is approximated as an integer (0-10)
    
    public Employee(String fname, String lname, String uname, String psw, String email, String phone,int access,Set<String> roles){
        super( fname,  lname,  uname,  psw,  email,  phone, access);
        this.roles=roles;
    }
    
    public void changeSkill(String typename, int lvl){
        skills.put(typename, lvl); //As a hashmap, this will either add a new value pair or update the existing one.
    }
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
    
    //@XmlElement
    public Map<String,Integer> getSkills(){
        return this.skills;
    }
}
