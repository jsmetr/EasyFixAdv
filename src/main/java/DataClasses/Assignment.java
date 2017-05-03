/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author JTS
 */
public class Assignment {
    //Users involved in assignment tracked by username to avoid potential 'double storage' by the datamanagement classes.
    private String customer;
    private String technician;
    private String clerk;
    private LocalDateTime creationtime;
    private LocalDateTime deadline;
    private Device item;
    private List<String> tasks; //All repair tasks 
    
    
    public Assignment(Device item, String deadline){
        this.item=item;
        this.deadline=LocalDateTime.parse(deadline);
        this.creationtime=LocalDateTime.now();
    }
    
    
}
