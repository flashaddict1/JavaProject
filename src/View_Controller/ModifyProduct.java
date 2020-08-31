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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Sam Gonzales
 */

public class ModifyProduct implements Initializable {

    private final ObservableList<Part> addedParts = FXCollections.observableArrayList();
    FilteredList<Part> filteredPartsData = new FilteredList<>(Inventory.getAllParts(), p -> true);

    @FXML
    private TextField txtProdSearch;
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
    private TextField txtModProdId;
    @FXML
    private TextField txtModProdName;
    @FXML
    private TextField txtModProdInv;
    @FXML
    private TextField txtModProdPrice;
    @FXML
    private TextField txtModProdMin;
    @FXML
    private TextField txtModProdMax;
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
     * Modify Project Constructor
     */
    public ModifyProduct() {
    }

    /**
     * Cancel Modifying Product
     * @param event Returns to Main Menu
     * @throws IOException if unable to return to Main Window
     */
    public void ModifyProductCancel(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
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


    /**
     * Get Selected Product
     * @param id Gets the id from the selected Product
     */
    public void selectedProduct(int id) {
        ObservableList<Product> allProductData = Inventory.getAllProducts();
        allProductData.forEach(product -> {
            if(product.getId() == id) {
                // Insert data into text field depending on id selected
                txtModProdId.setText(Integer.toString(product.getId()));
                txtModProdName.setText(product.getName());
                txtModProdInv.setText(Integer.toString(product.getStock()));
                txtModProdPrice.setText(Double.toString(product.getPrice()));
                txtModProdMax.setText(Integer.toString(product.getMax()));
                txtModProdMin.setText(Integer.toString(product.getMin()));
                addedParts.addAll(product.getAllAssociatedParts());
            }
        });
    }

    /**
     * Search Products Tableview
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
            }
            else return Integer.toString(part.getId()).contains(lowerCaseFilter);
        });
        SortedList<Part> sortedPartsData = new SortedList<>(filteredPartsData);
        sortedPartsData.comparatorProperty().bind(tblViewProduct1.comparatorProperty());
        tblViewProduct1.setItems(sortedPartsData);
    }

    /**
     * Adds the selected part to product association
     */
    public void onActionAddPart() {
        Part selectedPart = tblViewProduct1.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            // Add part into the ObservableList<> addedParts
            addedParts.add(selectedPart);
        } else {
            System.out.println("No Part selected");
        }
    }

    /**
     * Deletes the part association
     */
    public void onActionDeletePart() {
        Part selectedPart = tblViewProduct2.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            addedParts.remove(selectedPart);
        } else {
            System.out.println("No part selected");
        }
    }

    /**
     * Saves the Modified Product
     * @param event Saves the Modified Product
     * @throws IOException If unable to return to Main Window
     */
    @FXML
    public void onActionSaveProduct(ActionEvent event) throws IOException {
        ObservableList<Product> allProductsData = Inventory.getAllProducts();
        allProductsData.forEach(product -> {
            if( product.getId() == Integer.parseInt(txtModProdId.getText())) {
                String productName = txtModProdName.getText();
                double productPrice = Double.parseDouble(txtModProdPrice.getText());
                int productStock = Integer.parseInt(txtModProdInv.getText());
                int productMax = Integer.parseInt(txtModProdMax.getText());
                int productMin = Integer.parseInt(txtModProdMin.getText());

                // Set all updated data into Product
                product.setName(productName);
                product.setPrice(productPrice);
                product.setStock(productStock);
                product.setMax(productMax);
                product.setMin(productMin);

                // Clear out the associated parts list in the product
                product.deleteAllAssociatedParts();

                // Add parts to Product Group
                addedParts.forEach(product::addAssociatedPart);
            }
        });

        //Returns to Main Window
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
}


