package View_Controller;

import Model.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Sam Gonzales
 */

public class MainWindow  implements Initializable {

    Stage stage;
    Parent scene;

    FilteredList<Part> filteredPartsData = new FilteredList<>(Inventory.getAllParts(), p -> true);
    FilteredList<Product> filteredProductsData = new FilteredList<>(Inventory.getAllProducts(), p -> true);

    @FXML
    private TableView<Part> tblViewPart;
    @FXML
    private TableColumn<Part, Integer> tblColPartId;
    @FXML
    private TableColumn<Part, String> tblColPartName;
    @FXML
    private TableColumn<Part, Integer> tblColPartInvLvl;
    @FXML
    private TableColumn<Part, Double> tblColPartCost;
    @FXML
    private TextField txtPartSearch;
    @FXML
    private TableView<Product> tblViewProduct;
    @FXML
    private TableColumn<Product, Integer> tblColProdId;
    @FXML
    private TableColumn<Product, String> tblColProdName;
    @FXML
    private TableColumn<Product, Double> tblColProdCost;
    @FXML
    private TableColumn<Product, Integer> tblColProdInvLvl;
    @FXML
    private TextField txtProductSearch;

    /**
     * Opens up the Add Part Menu
     * @param event Changes the Stage to Add Part Menu
     * @throws IOException if unable to open Add Part
     */
    @FXML
    public void OpenAddPartMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/AddPart.fxml"));
        stage.setTitle("Add Part");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Opens Add Product Menu
     * @param event changes the stage to Add Product Menu
     * @throws IOException if unable to open Add Product
     */
    @FXML
    public void OpenAddProductMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../View_Controller/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Opens Modify Part Menu
     * @param event changes the stage to Modify Part Menu
     * @throws IOException if unable to open Modify Part
     */
    @FXML
    public void onActionModifyPart(ActionEvent event) throws IOException {
        //Selects Table View Row
        Part part = tblViewPart.getSelectionModel().getSelectedItem();
        if (part == null)
            return;

        //Opens Modify Menu
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../View_Controller/ModifyPart.fxml"));
        loader.load();
        ModifyPart PartController = loader.getController();
        PartController.getPart(part);
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Exits the program
     */
    public void ExitMenu() {
        System.exit(0);
    }


    /**
     * Searches the Part Tableview
      */
    @FXML
    void onActionSearchPart() {
        filteredPartsData.setPredicate(part -> {
            if (txtPartSearch.getText().equals("")) {
                return true;
            }
            String lowerCaseFilter = txtPartSearch.getText().toLowerCase();
            if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return Integer.toString(part.getId()).contains(lowerCaseFilter);
        });
        SortedList<Part> sortedPartsData = new SortedList<>(filteredPartsData);
        sortedPartsData.comparatorProperty().bind(tblViewPart.comparatorProperty());
        tblViewPart.setItems(sortedPartsData);
    }

    /**
     * Searches the Product Tableview
     */
    @FXML
    void onActionSearchProduct() {
        filteredProductsData.setPredicate(product -> {
            if (txtProductSearch.getText().equals("")) {
                return true;
            }
            String lowerCaseFilter = txtProductSearch.getText().toLowerCase();
            if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else
                return Integer.toString(product.getId()).contains(lowerCaseFilter);
        });
        SortedList<Product> sortedProductsData = new SortedList<>(filteredProductsData);
        sortedProductsData.comparatorProperty().bind(tblViewProduct.comparatorProperty());
        tblViewProduct.setItems(sortedProductsData);
    }

    /**
     * Opens the Modify Product Menu
      * @param event changes the stage to Modify Product Menu
     * @throws IOException If unable to open up Modify Product Menu
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("../View_Controller/ModifyProduct.fxml"));
        Loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = Loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null)
            return;
        ModifyProduct modifyProductController = Loader.getController();
        modifyProductController.selectedProduct(product.getId());
    }

    /**
     * Delete the selected Part
     */
    @FXML
    void onActionDeletePart() {

        Part part = tblViewPart.getSelectionModel().getSelectedItem();
        if (part == null)
            return;

        //Popup for Part Confirmation Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Deleting is Permanent");
        alert.setContentText("Are you sure you want to DELETE?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(null) == ButtonType.OK) {
            deletePart(part.getId());
        }
    }

    /**
     * Delete Selected Part
      * @param id part id is selected part
     */
    public void deletePart(int id) {
        for (Part Part : Inventory.getAllParts()) {
            if (Part.getId() == id) {
                Inventory.getAllParts().remove(Part);
                return;
            }
        }
    }

    /**
     * Delete the selected product
     */
    @FXML
    void onActionDeleteProduct() {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        if (product == null)
            return;

        //Popup for Product Confirmation Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText("Deleting is Permanent");
        alert.setContentText("Are you sure you want to DELETE?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(null) == ButtonType.OK) {
            deleteProduct(product.getId());
        }
    }

    /**
     * Delete Selected Product
     * @param id id is the Selected Part
     */
    public void deleteProduct(int id) {
        for (Product Product : Inventory.getAllProducts()) {
            if (Product.getId() == id) {
                Inventory.getAllProducts().remove(Product);
                return;
            }
        }
    }

    /**
     * Initializes the Table View
     * @param url points to the Specified tag, ID, Name, Price, Stock
     * @param rb Table View
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Parts Table View Information
        tblViewPart.setItems(Inventory.getAllParts());

        //Fill Part Table
        tblColPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblColPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        tblColPartInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //Products Table View Information
        tblViewProduct.setItems(Inventory.getAllProducts());

        //Fill Product Table
        tblColProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblColProdCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        tblColProdInvLvl.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}

