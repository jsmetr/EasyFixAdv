/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jarno
 */
public class Customer extends Person{
    private String info; //placeholder until a proper format for billing information is designed. Probably a bundle of Strings?
    
    public Customer(String fname, String lname, String uname, String psw, String email, String phone){
        super( fname,  lname,  uname,  psw,  email,  phone, 0);
        this.roles=new HashSet<String>();
        this.roles.add("customer");
    }
    
    public Customer(){}
}
