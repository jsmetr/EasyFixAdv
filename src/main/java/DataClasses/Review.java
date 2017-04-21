/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;

/**
 *
 * @author Jarno
 */
public class Review extends Comment implements Serializable{
    private int rating;
    
    public Review(int rating, String body,String creator,String signed){
        super(body,creator,signed);
        this.rating=rating;
        
    }
}
