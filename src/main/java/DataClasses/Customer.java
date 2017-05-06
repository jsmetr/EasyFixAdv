/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Jarno
 */
public class Customer extends Person{
    /*
    Street address, city, state & zipcode are sufficient to mail bills to if customer does not pay on retrieving the item. 
    Phone & email are handled by Person class.
    */
    private String address;
    private String city;
    private String state;
    private String zipcode;
    
    public Customer(String fname, String lname, String uname, String psw, String email, String phone,String address,String city,String state,String zipcode){
        super( fname,  lname,  uname,  psw,  email,  phone, 0);
        this.roles=new HashSet<String>();
        this.roles.add("customer");
    }
    
    public Customer(){}
    
    @XmlElement
    public String getAddress(){
        return this.address;
    }
    
    @XmlElement
    public String getCity(){
        return this.city;
    }
    
    @XmlElement
    public String getState(){
        return this.state;
    }
    
    @XmlElement
    public String getZipcode(){
        return this.zipcode;
    }
    
    public void setAddress(String input){
        this.address=input;
    }
    
    public void setCity(String input){
        this.city=input;
    }
    
    public void setState(String input){
        this.state=input;
    }
    
    public void setZipcode(String input){
        this.zipcode=input;
    }
}
