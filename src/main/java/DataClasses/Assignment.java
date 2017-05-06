/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JTS
 */
@XmlRootElement
public class Assignment {
    //Users involved in assignment tracked by username to avoid potential 'double storage' by the datamanagement classes.
    private String customer;
    private String technician;
    private String clerk;
    private int status; //Cancelled(-1), Inwork(0), Repaired(1), Archived(2)
    private LocalDateTime creationtime;
    private LocalDateTime deadline;
    private Device item;
    private List<String> tasks; //All repair tasks 
    
    
    public Assignment(Device item, String deadline){
        this.item=item;
        this.deadline=LocalDateTime.parse(deadline);
        this.creationtime=LocalDateTime.now();
        this.tasks=new ArrayList<String>();
    }
    
    
}
