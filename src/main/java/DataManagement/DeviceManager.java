/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import java.util.HashSet;
import DataClasses.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Jarno
 */
public class DeviceManager {

    private String filename = "DeviceManagerStorage"; //Must match UserHolder.getManager() deserialization filename.
    private HashSet<Assignment> assignments = new HashSet<Assignment>();
    private HashSet<Device> devices = new HashSet<Device>();
    private HashSet<DeviceType> devicetypes = new HashSet<DeviceType>();
    private LinkedList<Comment> modQueue;
    private int typecount = 0;
    public ReviewShell testReview; //there is no separate testmanager, so this is here

    public static DeviceManager getInstance() {
        return DeviceHolder.INSTANCE;
    }

    private static class DeviceHolder {

        private static final DeviceManager INSTANCE = getManager();

        private static DeviceManager getManager() {
            try {
                return (DeviceManager) Serializer.deserialize("DeviceManagerStorage");
            } catch (Exception e) {
                return new DeviceManager();
            }
        }
    }

    //resets the userbase
    public void nullAndVoid() {
        this.assignments.clear();
        this.devices.clear();
        this.devicetypes.clear();
        save();
    }

    public void save() {
        try {
            Serializer.serialize(this, filename);
        } catch (Exception e) {
            System.out.println("Failed to save to " + filename);
        }
    }

    public void addDevice(Device newdevice) {
        this.devices.add(newdevice);
        save();
    }

    public void addDeviceType(DeviceType newtype) {
        this.devicetypes.add(newtype);
        save();
    }

    public void addToModQueue(Comment cmnt) {
        this.modQueue.add(cmnt);
    }

    public boolean approveComment(int id) {
        for (Comment c : modQueue) {
            if (c.getId() == id) {
                modQueue.remove(c);
                return true;
            }
        }
        return false;
    }
    
    public List<Comment> getQueue(){
        return this.modQueue;
    }

    public Set<Assignment> getAssignments() {
        return this.assignments;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public Set<DeviceType> getTypes() {
        return this.devicetypes;
    }
}
