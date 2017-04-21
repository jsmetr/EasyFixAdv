/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Jarno
 */
public class Comment implements Serializable{
    private String body;
    private String creator;
    private String signed;
    private LocalDateTime timestamp;
    
    public Comment(String body,String creator,String signed){
        this.body=body;
        this.timestamp=LocalDateTime.now();
    }
}
