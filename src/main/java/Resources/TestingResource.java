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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Testingb")
public class TestingResource {
    private UriInfo context;
    
    @Path("/Comments/{amount}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabComments(@PathParam("amount") int amount){
        List<Comment> comments = new LinkedList<Comment>();
        String junk ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk,"Junkmaker","username");
        int i=0;
        while(i<amount){
            comments.add(cmnt);
        }
        return comments;
    }
    
    @Path("/Comments")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<Comment> grabsomeComments(){
        List<Comment> comments = new LinkedList<Comment>();
        String junk ="This text is filler. This text is filler. This text is filler. This text is filler. This text is filler. This text is filler.";
        Comment cmnt = new Comment(junk,"Junkmaker","username");
        int i=0;
        while(i<5){
            comments.add(cmnt);
        }
        return comments;
    }
    
    @Path("/Response")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String respond(){
        return "This is a response.";
    }
}
