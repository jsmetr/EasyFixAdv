/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.util.LinkedList;

/**
 *
 * @author Jarno
 */
public class ReviewShell {
    private Review review;
    private LinkedList<Comment> comments;
    
    public ReviewShell(Review review){
        this.review=review;
    }
    
}
