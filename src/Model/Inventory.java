package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Contains modules for Getter, Setter, Lookup Part, and Update Parts
 * <p>
 *
 * @author Sam Gonzales
 */

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Declares the getter for All Parts
     *
     * @return Returns All Parts in Inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Declares the getter for All Products
     *
     * @return Returns All Products in Inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Adds Part to Inventory
     *
     * @param newPart Adds the Part to the Inventory
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds Products to Inventory
     *
     * @param product Adds the Product to the Inventory
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * Lookup Parts in Inventory
     *
     * @param inputID Gets the Part
     * @return Returns the Part
     */
    public static int lookupPartIndex(int inputID) {
        return allParts.stream().filter(lookupPart -> lookupPart.getId() == inputID).findFirst().map(allParts::indexOf).orElse(-1);
    }

    /**
     * Updates the Part in Inventory
     *
     * @param moddedPart   Modifies the existing Part
     * @param modifiedPart The existing part in Inventory
     */
    public static void updatePart(int moddedPart, Part modifiedPart) {
        allParts.set(moddedPart, modifiedPart);
    }

}
