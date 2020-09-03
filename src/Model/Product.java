package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author Sam Gonzales
 */

public class Product {

    //Product Constructor
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }


    /**
     * Declare Products Constructor
     * @param name Sets the name for the Constructor for Products
     * @param id Sets the id for the Constructor for Products
     * @param price Sets the price for the Constructor for Products
     * @param stock Sets the stock for the Constructor for Products
     * @param min Sets the min for the Constructor for Products
     * @param max Sets the max for the Constructor for Products
     */
    public Product(String name, int id, double price, int stock, int min, int max) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Product Constructor
     */
    public Product() {
    }

    private String name;
    private int id;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

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
     * Declares the Part Inventory
     * @param inv Part Inventory
     */
    public void setStock(int inv) {
        this.stock = inv;
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

    /**
     * Add Associated Part
     * @param part Add Associated Part
     */
    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);
    }

    /**
     * Get Associated Part
     * @return Returns the Associated Part
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Deletes the Associated part
     */
    public void deleteAllAssociatedParts() {
        associatedParts.clear();
    }

    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}
