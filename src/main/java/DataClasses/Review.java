/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
//@Entity //for hibernate, later. Now focus on XML side
public class Review extends Comment implements Serializable{
    private int rating;
    
    public Review(int rating, String body,String creator,String signed){
        super(body,creator,signed);
        this.rating=rating;
        
    }
    
    public Review(){
    
    }
}
