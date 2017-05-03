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
    //may need to reorganize skills as a TreeSet of paired name-value with name as the organizing element for the tree
    private HashMap<String,Integer> skills; //Technician skills, you either have it or you don't and the skill level is approximated as an integer (0-10)
    
    public Employee(String fname, String lname, String uname, String psw, String email, String phone,int access,Set<String> roles){
        super( fname,  lname,  uname,  psw,  email,  phone, access);
        this.roles=roles;
    }
    
    public Employee(){}
    
    public void changeSkill(String typename, int lvl){
        skills.put(typename, lvl); //As a hashmap, this will either add a new value pair or update the existing one.
    }
    
    @XmlElement
    public Map<String,Integer> getSkills(){
        return this.skills;
    }
}
