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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Sam Gonzales
 *
 * <P>
 * Runtime Errors
 * Put below and explained in detail on every location that an error can occur.
 *
 * FUTURE IMPROVEMENTS
 * Should combine the Add Product and Modify Product menus making for a streamlined experience and shrinking the size of the
 * overall program.
 * The ability to Modify multiple parts at once
 * The ability to view all parts currently existing
 * Drop Down menu for the machine code already added to add additional with the same code without typing the whole thing.
 * Drop down menu for companies already added to quickly add more.
 * Live editing that will not allow the user to type anything in the inventory field that is larger than Max
 * Live editing that will not allow for the user to type anything in the Min field that is larger than Max
 * Search field on the top can be updated to allow for instant results as the user is typing instead of what currently
 * happens as the user currently has to push enter to search.
 * Allowing the user to select multiple parts to add into the association at once will be benifical when the list has
 * a large number of parts for the user to go through.
 * Allowing the user to select multiple parts to unassociate at once will be benifical to the user when there is a large
 * number of items that are associated.
 * Allowing the user to remove the assocaition of all parts will help out as well if the user doesn't need any of the
 * associations.
 * </P>
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
     * Cancels modifying the part
     * Error Exception happens when a user is unable to return to the main window an alert pops up notifying the user
     * that the form is no longer found.
     *
     * @param event Cancels modifying the part and returns to the Main Window
     */
    public void ModifyProductCancel(javafx.event.ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        } catch (Exception modifyCancelError) {
            Alert modifyCancelAlert = new Alert(Alert.AlertType.NONE);
            modifyCancelAlert.setAlertType(Alert.AlertType.ERROR);
            modifyCancelAlert.setContentText("Unable to find the Main Window");
            modifyCancelAlert.show();
        }
    }

    /**
     * Get Selected Product
     *
     * @param id Gets the id from the selected Product and sets it to be edited.
     *           retrieves ID, and matches it with, name, stock, max, min
     */
    public void selectedProduct(int id) {
        ObservableList<Product> allProductData = Inventory.getAllProducts();
        allProductData.forEach(product -> {
            if (product.getId() == id) {
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
     * Searches the Product  for parts depending on the text in txtProdSeearch field. The user must use alphanumeric
     * characters when searching. Users have the ability to search by Product Name and by Product ID.
     * If the field is left blank the Tableview shows all Products in the database.
     *
     * Error Exception happens when the user uses the search field and the field has non Alphanumeric characters.
     * The user will be unable to proceed further and will need to change the fields to the appropriate data type.
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
     * Creates associations with the part being created
     *
     * Error Exception happens and alerts the user if unable to add part to association.
     */
    public void onActionAddPart() {
        Part selectedPart = tblViewProduct1.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            addedParts.add(selectedPart);
        } else {
            Alert addPartAlert = new Alert(Alert.AlertType.NONE);
            addPartAlert.setAlertType(Alert.AlertType.ERROR);
            addPartAlert.setContentText("No Part Selected to add.");
            addPartAlert.show();
        }
    }

    /**
     * Removes the part association with the part currently being added. The user will have an alert popup confirming
     * that they would like the remove the association.
     *
     * Error Exception happens if the user tries to remove the association if there was no association to begin with.
     */
    public void onActionDeletePart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the association?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part selectedPart = tblViewProduct2.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                addedParts.remove(selectedPart);
                System.out.println("Removed association!");
            } else {
                Alert removeAssAlert = new Alert(Alert.AlertType.NONE);
                removeAssAlert.setAlertType(Alert.AlertType.ERROR);
                removeAssAlert.setContentText("You must select a part to remove from association.");
                removeAssAlert.show();
            }
        }
    }

    /**
     * Saves the part
     * @param event Saves the Part that the user is Modifying. Takes the ID, Name, Inventory, Min, and Max from the form
     *              and saves it. After the part is saved into memory, the user is then brought into the Main Window.
     *
     *              Error checking happens if the user tries to enter in a Min amount that is greater then Max.
     *              Error checking happens if the user tries to enter in an Inv amount that is greater than Max.
     *              Error checking happens if the user does not fill in the form to completion alerting the user
     *              to finish editing the form.
     */
    @FXML
    public void onActionSaveProduct(javafx.event.ActionEvent event) {
        try {
            ObservableList<Product> allProductsData = Inventory.getAllProducts();
            allProductsData.forEach(product -> {
                if (product.getId() == Integer.parseInt(txtModProdId.getText())) {
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
        } catch (Exception modifyProductError) {
            Alert modifyProductAlert = new Alert(Alert.AlertType.NONE);
            modifyProductAlert.setAlertType(Alert.AlertType.ERROR);
            modifyProductAlert.setContentText("Unable to Modify the Product, Use Alphanumeric Characters only!");
            modifyProductAlert.show();
        }
    }

    /**
     * Initializes the Table View. The second table view is populated will parts that are being associated with the
     * product.
     *
     * @param url points to the Specified tag, ID, Name, Price, Stock
     * @param rb  populates the tableviews with information gained from Inventory.getAllParts. Sets the parts of the
     *            items by matching id, name, price, stock to the columns.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
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
        } catch (Exception UnableToLoadError) {
            Alert UnableToLoadAlert = new Alert(Alert.AlertType.NONE);
            UnableToLoadAlert.setAlertType(Alert.AlertType.ERROR);
            UnableToLoadAlert.setContentText("Unable to load the table view!");
            UnableToLoadAlert.show();
        }
    }
}


