/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

/**
 *
 * @author Jarno
 */
import DataClasses.*;
import DataManagement.LoginManager;
import DataManagement.UserManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Testing")
public class TestingResource {

    LoginManager LogMan = LoginManager.getInstance();
    UserManager UseMan = UserManager.getInstance();
    //A minor addition to test local->GitLab->Github mirroring
    @Path("/Comments/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabComments(@PathParam("amount") int amount) {
        List<Comment> comments = new LinkedList<Comment>();
        String junk = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk, "Junkmaker", "username");
        int i = 0;
        while (i < amount) {
            comments.add(cmnt);
            i++;
        }
        return comments;
    }

    @Path("/Comments")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabsomeComments() {
        List<Comment> comments = new LinkedList<Comment>();
        String junk = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk, "Junkmaker", "username");
        int i = 0;
        while (i < 5) {
            comments.add(cmnt);
            i++;
        }
        return comments;
    }

    @Path("/Review")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public ReviewShell grabReview() {
        String junk1 = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 = "This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 = "This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. ";
        String junk4 = "This is another comment on comment as well as filler. This is another comment on comment as well as filler. This is another comment on comment as well as filler.";
        String junk5 = "This is another direct response. This is another direct response. This is another direct response. ";
        Review rvw = new Review(5, junk2, "ProReviewer", "username1");
        ReviewShell shell = new ReviewShell(rvw);
        Comment cmnt1 = new Comment(junk1, "PlainUser", "username2");
        Comment cmnt2 = new Comment(junk3, "ProReviewer", "username1");
        Comment cmnt3 = new Comment(junk4, "Junkmaker", "username3");
        Comment cmnt4 = new Comment(junk5, "Junkmaker", "username3");
        cmnt1.respond(cmnt2);
        cmnt1.respond(cmnt3);
        shell.commentOn(cmnt1);
        shell.commentOn(cmnt4);
        return shell;
    }

    @Path("/Reviews")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Review> grabReviews() {
        ArrayList<Review> reviews = new ArrayList<Review>();
        String junk1 = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 = "This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 = "This is another filler review. This is another filler review. This is another filler review. This is another filler review.";
        String junk4 = "This is yet another filler review. This is yet another filler review. This is yet another filler review. This is yet another filler review. ";
        Review rvw1 = new Review(5, junk1, "ProReviewer", "username1");
        Review rvw2 = new Review(5, junk2, "Junkmaker", "username3");
        Review rvw3 = new Review(5, junk3, "NotAHacker", "username4");
        Review rvw4 = new Review(5, junk4, "PlainUser", "username2");
        reviews.add(rvw1);
        reviews.add(rvw2);
        reviews.add(rvw3);
        reviews.add(rvw4);
        return reviews;
    }

    @Path("/populate")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String populateTestUsers() {
        HashSet<String> roles1 = new HashSet<String>();
        roles1.add("clerk");
        Employee newemp1 = new Employee("Timmy", "Russell", "TimRuss", "drowssap", "email", "phone", 1, roles1); 
        UseMan.addEmployee(newemp1);
        HashSet<String> roles2 = new HashSet<String>();
        roles2.add("technician");
        newemp1.changeSkill("iphone", 6);
        newemp1.changeSkill("television", 2);
        Employee newemp2 = new Employee("Johnny", "Doe", "JohnDoe", "swordfish", "email", "phone", 1, roles2); 
        UseMan.addEmployee(newemp2);
        HashSet<String> roles3 = new HashSet<String>();
        roles3.add("manager");
        newemp2.changeSkill("iphone", 2);
        newemp2.changeSkill("toaster", 9);
        Employee newemp3 = new Employee("Bob", "Stein", "BobStei", "greenisgood", "email", "phone", 2, roles3); 
        UseMan.addEmployee(newemp3);
        HashSet<String> roles4 = new HashSet<String>();
        roles4.add("clerk");
        roles4.add("manager");
        newemp3.changeSkill("lawnmover", 4);
        newemp3.changeSkill("iphone", 8);
        Employee newemp4 = new Employee("Jack", "Quick", "JackQui", "swift", "email", "phone", 2, roles4); 
        newemp4.changeSkill("iphone", 5);
        newemp4.changeSkill("electric stove", 7);
        UseMan.addEmployee(newemp4);
        return "populated";
    }
    
    @Path("/GrabUsers")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Person> grabUsers(){
        Set<Person> users = UseMan.getUsers();
        return users;
    }
    
    @Path("/GrabEmployees")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Employee> grabEmployees(){
        Set<Employee> emps =UseMan.getEmployees();
        return emps;
    }
    
    @Path("/GrabCustomers")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Customer> grabCustomers(){
        Set<Customer> customers = UseMan.getCustomers();
        return customers;
    }
    
    @Path("/NukeUsers")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String nukeUserBase(){
        UseMan.nullAndVoid();
        LogMan.UpdateLogins();
        return "BOOM";
    }

    @Path("/Response")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respond() {
        return "This is a response.";
    }

    @Path("/TickTock")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String checkingTheClock() {
        return LocalDateTime.now().toString();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaulting() {
        return "Swing and a miss.";
    }
}
