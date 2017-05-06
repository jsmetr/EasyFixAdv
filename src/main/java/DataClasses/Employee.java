/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.Set;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Employee extends Person{
    //may need to reorganize skills as a TreeSet of paired name-value with name as the organizing element for the tree
    private Set<RepairSkill> skills; //Technician skills, you either have it or you don't and the skill level is approximated as an integer (0-10)
    
    public Employee(String fname, String lname, String uname, String psw, String email, String phone,int access,Set<String> roles){
        super( fname,  lname,  uname,  psw,  email,  phone, access);
        this.roles=roles;
        this.skills =new TreeSet<RepairSkill>();
    }
    
    public Employee(){}
    
    public void changeSkill(String typename, int lvl){
        RepairSkill skill = new RepairSkill(typename,lvl);
        if(!skills.add(skill)){
            for(RepairSkill s:skills){
                if(s.getDevicetype().equals(typename)){
                    s.setLevel(lvl);
                    break;
                }
            }
        }
    }
    
    @XmlElement
    public Set<RepairSkill> getSkills(){
        return this.skills;
    }
}
