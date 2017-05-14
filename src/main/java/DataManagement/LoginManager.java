/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import DataClasses.*;

/**
 *
 * @author Jarno
 */
public class LoginManager {
    private Map<String,Person> logins = new HashMap<String,Person>(); //password - User
    private Map<String,Person> loggedIn = new HashMap<String,Person>(); //sessionId - User
    private Map<String,LocalDateTime> sessions = new HashMap<String,LocalDateTime>(); //sessionId - last action time
    private long autoLogoutLimit = 15; //in minutes
    private SessionGen sesGen = SessionGen.getInstance();
    private boolean taskpop=true;
    private boolean userpop=true;
    
    
    public static LoginManager getInstance(){
        return LoginHolder.INSTANCE;
    }
    
    private static class LoginHolder{
        private static final LoginManager INSTANCE = new LoginManager();
    }
    
    /* Compares the offered login to a list of valid logins. REST resources are responsible for updating the list as part of any activity that could modify it. */
    public String Login(String username, String password){
        Person logged = logins.get(password);
        if(logged!=null){
            if(logged.getUserName().equals(username)){  //implement user data comparison methods in Person, then uncomment
                return SessionSetup(logged);
            }
        }
        return "FAILURE"; //login failed
    }
    
    /* Removes a session from both maps it is stored in, assuming it is in them.*/
    public String Logout (String session){
        if(sessions.get(session)!=null){
            sessions.remove(session);
            loggedIn.remove(session);
            return "LOGGED OUT";
        }
        return "SESSION NOT FOUND";
    }
    
    /* Generates the sessionId for the session, making sure it is not already i use, 
    then adds the user and session start time into their respective maps keyed to the sessionId. */
    private String SessionSetup(Person user){
        boolean again = true;
        String sessionId="";
        while(again){
        sessionId = sesGen.genSessionId();
        if(sessions.get(sessionId) == null){
            sessions.put(sessionId, LocalDateTime.now());
            loggedIn.put(sessionId, user);
            again=false;
            }
        }
        return sessionId;
    }
    
    //implement access levels for users through Person class, then uncomment
    
    /* Files a list of users into the list of valid logins, user keyed to passwords.
    REST service handles omitting deactivated users from this list. */
    public void UpdateLogins(){
        UserManager UM = UserManager.getInstance();
        Set<Person> users = UM.getUsers();
        logins.clear();
        for(Person user : users){
            if(user.getAccess()>=0){
                logins.put(user.retrievePassword(), user);
            }
        }
    }
    
    /* Compares current time to latest stored sessiontime + autologout limit.
    If current time is under limit, session is renewed with current time becoming the latest stored session time.
    CheckSession(sessionId) is used in the REST resources as the first layer of if() to shield resources from unauthorized access. */
    public boolean CheckSession(String session){
        LocalDateTime check = LocalDateTime.now().minusMinutes(autoLogoutLimit);
        if(sessions.get(session)!=null){
            if((0<(sessions.get(session).compareTo(check)))){ //comparing the last check time to current time - autoLogoutLimit in minutes, results in false if over limit
                sessions.replace(session, LocalDateTime.now());
                return true;
            }
            Logout(session);
        }
        return false;
    }
    
    /* Returns the user object corresponding to the session that sent the request.
    This is used to check access level for manager-only requests.*/
    public Person getBySesId(String sesId){
        return loggedIn.get(sesId);
    }
}
