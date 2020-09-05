package Model;

/** Sets the InHouse part parameters
 * Contains the Constructor for InHouse Parts
 * <p>
 * Contains the Getter for InHouse Parts. Also contains the Constructor for InHouse Parts.
 * <br>Constructor is made from, Name, ID, Price, Stock ( Inventory ), Min and Max values to dictate how many items can be entered.
 *
 * @author Sam Gonzales
 */

public class InHouse extends Part {

    private final int machineId;

    /**
     * Declare Constructor
     * @param name Sets the name for the Constructor for InHouse Parts
     * @param id Sets the id for the Constructor for InHouse Parts
     * @param price Sets the price for the Constructor for InHouse Parts
     * @param stock Sets the stock for the Constructor for InHouse Parts
     * @param min Sets the min for the Constructor for InHouse Parts
     * @param max Sets the max for the Constructor for InHouse Parts
     * @param machineId Sets the companyName for the Constructor for InHouse Parts
     */
    public InHouse(String name, int id, double price, int stock, int min, int max, int machineId) {
        super(name, id, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Declares the MachineID Getter
      * @return Returns the MachineID
     */
    public int getMachineId() {
        return machineId;
    }
}

