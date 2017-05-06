/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import DataManagement.*;
import DataClasses.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Jarno
 */
@Path("/Users")
public class UserResource {

    LoginManager LogMan = LoginManager.getInstance();
    UserManager UseMan = UserManager.getInstance();

    @Context
    private UriInfo context;

    /*
    Adds a new employee into the system.
     */
    @Path("/AddEmpl/{fname}/{lname}/{uname}/{psw}/{email}/{phone}/{access}/{roles}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addEmployee(@PathParam("sessionId") String sessionId, @PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("uname") String uname, @PathParam("psw") String psw, @PathParam("phone") String phone, @PathParam("email") String email, @PathParam("access") String access, @PathParam("roles") String roleStr) {
        if (LogMan.CheckSession(sessionId)) {
            HashSet<String> roles = new HashSet<String>(Arrays.asList(roleStr.split("&")));
            Employee newemp = new Employee(fname, lname, uname, psw, email, phone, Integer.parseInt(access), roles); //In order: first name, last name, user name, password, access lvl, jobs
            if (UseMan.addEmployee(newemp)) {
                LogMan.UpdateLogins();;
                return "SUCCESS";
            }
            return "USERNAME ALREADY IN USE";
        }
        return "SESSION EXPIRED";
    }

    /*
    Adds a new employee into the system.
     */
    @Path("/AddCust/{fname}/{lname}/{uname}/{psw}/{email}/{phone}/{access}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addCustomer(@PathParam("sessionId") String sessionId, @PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("uname") String uname, @PathParam("psw") String psw, @PathParam("phone") String phone, @PathParam("email") String email) {
        if (LogMan.CheckSession(sessionId)) {
            Customer newcust = new Customer(fname, lname, uname, psw, email, phone);
            if (UseMan.addCustomer(newcust)) {
                LogMan.UpdateLogins();;
                return "SUCCESS";
            }
            return "USERNAME ALREADY IN USE";
        }
        return "SESSION EXPIRED";
    }

    /* Used to retrieve the data of an user with their username. */
    @Path("/View/{uname}/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Person viewInfo(@PathParam("sessionId") String sessionId, @PathParam("uname") String uname) {
        if (LogMan.CheckSession(sessionId)) {
            Person u = UseMan.findUser(uname);
            if (u.getRoles().contains("customer")) {
                Customer cust = (Customer) u;
                return cust;
            } else if (!u.getRoles().isEmpty()) {
                Employee empl = (Employee) u;
                return empl;
            }
            return u;
        }
        return null;
    }

    /* Uses the Login Manager to retrieve the data for the user logged in. */
    @Path("/View/Myself/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Person viewMyInfo(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Person crnt = LogMan.getBySesId(sessionId);
            return crnt;
        }
        return null;
    }

    /* 
    A successful login returns a non-null 32 char sesId String, a failure returns 'FAILURE'.
    Store the session id so that it persists when moving between pages, since you need it to sign all your REST calls.
     */
    @Path("/Login/{uname}/{psw}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@PathParam("uname") String uname, @PathParam("psw") String psw) {
        LogMan.UpdateLogins();
        String sesId = LogMan.Login(uname, psw);
        return sesId;
    }

    /* Removes the submitted sessionId from the maps tracking valid sessionIds. */
    @Path("/Logout/{sessionId}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(@PathParam("sessionId") String sessionId) {
        return LogMan.Logout(sessionId);
    }

    /* 
    Call this service whenever moving between pages to determine that the user still has a valid session
    If this returns 'FALSE', kick user back to login screen instead.
     */
    @Path("/Check/{sessionId}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String checkIn(@PathParam("sessionId") String sessionId) {
        LogMan.UpdateLogins();
        if (LogMan.CheckSession(sessionId)) {
            return "TRUE";
        }
        return "FALSE";
    }

    /*
    Used to change your own password, the only personal variable that requires confirming with current password.
     */
    @Path("/ChangeMyPsw/{newpsw}/{oldpsw}/{sessionId}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String ceckIn(@PathParam("sessionId") String sessionId, @PathParam("newpsw") String newpsw, @PathParam("oldpsw") String oldpsw) {
        Person crnt = LogMan.getBySesId(sessionId);
        if (LogMan.CheckSession(sessionId)) {
            if (crnt.getPassword().equals(oldpsw)) {
                crnt.setPassword(newpsw);
                UseMan.saveMyself();
                return "PASSWORD CHANGED";
            }
            return "CURRENT PASSWORD MISMATCH";
        }
        return "SESSION EXPIRED";
    }

    /*
    Call this after successful login to determine which page to take the user to.
     */
    @Path("/MyRole/{sessionId}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRole(@PathParam("sessionId") String sessionId) {
        String role = "customer";
        Person I = LogMan.getBySesId(sessionId);
        if (I.getRoles().contains("manager")) {
            role = "manager";
        } else if (I.getRoles().contains("technician")) {
            role = "technician";
        } else if (I.getRoles().contains("clerk")) {
            role = "clerk";
        }
        return role;
    }
}
