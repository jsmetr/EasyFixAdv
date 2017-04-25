/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Jarno
 */
public class Employee extends Person{
    private HashMap<DeviceType,Integer> skills; //Technician skills, you either have it or you don't and the skill level is approximated as an integer (0-10)
    
    public Employee(String fname, String lname, String uname, String psw, String email, String phone,int access,Set<String> roles){
        super( fname,  lname,  uname,  psw,  email,  phone, access);
        this.roles=roles;
    }
}
