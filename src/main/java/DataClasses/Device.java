/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Device {
    private int deviceId;
    private String owner; //owner username
    private String name; //name of device
    private String manufacturer; //unknown is valid, and serves as the default
    private String model; //ditto
    private DeviceType type;
    
    public Device(String owner, String name, DeviceType type){
        this.owner=owner;
        this.name=name;
        this.type=type;
        this.deviceId=hashCode();
    }
    
    @Override
    public int hashCode(){
        int hash=1+13*this.name.hashCode()+7*this.owner.hashCode();
        return hash;
    }
}
