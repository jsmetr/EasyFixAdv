/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jarno
 */
public class AssignmentTest {
    
    public AssignmentTest() {
    }

    /**
     * Test of getCustomer method, of class Assignment.
     */
    @Test
    public void testGetCustomer() {
        System.out.println("getCustomer");
        Assignment instance = new Assignment("title",new Device(),LocalDateTime.now().toString(),"customer","clerk","technician",1);
        String expResult = "customer";
        String result = instance.getCustomer();
        assertEquals(expResult, result);
    }

    /**
     * Test of getClerk method, of class Assignment.
     */
    @Test
    public void testGetClerk() {
        System.out.println("getClerk");
        Assignment instance = new Assignment("title",new Device(),LocalDateTime.now().toString(),"customer","clerk","technician",1);
        String expResult = "clerk";
        String result = instance.getClerk();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTechnician method, of class Assignment.
     */
    @Test
    public void testGetTechnician() {
        System.out.println("getTechnician");
        Assignment instance = new Assignment("title",new Device(),LocalDateTime.now().toString(),"customer","clerk","technician",1);
        String expResult = "technician";
        String result = instance.getTechnician();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class Assignment.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        Assignment instance = new Assignment("title",new Device(),LocalDateTime.now().toString(),"customer","clerk","technician",1);
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);;
    }

    /**
     * Test of getId method, of class Assignment.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Assignment instance = new Assignment("title",new Device(),LocalDateTime.now().toString(),"customer","clerk","technician",1);
        int expResult = instance.hashCode();
        int result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDeadline method, of class Assignment.
     */
    @Test
    public void testGetDeadline() {
        System.out.println("getDeadline");
        LocalDateTime dl = LocalDateTime.now();
        Assignment instance = new Assignment("title",new Device(),dl.toString(),"customer","clerk","technician",1);
        LocalDateTime expResult = dl;
        LocalDateTime result = LocalDateTime.parse(instance.getDeadline());
        assertEquals(expResult, result);
    }

}
