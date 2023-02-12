package model;

/**
 *  This class is inherited from Part and is specifically parts that are built in house.
 *
 */

public class InHousePart extends Part{
    private int MachineId;

    public InHousePart(int id, String name, double price, int stock, int min, int max, int machineid){
        super(id, name, price, stock, min, max);
        this.MachineId = machineid;
    }

    //Setters and Getters
    /**
     * @return the machine Id
     */
    public int getMachineId() {
        return MachineId;
    }
    /**
     * @param machineId Id of the Machine
     */
    public void setMachineId(int machineId) {
        MachineId = machineId;
    }
}