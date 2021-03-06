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
    private String description;
    private int status; //corresponds to: Cancelled(-1), Inwork(0), Repaired(1), Archived(2)
    private int id;
    private int priority; //low is more important: 0 top, 1 mid, 2 low priority
    private LocalDateTime creationtime;
    private LocalDateTime deadline;
    private Device device;
    private List<String> repairs; //Technicians got the short end of the stick due to time constraints, as tracking of repairs did not make it into the final product.
    private ReviewShell review=null;
    
    
    public Assignment(String title,String description,Device item, String deadline,String customer,String clerk,String technician,int priority){
        this.title=title;
        this.description=description;
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
    public String getDesc(){
        return this.description;
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
    /*
    As the full assignment never needs the entire reviewshell with comments, the getter digs a level deeper to return just the review.
    For each assignment, there can only be one review. But for every review there can be unlimited comments and comments of comments.
    There is no point to retrieving all of that when you are just concerned with the assignment itself.
    */
    @XmlElement
    public Review getReview(){
        if(this.review==null){
            return null;
        }
        return this.review.getReview();
    }
    
    public ReviewShell getReviewShell(){
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
        int hash = 1 + this.customer.hashCode() + this.title.hashCode()+ this.deadline.hashCode();
        return hash;
    }

    @Override
    public int compareTo(Assignment other) {
        int time =this.deadline.compareTo(other.deadline);
        if(time==0){
            return (this.hashCode() - other.hashCode());
        } else{
            return time;
        }
    }

    @Override
    public boolean equals(Object other) {
        if(this.hashCode() == other.hashCode()) {
            return true;
        }
        return false;
    }
}
