package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Form for creating a new Product
 *
 * @author Sam Gonzales
 *
 * <p>
 * Runtime Errors
 * <br>Put below and explained in detail on every location that an error can occur.
 * <p>
 * <br>FUTURE IMPROVEMENTS
 * <br>Should combine the Add Product and Modify Product menus making for a streamlined experience and shrinking the size of the
 * overall program.
 * <br>The ability to add multiple parts at once
 * <br>The ability to view all parts currently existing
 * <br>Drop Down menu for the machine code already added to add more that match.
 * <br>Drop down menu for companies already added to quickly add more.
 * <br>Live editing that will not allow the user to type anything in the inventory field that is larger than Max
 * <br>Live editing that will not allow for the user to type anything in the Min field that is larger than Max
 * <br>Search field on the top can be updated to allow for instant results as the user is typing instead of what currently
 * <br>happens as the user currently has to push enter to search.
 * <br>Allowing the user to select multiple parts to add into the association at once will be benifical when the list has
 * a large number of parts for the user to go through.
 * <br>Allowing the user to select multiple parts to unassociate at once will be benifical to the user when there is a large
 * number of items that are associated.
 * <br>Allowing the user to remove the assocaition of all parts will help out as well if the user doesn't need any of the
 * associations.
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
     * @param event Confirms that the user wants to cancel creation of a new form. Cancels the creation of a new part.
     *              Returns to the Main Window and clears out the information already entered into the form.
     *              <p>
     *              Error Exception happens when the Main window is not able to be loaded.
     *              This causes the application to stall and not proceed any farther as there is no window for the user
     *              to return to.
     *              This is a fatal error.
     */
    public void onActionProductCancel(javafx.event.ActionEvent event) {
        try {
            changeWindow(event);
        } catch (Exception invalidWindowLink) {
            alertMessage("Unable to find the Main Window");
        }
    }


    private final ObservableList<Part> addedParts = FXCollections.observableArrayList();
    FilteredList<Part> filteredPartsData = new FilteredList<>(Inventory.getAllParts(), p -> true);

    /**
     * Searches the Product  for parts depending on the text in txtProdSeearch field. The user must use alphanumeric
     * characters when searching. Users have the ability to search by Product Name and by Product ID.
     * If the field is left blank the Tableview shows all Products in the database.
     * <p>
     * Error Exception happens when the user uses the search field and the field has non Alphanumeric characters.
     * The user will be unable to proceed further and will need to change the fields to the appropriate data type.
     */
    @FXML
    void onActionSearchProduct() {
        filteredPartsData.setPredicate(this::test);
        SortedList<Part> sortedPartsData = new SortedList<>(filteredPartsData);
        sortedPartsData.comparatorProperty().bind(tblViewProduct1.comparatorProperty());
        tblViewProduct1.setItems(sortedPartsData);
    }

    /**
     * Removes the part association with the part currently being added. The user will have an alert popup confirming
     * that they would like the remove the association.
     * <p>
     * Error Exception happens if the user tries to remove the association if there was no association to begin with.
     * User must remove all part associations before proceeding.
     */
    @FXML
    void onActionDeletePart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete the association?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part selectedPart = tblViewProduct2.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                addedParts.remove(selectedPart);
                System.out.println("Removed association!");
            }
            alertMessage("There is no part association.");
        }
    }

    /**
     * Saves the part
     *
     * @param event Saves the Part that the user is creating. Takes the ID, Name, Inventory, Min, and Max from the form
     *              and saves it. After the part is saved into memory, the user is then brought into the Main Window.
     *              <p>
     *              Error checking happens if the user tries to enter in a Min amount that is greater then Max.
     *              Error checking happens if the user tries to enter in an Inv amount that is greater than Max.
     *              Error checking happens if the user does not fill in the form to completion
     *              All errors alert the user to correct the form before continuing.
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

            //Error if Inv is Less then Zero
            if (stock < 0) {
                alertMessage("Inventory needs to be greater than 0.");
            }

            //Error if Min is Greater then Max field
            if (min > max) {
                alertMessage("Quantity Min needs to be smaller than Max");
                return;
            }
            //Error if Inventory is Greater then Max Field
            if (stock > max) {
                alertMessage("Inventory needs to be smaller than Max");
                return;
            }

            //Return to Main Window
            Inventory.addProduct(newProduct);
            changeWindow(event);

        } catch (Exception invalidCharError) {
            alertMessage("Invalid Character! Part must only contain AlphaNumeric characters!");
        }
    }


    /**
     * Creates associations with the part being created.
     * <p>
     * Error Exception happens if the user tries to add a part that does not exist in the database
     * this will then Alert the user if unable to add part to association
     * This is a fatal error.
     */
    @FXML
    void onActionAddPart() {
        Part selectedPart = tblViewProduct1.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            addedParts.add(selectedPart);
        }
        alertMessage("Error unable to associate part");
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

    private void alertMessage(String s) {
        Alert invalidCharAlert = new Alert(Alert.AlertType.NONE);
        invalidCharAlert.setAlertType(Alert.AlertType.ERROR);
        invalidCharAlert.setContentText(s);
        invalidCharAlert.show();
    }

    private void changeWindow(ActionEvent event) throws java.io.IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    private boolean test(Part part) {
        if (txtProdSearch.getText().equals("")) {
            return true;
        }
        String lowerCaseFilter = txtProdSearch.getText().toLowerCase();
        return part.getName().toLowerCase().contains(lowerCaseFilter) || Integer.toString(part.getId()).contains(lowerCaseFilter);
    }
}


