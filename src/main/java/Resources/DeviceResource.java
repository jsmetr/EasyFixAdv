/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Resources;

import DataClasses.*;
import DataManagement.DeviceManager;
import DataManagement.LoginManager;
import java.util.List;
import java.util.Set;
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

    @Path("/Devicetypes")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Set<DeviceType> getTypes() {
        return DevMan.getTypes();
    }

    /*
    Stores a comment responding directly to a review.
     */
    @Path("/respondToComment/{body}/{reviewId}/{sessionId}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String respondToReview(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview().getReviewId() == rewId) {
                    shell = a.getReview();
                    Person u = LogMan.getBySesId(sessionId);
                    Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                    DevMan.testReview.commentOn(cmnt);
                    DevMan.save();
                    return "Comment found and responded to.";

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
    @Produces(MediaType.TEXT_PLAIN)
    public String respondToComment(@PathParam("sessionId") String sessionId, @PathParam("reviewId") String reviewId, @PathParam("commentId") String commentId, @PathParam("body") String body) {
        if (LogMan.CheckSession(sessionId)) {
            ReviewShell shell;
            int rewId = Integer.parseInt(reviewId);
            int cmntId = Integer.parseInt(commentId);
            Set<Assignment> assignments = DevMan.getAssignments();
            for (Assignment a : assignments) {
                if (a.getReview().getReviewId() == rewId) {
                    shell = a.getReview();
                    Person u = LogMan.getBySesId(sessionId);
                    Comment cmnt = new Comment(body, u.getFirstName() + " " + u.getLastName(), u.getUserName());
                    if (DevMan.testReview.respond(cmnt, Integer.parseInt(commentId)) == true) {
                        DevMan.save();
                        return "Comment found and responded to.";
                    }
                }
            }
            return "Comment not found.";
        }
        return "SESSION EXPIRED";
    }
}
