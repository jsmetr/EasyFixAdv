/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataManagement;

import java.util.HashSet;
import DataClasses.*;
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
    private int typecount=0;
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
        saveMyself();
    }
    
    private void saveMyself() {
        try {
            Serializer.serialize(this, filename);
        } catch (Exception e) {
            System.out.println("Failed to save to " + filename);
        }
    }
    
    public void addDevice(Device newdevice){
        this.devices.add(newdevice);
        saveMyself();
    }
    
    public void addDeviceType(DeviceType newtype){
        this.devicetypes.add(newtype);
        saveMyself();
    }
    
    public Set<DeviceType> getTypes(){
        return this.devicetypes;
    }
}
