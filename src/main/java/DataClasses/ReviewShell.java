/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
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
    
    public ReviewShell(){}
    
    /*Comments that are direct responses to the review are added to the reviewshell comments list. 
      Each comment stores the responses to itself. */
    public void commentOn(Comment cmnt){
        comments.add(cmnt);
    }
    
    public boolean removeComment(int id){
        for(Comment com : comments){
            if(com.getId()==id){
                comments.remove(com);
                return true;
            }
            if(com.removeComment(id)==true){
                return true;
            }
        }
        return false;
    }
    
    //when retrieving review + comments, the id of the review must be accessible by the reviewshell so the correct shell can be identified.
    public int getReviewId(){
        return this.review.getId();
    }
    
    @XmlElement
    public Review getReview(){
        return this.review;
    }
    
    //When responding to a comment, reviewId is used to find the  right shell and comment id to to find the correct comment
    @XmlElement
    public List<Comment> getComments(){
        return this.comments;
    }
}
