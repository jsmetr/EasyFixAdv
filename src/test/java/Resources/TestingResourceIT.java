/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import DataClasses.Comment;
import DataClasses.Customer;
import DataClasses.Employee;
import DataClasses.Person;
import DataClasses.Review;
import DataClasses.ReviewShell;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno
 */
public class TestingResourceIT {
    
    public TestingResourceIT() {
    }

    /**
     * Test of grabComments method, of class TestingResource.
     */
    @Test
    public void addComment() {
        System.out.println("Adding a comment.");
        TestingResource instance = new TestingResource();
        ReviewShell rev = instance.grabReview();
        Integer cmntid=rev.getComments().get(0).getId();
        assertEquals(2,rev.getComments().get(0).getComments().size());
        assertEquals(rev.getComments().get(0).getComments().get(0).getSigned(), "ProReviewer");
        String body="fillertext";
        instance.respondToComment(cmntid.toString(),body);
        assertEquals(3,rev.getComments().get(0).getComments().size());
    }

    /**
     * Test of grabReview method, of class TestingResource.
     */
    @Test
    public void removeComment() {
        System.out.println("Removing a comment.");
        TestingResource instance = new TestingResource();
        ReviewShell rev = instance.grabReview();
        assertEquals(rev.getComments().size(), 2);
        Integer cmntid=rev.getComments().get(0).getId();
        instance.removeComment(cmntid.toString());
        assertEquals(rev.getComments().size(), 1);
    }

    /**
     * Test of removeComment method, of class TestingResource.
     */
    @Test
    public void userAddition() {
        System.out.println("Testing user addition.");
        TestingResource instance = new TestingResource();
        instance.populateTestUsers();
        Set<Person> users = instance.grabUsers();
        assertEquals(users.size(), 4);
        boolean found=false;
        for(Person p:users){
            if(p.getFirstName().equals("Jack")){
                found=true;
            }
        }
        assertTrue(found);
    }

 
}
