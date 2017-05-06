/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jarno
 */
@XmlRootElement
public class Device implements Serializable{

    private int deviceId;
    private String owner; //owner username
    private String name; //name of device
    private String manufacturer; //unknown is valid, and serves as the default
    private String model; //ditto
    private DeviceType type;

    public Device(String owner, String name, DeviceType type) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.deviceId = hashCode();
    }

    @XmlElement
    public int getId() {
        return this.deviceId;
    }

    @XmlElement
    public String getOwner() {
        return this.owner;
    }

    @XmlElement
    public String getName() {
        return this.name;
    }

    @XmlElement
    public String getManufacturer() {
        return this.manufacturer;
    }

    @XmlElement
    public String getModel() {
        return this.model;
    }

    @XmlElement
    public DeviceType getType() {
        return this.type;
    }
    
    public void setId(String input){
        this.deviceId=hashCode();
    }
    
    public void setName(String input){
        this.name=input;
    }
    
    public void setModel(String input){
        this.model=input;
    }
    
    public void setManufacturer(String input){
        this.manufacturer=input;
    }
    
    public void setType(DeviceType input){
        this.type=input;
    }

    @Override
    public int hashCode() {
        int hash = 1 + 13 * this.name.hashCode() + 7 * this.owner.hashCode();
        return hash;
    }
}
