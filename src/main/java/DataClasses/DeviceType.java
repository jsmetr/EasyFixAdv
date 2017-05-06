/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataClasses;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Jarno
 */

/*
The class is intented as a flexible way to store a bulk of data common to all devices of a given 'type'. 
The scope of type is left to the end user to define, for example 'smartphone' vs'iPhone' vs 'specific product line of iPhones'.
The handling of device types could easily be expanded to accomodate specific needs of the customer, 
but in the absence of such requirements we keep this simple.
*/
@XmlRootElement
public class DeviceType  implements Serializable{
    private String typename;
    private String typedata;
    
    public DeviceType(String name,String data){
        this.typename=name;
        this.typedata=data;
    }
    
    public DeviceType(){}
    
    @XmlElement
    public String getName(){
        return this.typename;
    }
    
    @XmlElement
    public String getData(){
        return this.typedata;
    }
    
    public void setName(String typename){
        this.typename=typename;
    }
    
    public void setData(String typedata){
        this.typedata=typedata;
    }
}
