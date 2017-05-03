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

    //currently a testing version
    @Path("/AddEmpl/{fname}/{lname}/{uname}/{psw}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addEmployee(@PathParam("sessionId") String sessionId, @PathParam("fname") String fname, @PathParam("lname") String lname, @PathParam("uname") String uname, @PathParam("psw") String psw) {
        LogMan.UpdateLogins();;
        if (true) {//LogMan.CheckSession(sessionId)){
            Set<Person> users = UseMan.getUsers();
            for (Person u : users) {
                if (u.getUserName().equals(uname)) {
                    return "USERNAME ALREADY IN USE";
                }
            }
            HashSet<String> roles = new HashSet<String>();
            roles.add("clerk");
            Employee newemp = new Employee(fname, lname, uname, psw, "email", "phone", 1, roles); //In order: first name, last name, user name, password, access lvl, jobs
            UseMan.addEmployee(newemp);
            return "SUCCESS";
        }
        return "SESSION EXPIRED";
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
    Call this after successful login to determine which page to take the user to.
    */
    @Path("/MyRole/{sessionId}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRole(@PathParam("sessionId") String sessionId) {
        String role = "customer";
        Person I = LogMan.getBySesId(sessionId);
        if(I.getRoles().contains("manager")){
            role="manager";
        } else if(I.getRoles().contains("technician")){
            role="technician";
        } else if(I.getRoles().contains("clerk")){
            role="clerk";
        }
        return role;
    }
}
