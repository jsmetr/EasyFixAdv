/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Review extends Comment implements Serializable {

    private String revtitle; //front-end assigns a default title based off assigment name?
    private int rating;

    public Review(String title, int rating, String body, String creator, String signed) {
        super(body, creator, signed);
        this.revtitle = title;
        this.rating = rating; //probably 0-10 to account for half-stars without fractions?
        this.comments = null;
    }

    public Review() {
    }

    @Override
    public boolean respond(Comment response, int id) {
        //empty by design: reviews delegate their comments to their reviewshell to enable partial retrieveal: just review or review + comments
        return false;
    }

    @XmlElement
    public int getRating() {
        return this.rating;
    }

    public void setRating(int newrate) {
        this.rating = newrate;
    }

    @XmlElement
    public String getTitle() {
        return this.revtitle;
    }

    public void setTitle(String input) {
        this.revtitle = input;
    }
    
    @Override
    public int compareTo(Comment c) {
        Review other=(Review)c;
        int time=other.creationtime.compareTo(this.creationtime);
        if(time==0){
            return this.revtitle.hashCode()-other.revtitle.hashCode();
        }
        else{
        return other.creationtime.compareTo(this.creationtime);
        }
    }
}
