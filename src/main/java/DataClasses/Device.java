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
public class Device implements Serializable, Comparable<Device>{

    private int deviceId;
    private String owner; //owner username
    private String name; //name of device
    private String manufacturer; //As the list of manufacturers is potentially endless for a 'Universal Repair Shop' like Pärnänen's, this is a write-in field.
    private DeviceType type;

    public Device(String owner, String name, DeviceType type, String manufacturer) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.manufacturer=manufacturer;
        this.deviceId = hashCode();
    }
    
    public Device(){};

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
    public DeviceType getType() {
        return this.type;
    }
    
    public void setId(String input){
        this.deviceId=hashCode();
    }
    
    public void setName(String input){
        this.name=input;
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

    @Override
    public int compareTo(Device other) {
        return this.deviceId-other.deviceId;
    }

    @Override
    public boolean equals(Object other) {
        if(this.hashCode() == other.hashCode()) {
            return true;
        }
        return false;
    }
}
