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
 * @author Sam Gonzales
 * Improvements can be made to combine the modify and add products menus to create a more streamlined experience, should
 * also reduce the size of the program overall.
 * Search fields can also be adjusted to allow for updating as the user types the product.
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
     * Canceling adding a part
     *
     * @param event Cancels adding the part, Alerts the user to confirm canceling the part. Alerts the user if program
     *              is unable to return to main window.
     */
    public void onActionProductCancel(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        } catch (RuntimeException | IOException runtimeException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Unable to find the Main Window");
            a.show();
        }
    }

    private final ObservableList<Part> addedParts = FXCollections.observableArrayList();
    FilteredList<Part> filteredPartsData = new FilteredList<>(Inventory.getAllParts(), p -> true);

    /**
     * Searches the Product  for parts depending on the text in txtProdSeearch field, the field can search
     * Alphanumeric characters. Users are able to search for ID and/or Name or parts. If the field is left blank
     * the Tableview shows all Products in the database.
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
     * Deletes the part associations with the selected product.
     */
    @FXML
    void onActionDeletePart() {
        Part selectedPart = tblViewProduct2.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            addedParts.remove(selectedPart);
        } else {
            System.out.println("No part selected");
        }
    }

    /**
     * Saves the part
     *
     * @param event Saves the part and uses the details provided by the user, if the user enters an invalid character,
     *              it will alert the user if they do not create the part with alphanumeric charters.
     */
    @FXML
    void onActionSaveProduct(javafx.event.ActionEvent event) {
        try {
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

        } catch (NumberFormatException | IOException numberFormatException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Invalid Character! Part must only contain AlphaNumeric characters!");
            a.show();
        }
    }

    /**
     * Creates associations with the part being created. Alerts the user if unable to add part to association
     */
    @FXML
    void onActionAddPart() {
        Part selectedPart = tblViewProduct1.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            // Add part into the ObservableList<> addedParts
            addedParts.add(selectedPart);
        } else {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Error unable to associate part");
            a.show();
        }
    }

    /**
     * Initializes the Table View
     *
     * @param url points to the Specified tag, ID, Name, Price, Stock
     * @param rb  populates the tableviews with information gained from Inventory.getAllParts. Sets the parts of the
     *            items by matching id, name, price, stock to the columns.
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


