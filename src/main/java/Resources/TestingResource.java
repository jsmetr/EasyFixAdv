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
import DataManagement.DeviceManager;
import DataManagement.LoginManager;
import DataManagement.UserManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Testing")
public class TestingResource {
    /*
    Primarily used for two purposes: 
    earlier in the project to test that the data classes work as intended and later to fill the system with testing data and to reset that data as needed.
    */
    
    DeviceManager DevMan = DeviceManager.getInstance();
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
        if (DevMan.testReview == null) {
            String junk1 = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
            String junk2 = "This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
            String junk3 = "This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. ";
            String junk4 = "This is another comment on comment as well as filler. This is another comment on comment as well as filler. This is another comment on comment as well as filler.";
            String junk5 = "This is another direct response. This is another direct response. This is another direct response. ";
            Review rvw = new Review("Junkreview", 5, junk2, "ProReviewer", "username1");
            ReviewShell shell = new ReviewShell(rvw);
            Comment cmnt1 = new Comment(junk1, "PlainUser", "username2");
            Comment cmnt2 = new Comment(junk3, "ProReviewer", "username1");
            Comment cmnt3 = new Comment(junk4, "Junkmaker", "username3");
            Comment cmnt4 = new Comment(junk5, "Junkmaker", "username3");
            shell.commentOn(cmnt1);
            shell.commentOn(cmnt4);
            shell.respond(cmnt2, cmnt1.getId());
            shell.respond(cmnt3, cmnt1.getId());
            DevMan.testReview = shell;
        }
        return DevMan.testReview;
    }
    
    @Path("/removeComment/{commentId}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String removeComment(@PathParam("commentId") String commentId) {
        if (DevMan.testReview.removeComment(Integer.parseInt(commentId)) == true) {
            return "Comment found and removed.";
        }
        return "Comment not found.";
    }
    
    @Path("/respondToComment/{body}/{commentId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String respondToComment(@PathParam("commentId") String commentId, @PathParam("body") String body) {
        Comment cmnt = new Comment(body, "ProReviewer", "username1");
        if (DevMan.testReview.respond(cmnt, Integer.parseInt(commentId)) == true) {
            return "Comment found and responded to.";
        }
        return "Comment not found.";
    }
    
    @Path("/Reviews")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Review> grabReviews() {
        Set<Review> reviews = new TreeSet<Review>();
        String junk1 = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 = "This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 = "This is another filler review. This is another filler review. This is another filler review. This is another filler review.";
        String junk4 = "This is yet another filler review. This is yet another filler review. This is yet another filler review. This is yet another filler review. ";
        Review rvw2 = new Review("Junkreview 2", 5, junk2, "Junkmaker", "username3");
        Review rvw4 = new Review("Junkreview 4", 5, junk4, "PlainUser", "username2");
        Review rvw3 = new Review("Junkreview 3", 5, junk3, "NotAHacker", "username4");
        Review rvw1 = new Review("Junkreview 1", 5, junk1, "ProReviewer", "username1");
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
        String roles1 = ("clerk");
        Employee newemp1 = new Employee("Timmy", "Russell", "TimRuss", "drowssap", "mememail@parnanen.fi", "phone", 1, roles1);
        UseMan.addEmployee(newemp1);
        String roles12 = ("clerk");
        Employee newemp12 = new Employee("Mike", "Jobbs", "MikeJob", "fiesta", "mike@parnanen.fi", "555 5555 555", 1, roles12);
        UseMan.addEmployee(newemp12);
        String roles13 = ("clerk");
        Employee newemp13 = new Employee("Eddie", "Slate", "EddSlat", "simplistic", "eddie@parnanen.fi", "555 5555 555", 1, roles13);
        UseMan.addEmployee(newemp13);
        String roles2 = ("technician");
        Employee newemp2 = new Employee("Johnny", "Doe", "JohnDoe", "swordfish", "daDoe@parnanen.fi", "phone", 1, roles2);
        UseMan.addEmployee(newemp2);
        newemp2.changeSkill("television", 2);
        newemp2.changeSkill("toaster", 9);
        newemp2.changeSkill("iphone", 5);
        newemp2.changeSkill("lawnmower", 1);
        String roles22 = ("technician");
        Employee newemp22 = new Employee("Jim", "Masters", "JimMast", "putzle", "jmasters@parnanen.fi", "555 5555 555", 1, roles22);
        UseMan.addEmployee(newemp22);
        newemp22.changeSkill("lawnmower", 4);
        newemp22.changeSkill("iphone", 8);
        newemp22.changeSkill("electric stove", 3);
        String roles23 = ("technician");
        Employee newemp23 = new Employee("Wenhao", "Liang", "WeLiang", "security", "wliang@parnanen.fi", "555 5555 555", 1, roles23);
        UseMan.addEmployee(newemp23);
        newemp23.changeSkill("iphone", 5);
        newemp23.changeSkill("electric stove", 7);
        newemp23.changeSkill("toaster", 6);
        String roles3 = ("manager");
        Employee newemp3 = new Employee("Bob", "Stein", "BobStei", "greenisgood", "Bobs@parnanen.fi", "phone", 2, roles3);
        UseMan.addEmployee(newemp3);
        String roles4 = ("manager");
        Employee newemp4 = new Employee("Jack", "Quick", "JackQui", "swift", "JQuick@parnanen.fi", "phone", 2, roles4);
        UseMan.addEmployee(newemp4);
        //add customers,
        Customer cust1 = new Customer("Jill", "Doe", "JillDoe", "secure", "email@snail.com", "345 6523", "Distant Street 6", "Closesby", "Of Emergency", "00700");        
        Customer cust2 = new Customer("Kirk", "Ridge", "KirkRid", "ironclad", "RidgeRacer@maille.com", "125 7442", "5th Avenue", "city", "Of Nation", "04040");        
        Customer cust3 = new Customer("Gary", "Stu", "GaryStu", "unsinkable", "Stu@warudo.com", "335 7635", "Nearsby Street 5", "Faraway", "Your Business", "00500");        
        Customer cust4 = new Customer("Monica", "Santos", "MonicaS", "dependable", "viva@mailure.com", "998 8899", "Dim Street 4", "Shady Sands", "of Being", "04030");        
        UseMan.addCustomer(cust1);
        UseMan.addCustomer(cust2);
        UseMan.addCustomer(cust3);
        UseMan.addCustomer(cust4);
        stockAssignments();
        return "populated";
    }
    
    @Path("/stock")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String stockAssignments() {
        DeviceType iphone = new DeviceType("iphone", "Signals status.");
        DeviceType stove = new DeviceType("electric stove", "Cooks food.");
        DeviceType mower = new DeviceType("lawnmower", "Mowns the lawn");
        DeviceType tv = new DeviceType("television", "Old School catode tube telly.");
        DeviceType toaster = new DeviceType("toaster", "A toast for toast.");
        DevMan.addDeviceType(iphone);
        DevMan.addDeviceType(mower);
        DevMan.addDeviceType(tv);
        DevMan.addDeviceType(stove);
        DevMan.addDeviceType(toaster);
        Device estove = new Device("JillDoe", "Electrolux", stove, "manufacturer");
        Device iphone9k = new Device("GaryStu", "iPhone 1t1n", iphone, "Apple");
        Device Hondamower = new Device("JillDoe", "ClearCutter X", mower, "Honda");
        Device btoaster = new Device("KirkRid", "SchorchMaster", toaster, "unknown");
        Device iphoned = new Device("JillDoe", "iPhone L3", iphone, "Apple");
        Device ttv = new Device("MonicaS", "WideVisio 1720", tv, "unknown");
        DevMan.addDevice(estove);
        DevMan.addDevice(iphone9k);
        DevMan.addDevice(Hondamower);
        DevMan.addDevice(btoaster);
        DevMan.addDevice(iphoned);
        DevMan.addDevice(ttv);        
        Assignment a1 = new Assignment("Stove does not heat up","dud-desc", estove, "2017-09-13T10:15", "JillDoe", "MikeJob", "WeLiang", 1);        
        Assignment a2 = new Assignment("Broken Screen","dud-desc", iphone9k, "2017-11-07T10:15", "GaryStu", "TimRuss", "WeLiang", 0);
        Assignment a3 = new Assignment("Snapped mower blade","dud-desc", Hondamower, "2017-06-01T10:15", "JillDoe", "TimRuss", "JimMast", 0);
        Assignment a4 = new Assignment("Smokes when used","dud-desc", btoaster, "2017-06-05T10:15", "KirkRid", "MikeJob", "JohnDoe", 2);
        Assignment a5 = new Assignment("Virus infection","dud-desc", iphoned, "2017-07-03T10:15", "JillDoe", "EddSlat", "JohnDoe", 1);
        Assignment a6 = new Assignment("Bad signal","dud-desc", ttv, "2017-06-24T10:15", "MonicaS", "TimRuss", "JohnDoe", 1);        
        Assignment a7 = new Assignment("Makes odd noise","dud-desc", estove, "2017-09-13T10:15", "JillDoe", "MikeJob", "WeLiang", 1);        
        Assignment a8 = new Assignment("Bad reception","dud-desc", iphone9k, "2017-11-07T10:15", "GaryStu", "TimRuss", "WeLiang", 0);
        Assignment a9 = new Assignment("Does not start","dud-desc", Hondamower, "2017-06-01T10:15", "JillDoe", "EddSlat", "JimMast", 0);
        Assignment a10 = new Assignment("Springs do not work","dud-desc", btoaster, "2017-06-05T10:15", "KirkRid", "MikeJob", "WeLiang", 2);
        Assignment a11 = new Assignment("Ransomware removal","dud-desc", iphoned, "2017-07-03T10:15", "JillDoe", "EddSlat", "JohnDoe", 1);
        Assignment a12 = new Assignment("Sound bugs out randomly","dud-desc", ttv, "2017-06-24T10:15", "MonicaS", "MikeJob", "JimMast", 1);
        Assignment a13 = new Assignment("Fell into water","dud-desc", iphoned, "2017-08-03T10:15", "JillDoe", "MikeJob", "JohnDoe", 1);
        Assignment a14 = new Assignment("Buttons jammed","dud-desc", iphoned, "2017-07-09T10:15", "JillDoe", "EddSlat", "WeLiang", 1);
        Assignment a15 = new Assignment("Oozing strange fluid","dud-desc", iphoned, "2017-07-23T10:15", "JillDoe", "TimRuss", "JohnDoe", 1);
        DevMan.addAssignment(a1);
        DevMan.addAssignment(a2);
        DevMan.addAssignment(a3);
        DevMan.addAssignment(a4);
        DevMan.addAssignment(a5);
        DevMan.addAssignment(a6);
        DevMan.addAssignment(a7);
        DevMan.addAssignment(a8);
        DevMan.addAssignment(a9);
        DevMan.addAssignment(a10);
        DevMan.addAssignment(a11);
        DevMan.addAssignment(a12);
        DevMan.addAssignment(a13);
        DevMan.addAssignment(a14);
        DevMan.addAssignment(a15);
        String hashed = " " + a1.hashCode() + " " + a2.hashCode() + " " + a3.hashCode() + " " + a4.hashCode() + " " + a5.hashCode() + "" + a6.hashCode();
        a1.setStatus(1);
        a6.setStatus(1);
        a2.setStatus(1);
        a3.setStatus(1);
        a12.setStatus(-1);
        a9.setStatus(-1);
        a10.setStatus(-1);
        a13.setStatus(-1);
        a14.setStatus(1);
        a15.setStatus(1);
        createReview(3,a1,"#1");
        createReview(4,a6,"#2");
        createReview(2,a2,"#3");
        createReview(5,a3,"#4");
        createReview(5,a13,"#5");
        createReview(3,a14,"#6");
        createReview(4,a15,"#7");
        DevMan.save();
        return "stocked: " + DevMan.getTypes().size() + " devicetypes, " + DevMan.getDevices().size() + " devices, " + DevMan.getAssignments().size() + " assignments, hashes: " + hashed;
    }
    
    @Path("/GrabUsers")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Person> grabUsers() {
        Set<Person> users = UseMan.getUsers();
        return users;
    }
    
    @Path("/GrabEmployees")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Employee> grabEmployees() {
        Set<Employee> emps = UseMan.getEmployees();
        return emps;
    }
    
    @Path("/GrabCustomers")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Customer> grabCustomers() {
        Set<Customer> customers = UseMan.getCustomers();
        return customers;
    }
    
    @Path("/Nuke")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String nuke() {
        UseMan.nullAndVoid();
        DevMan.nullAndVoid();
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
    
    public void createReview(int rating, Assignment a,String number) {
        Set<Customer> cust = UseMan.getCustomers();
        List<Customer> clist = new ArrayList<Customer>();
        clist.addAll(cust);
        Set<Person> users = UseMan.getUsers();
        List<Person> ulist = new ArrayList<Person>();
        clist.addAll(cust);
        ulist.addAll(users);
        Random rng = new Random();
        clist.get(rng.nextInt(clist.size())).getUserName();
        String junk1 = "This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        String junk2 = "This is a filler review. This is a filler review. This is a filler review. This is a filler review. This is a filler review.";
        String junk3 = "This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. This comment on comment is filler. ";
        String junk4 = "This is another comment on comment as well as filler. This is another comment on comment as well as filler. This is another comment on comment as well as filler.";
        String junk5 = "This is another direct response. This is another direct response. This is another direct response. ";
        Customer c = clist.get(rng.nextInt(clist.size()));
        Person p = ulist.get(rng.nextInt(ulist.size()));
        Review rvw = new Review("Demo Review "+number+" - "+a.getDevice().getType().getName(), rating, ""+number+" "+junk2, c.getFirstName() + " " + c.getLastName(), c.getUserName());
        ReviewShell shell = new ReviewShell(rvw);
        
        Comment cmnt1 = new Comment(junk1+" 1", p.getFirstName() + " " + p.getLastName(), p.getUserName());
        p = ulist.get(rng.nextInt(ulist.size()));
        Comment cmnt2 = new Comment(junk3+" 3", p.getFirstName() + " " + p.getLastName(), p.getUserName());
        p = ulist.get(rng.nextInt(ulist.size()));
        Comment cmnt3 = new Comment(junk4+" 4", p.getFirstName() + " " + p.getLastName(), p.getUserName());
        p = ulist.get(rng.nextInt(ulist.size()));
        Comment cmnt4 = new Comment(junk5+" 5", p.getFirstName() + " " + p.getLastName(), p.getUserName());
        shell.commentOn(cmnt1);
        shell.commentOn(cmnt4);
        shell.respond(cmnt2, cmnt1.getId());
        shell.respond(cmnt3, cmnt1.getId());
        a.addReview(shell);
    }
}
