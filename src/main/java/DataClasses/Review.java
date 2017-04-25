/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
public class Review extends Comment implements Serializable{
    private int rating;
    
    public Review(int rating, String body,String creator,String signed){
        super(body,creator,signed);
        this.rating=rating;
        this.comments=null;
    }
    
    public Review(){}
    
    @Override
    public void respond(Comment response){
        //empty by design: reviews delegate their comments to their reviewshell to enable partial retrieveal: just review or review + comments
    }
    
    @XmlElement
    public int getRating(){
        return this.rating;
    }
}
