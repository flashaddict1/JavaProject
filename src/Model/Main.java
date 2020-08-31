package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Sam Gonzales
 */

public class Main extends Application {
    @Override
    /**
     * Runs on Startup, sets the Stage
     */
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 900, 350));
        primaryStage.show();
    }

    /**
     * Populates the Main Window
     * @param args Populates the Main Window with preset data
     */
    public static void main(String[] args) {
        //Part Inventory
        Part part1 = new InHouse("Rims", 1, 12.22, 2, 1, 25, 982570);
        Part part2 = new Outsourced("Tires", 2, 22.22, 3, 1, 25, "Sam's");
        Part part3 = new InHouse("Muffler", 3, 24.52, 2, 1, 25, 7189);
        Part part4 = new Outsourced("Windshield", 4, 54.12, 3, 1, 25, "Rachel's");
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);

        //Product Inventory
        Product prod1 = new Product("Door Handle", 1, 12.22, 2, 1, 25);
        Product prod2 = new Product("Driver Seat", 2, 222.22, 3, 1, 25);
        Product prod3 = new Product("Tail Light", 3, 234.52, 2, 1, 25);
        Product prod4 = new Product("Head Light", 4, 514.12, 3, 1, 25);
        Product prod5 = new Product("Door", 5, 122.42, 4, 1, 25);
        prod1.addAssociatedPart(part1);
        prod2.addAssociatedPart(part1);
        prod3.addAssociatedPart(part2);
        prod4.addAssociatedPart(part3);
        prod5.addAssociatedPart(part4);
        Inventory.addProduct(prod1);
        Inventory.addProduct(prod2);
        Inventory.addProduct(prod3);
        Inventory.addProduct(prod4);
        Inventory.addProduct(prod5);

        launch(args);
    }
}