package Model;

/**
 *
 * @author Sam Gonzales
 */

public class Part{
    //Declare Fields
    private String name;
    private int id;
    private double price;
    private final int stock;
    private int min;
    private int max;

    /**
     * Declare Parts Constructor
     * @param name Sets the name for the Constructor for Parts
     * @param id Sets the id for the Constructor for Parts
     * @param price Sets the price for the Constructor for Parts
     * @param stock Sets the stock for the Constructor for Parts
     * @param min Sets the min for the Constructor for Parts
     * @param max Sets the max for the Constructor for Parts
     */
    public Part(String name, int id, double price, int stock, int min, int max) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Declares the Name getter
     * @return Returns Part Name
     */
    public String getName() {
        return name;
    }

    /**
     * Declares the ID getter
     * @return Returns the Part ID
     */
    public int getId() {
        return id;
    }

    /**
     * Declares the Price getter
     * @return Returns the Part Price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Declares the Stock getter
     * @return Returns the Part Inventory
     */
    public int getStock() {
        return stock;
    }

    /**
     * Declares the Min getter
     * @return Returns the Part Minimum Inventory
     */
    public int getMin() {
        return min;
    }

    /**
     * Declares the Part Max getter
     * @return Returns the Part Max
     */
    public int getMax() {
        return max;
    }

    /**
     * Declares the Part Name setter
      * @param name Part Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Declares the Part ID setter
     * @param id Part ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Declares the Part Price
     * @param price Part Price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Declares the Part Min
     * @param min Part Minimum
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Declares the Part Max
     * @param max Part Maximum
     */
    public void setMax(int max) {
        this.max = max;
    }
}
