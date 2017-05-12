/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import DataClasses.*;
import DataManagement.DeviceManager;
import DataManagement.LoginManager;
import DataManagement.UserManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
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
@Path("/Devices")
public class DeviceResource {

    LoginManager LogMan = LoginManager.getInstance();
    DeviceManager DevMan = DeviceManager.getInstance();
    UserManager UseMan = UserManager.getInstance();

    public String herp(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {

        }
        return "SESSION EXPIRED";
    }

    @Path("/Devicetypes")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<DeviceType> getTypes() {
        return DevMan.getTypes();
    }

    @Path("/AllDevices")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Device> getDevices() {
        return DevMan.getDevices();
    }

    @Path("/Assignments")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> getAssignment() {
        return DevMan.getAssignments();
    }

    @Path("/Assignments/Active/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> getActiveAssignment(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<Assignment> active = new TreeSet<Assignment>();
            for (Assignment a : all) {
                if (a.getStatus() == 0) {
                    active.add(a);
                }
            }
            return active;
        }
        return null;
    }

    @Path("/Assignments/Canceled/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> getCanceledAssignment(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<Assignment> active = new TreeSet<Assignment>();
            for (Assignment a : all) {
                if (a.getStatus() < 0) {
                    active.add(a);
                }
            }
            return active;
        }
        return null;
    }

    @Path("/Assignments/Repaired/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> getRepairedAssignment(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<Assignment> active = new TreeSet<Assignment>();
            for (Assignment a : all) {
                if (a.getStatus() == 1) {
                    active.add(a);
                }
            }
            return active;
        }
        return null;
    }

    @Path("/Assignments/Archived/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> getArchivedAssignment(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<Assignment> active = new TreeSet<Assignment>();
            for (Assignment a : all) {
                if (a.getStatus() == 2) {
                    active.add(a);
                }
            }
            return active;
        }
        return null;
    }

    /*
    Grabs all devices owned by a customer.
     */
    @Path("/{customer}/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Device> getCustDev(@PathParam("sessionId") String sessionId, @PathParam("customer") String customer) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Device> devices = new HashSet<Device>();
            for (Device d : DevMan.getDevices()) {
                if (d.getOwner().equals(customer)) {
                    devices.add(d);
                }
            }
            return devices;
        }
        return null;
    }

    @Path("/AddDevice/{type}/{name}/{owner}/{manufacturer}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addDevice(@PathParam("sessionId") String sessionId, @PathParam("name") String name, @PathParam("type") String type,
            @PathParam("owner") String owner, @PathParam("model") String model, @PathParam("manufacturer") String manufacturer) {
        if (LogMan.CheckSession(sessionId)) {
            if (LogMan.getBySesId(sessionId).getAccess() < 1) {
                return "ACCESS DENIED";
            }
            DeviceType devtype = DevMan.getTypeByName(type);
            Device device = new Device(owner, name, devtype, manufacturer);//String owner, String name, DeviceType type,String manufacturer,String model
            boolean added = DevMan.addDevice(device);
            if (added) {
                return "DEVICE ADDED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    @Path("/AddType/{name}/{data}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String addType(@PathParam("sessionId") String sessionId, @PathParam("name") String typename, @PathParam("data") String typedata) {
        if (LogMan.CheckSession(sessionId)) {
            if (LogMan.getBySesId(sessionId).getAccess() < 1) {
                return "ACCESS DENIED";
            }
            DeviceType newtype = new DeviceType(typename, typedata);
            boolean added = DevMan.addDeviceType(newtype);
            if (added) {
                return "DEVICE TYPE ADDED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    @Path("/CreateAssignment/{title}/{deviceid}/{deadline}/{prio}/{customer}/{technician}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String createAssignment(@PathParam("sessionId") String sessionId, @PathParam("deviceid") String deviceid, @PathParam("deadline") String deadline,
            @PathParam("prio") String priority, @PathParam("customer") String customer, @PathParam("technician") String technician, @PathParam("title") String title) {
        if (LogMan.CheckSession(sessionId)) {
            if (LogMan.getBySesId(sessionId).getAccess() < 1) {
                return "ACCESS DENIED";
            }
            if (technician.equals("noqualified")) {
                return "NO QUALIFIED STAFF";
            }
            String clerk = LogMan.getBySesId(sessionId).getUserName();
            Device item = DevMan.getDeviceById(Integer.parseInt(deviceid));
            if (technician.equals("automate")) {
                UserResource temp = new UserResource();
                Set<Employee> techs = temp.getTechByType(sessionId, item.getType().getName());
                technician = pickTech(item.getType().getName(), techs);
            }
            Assignment created = new Assignment(title, item, deadline + "T15:00", customer, clerk, technician, Integer.parseInt(priority));
            boolean added = DevMan.addAssignment(created);
            if (added) {
                return "ASSIGNMENT CREATED";
            }
            return "FAILURE";
        }
        return "SESSION EXPIRED";
    }

    /*
    Retrieves all of your (technician) open assignments in order of earliest deadline first.
     */
    @Path("/MyAssignments/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> myAssignments(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Person me = LogMan.getBySesId(sessionId);
            Set<Assignment> forMe = new TreeSet<Assignment>();
            for (Assignment a : DevMan.getAssignments()) {
                if (a.getTechnician().equals(me.getUserName()) && a.getStatus() == 0) {
                    forMe.add(a);
                }
            }
            return forMe;
        }
        return null;
    }

    @Path("/Assignment/{assignmentid}/{newstatus}/{sessionId}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String updateStatus(@PathParam("sessionId") String sessionId, @PathParam("assignmentid") String id, @PathParam("newstatus") String status) {
        if (LogMan.CheckSession(sessionId)) {
            int getid = Integer.parseInt(id);
            int setstatus = Integer.parseInt(status);
            DevMan.getAssignmentById(getid).setStatus(setstatus);
            DevMan.save();
            return "STATUS UPDATED: " + setstatus;
        }
        return "SESSION EXPIRED";
    }

    /*
    Retrieves all of your (technician) open assignments in order of priority, earliest deadline first within tier.
     */
    @Path("/MyAssignments/Priority/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Assignment> myAssignmentsByPriority(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Person me = LogMan.getBySesId(sessionId);
            ArrayList<Set<Assignment>> holder = new ArrayList<Set<Assignment>>();
            holder.add(new TreeSet<Assignment>());
            holder.add(new TreeSet<Assignment>());
            holder.add(new TreeSet<Assignment>());
            Set<Assignment> forMe = new TreeSet<Assignment>();
            for (Assignment a : DevMan.getAssignments()) {
                if (a.getTechnician().equals(me.getUserName()) && a.getStatus() == 0) {
                    holder.get(a.getPriority()).add(a);
                }
            }
            for (Set<Assignment> set : holder) {
                for (Assignment a : set) {
                    forMe.add(a);
                }
            }
            return forMe;
        }
        return null;
    }

    /*
    Stores a comment responding directly to a review.
     */
    @Path("/postReview/{assignid}/{title}/{body}/{rating}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String postReview(@PathParam("sessionId") String sessionId, @PathParam("assignid") String assignid, @PathParam("title") String title,
            @PathParam("rating") String rt, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            Person u = LogMan.getBySesId(sessionId);
            ReviewShell shell;
            int rating = Integer.parseInt(rt);
            int id = Integer.parseInt(assignid);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getId() == id) {
                    if (u.getUserName() != a.getCustomer()) {
                        return "You are not the customer of this assignment.";
                    }
                    String signed = LogMan.getBySesId(sessionId).getFirstName() + "" + LogMan.getBySesId(sessionId).getLastName();
                    Review newreview = new Review(title, rating, body, LogMan.getBySesId(sessionId).getUserName(), signed);
                    ReviewShell newshell = new ReviewShell(newreview);
                    a.addReview(newshell);
                    DevMan.save();
                    return "Assignment found and review added.";

                }
            }
            return "Assignment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Stores a comment responding directly to a review.
     */
    @Path("/respondToReview/{body}/{reviewId}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String respondToReview(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview() != null) {
                    if (a.getReviewShell().getReviewId() == rewId) {
                        shell = a.getReviewShell();
                        Person u = LogMan.getBySesId(sessionId);
                        Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                        shell.commentOn(cmnt);
                        DevMan.save();
                        return "Comment found and responded to.";
                    }
                }
            }
            return "Comment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Stores a comment responding to another existing comment.
     */
    @Path("/respondToComment/{body}/{reviewId}/{commentId}/{sessionId}")
    @POST
    @Produces(MediaType.APPLICATION_XML)
    public String respondToComment(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId,
            @PathParam("commentId") String commentId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            int cmntId = Integer.parseInt(commentId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview() != null) {
                    if (a.getReviewShell().getReviewId() == rewId) {
                        shell = a.getReviewShell();
                        Person u = LogMan.getBySesId(sessionId);
                        Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                        if (shell.respond(cmnt, cmntId) == true) {
                            DevMan.save();
                            return "Comment found and responded to.";
                        }
                    }
                }
            }
            return "Comment not found.";
        }
        return "SESSION EXPIRED";
    }

    /*
    Returns a desired amount of recent reviews for any type of device.
    Not controlled by sessionId since this is supposed to be open to all browsers.
     */
    @Path("/getReviews/Latest/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<Review> getLatest(@PathParam("amount") String amnt) {
        Set<Review> reviews = new TreeSet<Review>();
        int amount = Integer.parseInt(amnt);
        int count = 0;
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getReview() != null) {
                reviews.add(a.getReview());
                count++;
                if (!(count <= amount)) {
                    return reviews;
                }
            }
        }
        return reviews; //even if there are not enough to fill the quota, whatever was found is returned.
    }

    /*
    Returns a desired amount of recent reviews for the selected type of device.
    Not controlled by sessionId since this is supposed to be open to all browsers.
     */
    @Path("/getReviews/ByType/{type}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Review> getByType(@PathParam("type") String type) {
        List<Review> reviews = new ArrayList<Review>();
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getReview() != null && a.getDevice().getType().getName().equals(type)) {
                reviews.add(a.getReview());
            }
        }
        return reviews; //even if there are not enough to fill the quota, whatever was found is returned.
    }

    @Path("/getFullReview/{id}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public ReviewShell getFull(@PathParam("id") String id) {
        int revid = Integer.parseInt(id);
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getReview() != null && a.getReviewShell().getReviewId() == revid) {
                return a.getReviewShell();
            }
        }
        return null;
    }

    @Path("/Graph/Assignments/All/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<WorkLoad> graphActive(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<WorkLoad> active = new TreeSet<WorkLoad>();
            boolean addnew = true;
            for (Assignment a : all) {
                addnew = true;
                for (WorkLoad wl : active) {
                    if (wl.getName().equals(a.getDevice().getType().getName())) {
                        wl.countUp();
                        addnew = false;
                    }
                }
                if (addnew) {
                    WorkLoad newload = new WorkLoad(a.getDevice().getType().getName());
                    newload.countUp();
                    active.add(newload);
                }
            }
            return active;
        }
        return null;
    }

    @Path("/Graph/Assignments/Canceled/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<WorkLoad> graphCanceled(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Assignment> all = DevMan.getAssignments();
            Set<WorkLoad> active = new TreeSet<WorkLoad>();
            boolean addnew = true;
            for (Assignment a : all) {
                if (a.getStatus() == -1) {
                    addnew = true;
                    for (WorkLoad wl : active) {
                        if (wl.getName().equals(a.getDevice().getType().getName())) {
                            wl.countUp();
                            addnew = false;
                        }
                    }
                    if (addnew) {
                        WorkLoad newload = new WorkLoad(a.getDevice().getType().getName());
                        newload.countUp();
                        active.add(newload);
                    }
                }
            }
            return active;
        }
        return null;
    }

    @Path("/Graph/Skills/{sessionId}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<WorkLoad> graphSkills(@PathParam("sessionId") String sessionId) {
        if (LogMan.CheckSession(sessionId)) {
            Set<Employee> all = UseMan.getEmployees();
            Set<DeviceType> types = DevMan.getTypes();
            List<WorkLoad> skills = new ArrayList<WorkLoad>();
            for(DeviceType t:types){
                WorkLoad newload = new WorkLoad(t.getName());
                skills.add(newload);
            }
            for (Employee t : all) {
                for (WorkLoad wl : skills) {
                    for(RepairSkill rs: t.getSkills()){
                        if(rs.getDevicetype().equals(wl.getName())){
                            wl.countUp();
                        }
                    }
                }
            }
            return skills;
        }
        return null;
    }

    public String pickTech(String type, Set<Employee> techs) {
        ArrayList<WorkLoad> wlset = new ArrayList<WorkLoad>();
        for (Employee t : techs) {
            WorkLoad wl = new WorkLoad(t.getUserName());
            wlset.add(wl);
        }
        for (Assignment a : DevMan.getAssignments()) {
            if (a.getStatus() == 0) {
                for (WorkLoad load : wlset) {
                    if (load.getName().equals(a.getTechnician())) {
                        load.countUp();
                    }
                }
            }
        }
        Collections.sort(wlset);
        return wlset.get(0).getName();
    }
}
