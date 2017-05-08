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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JTS
 */
@XmlRootElement
public class Assignment implements Serializable, Comparable<Assignment>{
    //Users involved in assignment tracked by username to avoid potential 'double storage' by the datamanagement classes.
    private String customer;
    private String technician;
    private String clerk;
    private String title;
    private int status; //Cancelled(-1), Inwork(0), Repaired(1), Archived(2)
    private int id;
    private int priority; //low is more important: 0 top, 1 mid, 2 low priority
    private LocalDateTime creationtime;
    private LocalDateTime deadline;
    private Device device;
    private List<String> repairs; //All repair tasks 
    private ReviewShell review=null;
    
    
    public Assignment(String title,Device item, String deadline,String customer,String clerk,String technician,int priority){
        this.title=title;
        this.clerk=clerk;
        this.technician=technician;
        this.customer=customer;
        this.device=item;
        this.status=0;
        this.priority=priority;
        this.deadline=LocalDateTime.parse(deadline);
        this.creationtime=LocalDateTime.now();
        this.repairs=new LinkedList<String>();
        this.id=hashCode();
    }
    
    public Assignment(){}
    
    @XmlElement
    public String getCustomer(){
        return this.customer;
    }
    
    @XmlElement
    public String getTitle(){
        return this.title;
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
    public int getPriority(){
        return this.priority;
    }
    
    @XmlElement
    public int getId(){
        return this.id;
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
    
    @XmlElement
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
    
    public void setTitle(String title){
        this.title=title;
    }
    
    public void setStatus(int status){
        this.status=status;
    }
    
    public void setPriority(int priority){
        this.priority=priority;
    }
    
    public void setTechnician(String uname){
        this.technician=uname;
    }
    
    public void setDeadline(String time){
        this.deadline= LocalDateTime.parse(time);
    }
    
    public int hashCode(){
        int hash = 1 + 13 * this.customer.hashCode() + 7 * this.title.hashCode();
        return hash;
    }

    @Override
    public int compareTo(Assignment other) {
        return this.hashCode() - other.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(this.hashCode() == other.hashCode()) {
            return true;
        }
        return false;
    }
}
