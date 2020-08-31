package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Sam Gonzales
 */

public class AddProduct implements Initializable {
    @FXML
    private TableView<Part> tblViewProduct1;
    @FXML
    private TableColumn<Part, Integer> tblColProdId1;
    @FXML
    private TableColumn<Part, String> tblColProdName1;
    @FXML
    private TableColumn<Part, Integer> tblColProdInvLvl1;
    @FXML
    private TableColumn<Part, Double> tblColProdCost1;
    @FXML
    private TextField txtAddProdName;
    @FXML
    private TextField txtAddProdInv;
    @FXML
    private TextField txtProdSearch;
    @FXML
    private TextField txtAddProdPrice;
    @FXML
    private TextField txtAddProdMin;
    @FXML
    private TextField txtAddProdMax;
    @FXML
    private TableView<Part> tblViewProduct2;
    @FXML
    private TableColumn<Part, Integer> tblColProdId2;
    @FXML
    private TableColumn<Part, String> tblColProdName2;
    @FXML
    private TableColumn<Part, Integer> tblColProdInvLvl2;
    @FXML
    private TableColumn<Part, Double> tblColProdCost2;

    /**
     * Cancels Adding Product
     * @param event Return To Main Window on Add Product Cancel
     * @throws IOException If unable to return to Main Menu
     */
    public void onActionProductCancel(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    private final ObservableList<Part> addedParts = FXCollections.observableArrayList();
    FilteredList<Part> filteredPartsData = new FilteredList<>(Inventory.getAllParts(), p -> true);

    /**
     * Search Products in Inventory
     */
    @FXML
    void onActionSearchProduct() {
        filteredPartsData.setPredicate(part -> {
            if (txtProdSearch.getText().equals("")) {
                return true;
            }
            String lowerCaseFilter = txtProdSearch.getText().toLowerCase();
            if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            } else return Integer.toString(part.getId()).contains(lowerCaseFilter);
        });
        SortedList<Part> sortedPartsData = new SortedList<>(filteredPartsData);
        sortedPartsData.comparatorProperty().bind(tblViewProduct1.comparatorProperty());
        tblViewProduct1.setItems(sortedPartsData);
    }

    /**
     * Remove Associated Part
      */
    @FXML
    void onActionDeletePart() {
        Part selectedPart = tblViewProduct2.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            addedParts.remove(selectedPart);
        } else {
            System.out.println("No part selected");
        }
    }

    /**
     * Save Added Product
      * @param event Save Added Product and returns to Main Window
     * @throws IOException If unable to Return to Main Window
     */
    @FXML
    void onActionSaveProduct(javafx.event.ActionEvent event) throws IOException {
        String name = txtAddProdName.getText();
        int id = Inventory.getAllProducts().size() + 1;
        double price = Double.parseDouble(txtAddProdPrice.getText());
        int stock = Integer.parseInt(txtAddProdInv.getText());
        int min = Integer.parseInt(txtAddProdMin.getText());
        int max = Integer.parseInt(txtAddProdMax.getText());
        Product newProduct = new Product(name, id, price, stock, min, max);

        //Add all selected parts to the product
        addedParts.forEach(newProduct::addAssociatedPart);

        //Return to Main Window
        Inventory.addProduct(newProduct);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Add Part to Inventory
     */
    @FXML
    void onActionAddPart() {
        Part selectedPart = tblViewProduct1.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            // Add part into the ObservableList<> addedParts
            addedParts.add(selectedPart);
        }
        else {
            System.out.println("No Part selected");
        }
    }

    /**
     * Initialize Table View
     * @param url points to the Specified tag, ID, Name, Price, Stock
     * @param rb Table View
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Products Table View Information 1
        tblViewProduct1.setItems(Inventory.getAllParts());
        tblColProdId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColProdName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblColProdCost1.setCellValueFactory(new PropertyValueFactory<>("price"));
        tblColProdInvLvl1.setCellValueFactory(new PropertyValueFactory<>("stock"));

        //Products Table View Information 2
        tblViewProduct2.setItems(addedParts);
        tblColProdId2.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblColProdName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        tblColProdCost2.setCellValueFactory(new PropertyValueFactory<>("price"));
        tblColProdInvLvl2.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }
}


