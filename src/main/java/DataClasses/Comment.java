/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
//@Entity //for hibernate, later. Now focus on XML side
public class Comment implements Serializable{
    private String body;
    private String creator;
    private String signed;
    private LocalDateTime timestamp;
    private int id; //used to search for the comment/review on back end when responding to it.
    protected LinkedList<Comment> comments = new LinkedList<Comment>();
    
    public Comment(String body,String creator,String signed){
        this.body=body;
        this.timestamp=LocalDateTime.now();
        this.id=idGen();
    }
    
    public Comment(){
    }
    
    //generates an unique id for comments & reviews for searching purposes.
    private int idGen(){
        return 0;
    }
    
    public void respond(Comment response){
        comments.add(response);
    }
        
    @XmlElement
    public String getBody(){
        return this.body;
    }
        
    @XmlElement
    public String getCreator(){
        return this.creator;
    }
        
    @XmlElement
    public String getSigned(){
        return this.signed;
    }
        
    @XmlElement
    public String getTimestamp(){
        return this.timestamp.toString();
    }
        
    @XmlElement
    public List<Comment> getComments(){
        return this.comments;
    }
    
    @XmlElement
    public int getId(){
        return this.id;
    }
}
