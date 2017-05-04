/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class DeviceType {
    private String typename;
    private String typedata;
    
    public DeviceType(){
        
    }
    
    @XmlElement
    public String getname(){
        return this.typename;
    }
    
    @XmlElement
    public String getdata(){
        return this.typedata;
    }
    
    public void setName(String typename){
        this.typename=typename;
    }
    
    public void setData(String typedata){
        this.typedata=typedata;
    }
}
