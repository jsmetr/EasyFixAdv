/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Jarno
 */
public class SessionGen {
    private SecureRandom random = new SecureRandom();
    
    private SessionGen(){
    }
    
    public static SessionGen getInstance(){
        return GenHolder.INSTANCE;
    }
    
    private static class GenHolder{
        private static final SessionGen INSTANCE = new SessionGen();
    }

    //generates a 26 character string randomly
    public String genSessionId() {
        return new BigInteger(130, random).toString(32);
    }
    
}
