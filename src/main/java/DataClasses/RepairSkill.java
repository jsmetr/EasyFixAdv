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
public class RepairSkill implements Serializable, Comparable<RepairSkill> {

    /*
    A workaround for the fact that XML does not support maps. So instead we use 'RepairSkill' as a container for devicetype String and skill level integer.
     */
    private int level;
    private String devicetype;

    RepairSkill(String type, int level) {
        this.level = level;
        this.devicetype = type.toLowerCase();
    }

    RepairSkill() {
    }

    /*
    Renaming a skill can be useful if the devicetype classification needs to be revised. 
    In that event, the skill lists of technician would need to be updated to the new scheme.
     */
    public void setDevicetype(String newtype) {
        this.devicetype = newtype.toLowerCase();
    }

    public void setLevel(int newlevel) {
        this.level = newlevel;
    }

    @XmlElement
    public String getDevicetype() {
        return this.devicetype;
    }

    @XmlElement
    public int getLevel() {
        return this.level;
    }

    /*
    A skill that has the same name is the same skill, even if the level is different.
    */
    @Override
    public int hashCode() {
        int hash = 1 + 13 * this.devicetype.hashCode();
        return hash;
    }

    @Override
    public int compareTo(RepairSkill other) {
        return this.hashCode() - other.hashCode();
    }
}
