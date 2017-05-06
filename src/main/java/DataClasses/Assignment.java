/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
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
    private Device device;
    private List<String> repairs; //All repair tasks 
    private ReviewShell review;
    
    
    public Assignment(Device item, String deadline,String customer,String clerk,String technician){
        this.clerk=clerk;
        this.technician=technician;
        this.customer=customer;
        this.device=item;
        this.status=0;
        this.deadline=LocalDateTime.parse(deadline);
        this.creationtime=LocalDateTime.now();
        this.repairs=new ArrayList<String>();
    }
    
    @XmlElement
    public String getCustomer(){
        return this.customer;
    }
    
    @XmlElement
    public String getClerk(){
        return this.clerk;
    }
    
    @XmlElement
    public String getTechnician(){
        return this.technician;
    }
    
    @XmlElement
    public int getStatus(){
        return this.status;
    }
    
    @XmlElement
    public String getCreationtime(){
        return this.creationtime.toString();
    }
    
    @XmlElement
    public String getDeadline(){
        return this.deadline.toString();
    }
    
    @XmlElement
    public Device getDevice(){
        return this.device;
    }
    
    public List<String> getRepairs(){
        return this.repairs;
    }
    
    @XmlElement
    public ReviewShell getReview(){
        return this.review;
    }
    
    public void addReview(ReviewShell review){
        this.review=review;
    }
    
    public void addRepair(String repair){
        this.repairs.add(repair);
    }
    
    public void setStatus(int status){
        this.status=status;
    }
    
    public void setTechnician(String uname){
        this.technician=uname;
    }
    
    public void setDeadline(String time){
        this.deadline= LocalDateTime.parse(time);
    }
}
