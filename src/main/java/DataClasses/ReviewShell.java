/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */

@XmlRootElement
public class ReviewShell {
    private Review review;
    private LinkedList<Comment> comments;
    
    public ReviewShell(Review review){
        this.review=review;
        this.comments = new LinkedList<Comment>();
    }
    
    public Review getReview(){
        return this.review;
    }
    
    public List<Comment> getComments(){
        return this.comments;
    }
    
    public void setReview(Review review){
        this.review = review;
    }
    
    public void setComments(LinkedList<Comment> comments){
        this.comments=comments;
    }
}
