package Model;

/**
 *
 * @author Sam Gonzales
 */

public class Outsourced extends Part {
    /**
     * Declare company Name
     */
    private final String companyName;


    /**
     * Declare Constructor
     * @param name Sets the name for the Constructor for Outsourced Parts
     * @param id Sets the id for the Constructor for Outsourced Parts
     * @param price Sets the price for the Constructor for Outsourced Parts
     * @param stock Sets the stock for the Constructor for Outsourced Parts
     * @param min Sets the min for the Constructor for Outsourced Parts
     * @param max Sets the max for the Constructor for Outsourced Parts
     * @param companyName Sets the companyName for the Constructor for Outsourced Parts
     */
    public Outsourced(String name, int id, double price, int stock, int min, int max, String companyName) {
        super(name, id, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Creates a getting for Company Name
     * @return Returns the Company Name
     */
    public String getCompanyName() {
        return companyName;
    }
}
