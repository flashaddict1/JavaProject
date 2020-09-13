package View_Controller;

import Model.*;
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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Form for Viewing and Deleting parts and products, contains buttons for Modifying and Adding new parts and products.
 * @author Sam Gonzales
 *
 * <p>
 * Error Exception
 * <br>Put below and explained in detail on every location that an error can occur.
 * <p>
 * <br>FUTURE IMPROVEMENTS
 * <br>Should add functionallity to select more than one part at a time to modify in bulk
 * <br>Should add functionallity to select more than one part at a time to delete in bulk
 * <br>Should add functionallity to select more than one product at a time to modify in bulk
 * <br>Should add functionallity to select more than one product at a time to delete in bulk
 * <br>Should add the ability to have the search field update while the user is typing.
 * <br>Adding in functionallty to see what parts that are assocaited with products live would benifit the user as well
 * to let them see if they need to modify the item before going through the steps.
 */

public class MainWindow implements Initializable {

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
     * <p>
     * Error Exception happens if the Part menu does not exist or if the program is unable to open the Form up.
     * This causes the application to stall and not proceed any farther as there is no window for the user to return to.
     * This is a fatal error.
     *
     * @param event Changes the Stage to Add Part Menu if unable to open Add Part Menu an alert pops up alerting the
     *              user that system was unable to open up the menu.
     */
    @FXML
    public void OpenAddPartMenu(ActionEvent event) {
        try {
            switchWindow(event,"../View_Controller/AddPart.fxml", "Add Part" );
        } catch (Exception openAddPartMenu) {
            alertMenu("Unable to open the Add a Part Menu!");
        }
    }

    /**
     * Opens up the Add Product Menu
     * <p>
     * Error Exception happens if the Product menu does not exist or if the program is unable to open the Form up.
     * This causes the application to stall and not proceed any farther as there is no window for the user to return to.
     * This is a fatal error.
     *
     * @param event Changes the Stage to Add Product Menu if unable to open Add Part Menu an alert pops up alerting the
     *              user that system was unable to open up the menu.
     */
    @FXML
    public void OpenAddProductMenu(ActionEvent event) {
        try {
            switchWindow(event,"../View_Controller/AddProduct.fxml", "Add Product" );
        } catch (Exception openAddProductMenu) {
            alertMenu("Unable to open the Add a Product Menu!");
        }
    }

    /**
     * Opens Modify Part Menu and completes the form with the data selected
     * Error Exception happens when the does not select a part to modify
     * User Must Select a part to Continue
     *
     * @param event Allows the user to select a part to add and changes the scene to the Modify Part Menu
     */
    @FXML
    public void onActionModifyPart(javafx.event.ActionEvent event) {
        try {
            //Selects Table View Row
            Part part = tblViewPart.getSelectionModel().getSelectedItem();
            if (part == null) {
                alertMenu("You must select a part in order to modify!");
                return;
            }

            //Opens Modify Menu
            switchWindow(event,"../View_Controller/ModifyPart.fxml", "Modify Part" );
        } catch (Exception noSelectionModify) {
            alertMenu("You must select a part in order to modify!");
        }
    }

    /**
     * An alert pops up alerting the user with a confirmation message. If the user selects okay, the program will close
     * if the user selects cancel, the alert message will close and user is returned to Main Window.
     */
    public void ExitMenu() {
        Optional<ButtonType> result = alertMenu();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Searches the Part Tableview for parts depending on the text in txtPartSearch field, the field can search
     * Alphanumeric characters. Users are able to search for ID and/or Name or parts. If the field is left blank
     * the Tableview shows all parts in the database.
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
     * Searches the Product Tableview for parts depending on the text in txtProductSearch field, the field can search
     * Alphanumeric characters. Users are able to search for ID and/or Name or parts. If the field is left blank
     * the Tableview shows all Products in the database.
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
     * Opens Modify Product Menu and completes the form with the data selected
     * <p>
     * Error Exception happens when the user tries to modify a part, but does not select one to modify.
     * User must select a part to continue
     *
     * @param event Changes the stage to Modify Product Menu if the user has a part selected, if the user does not have
     *              a part selected an alert will alert the user to select the part before being able to proceed.
     */
    @FXML
    void onActionModifyProduct(ActionEvent event) {
        try {
            //Selects Table View Row
            Product product = tblViewProduct.getSelectionModel().getSelectedItem();
            if (product == null) {
                alertMenu("You must select a part in order to modify!");
                return;
            }
//Opens Modify Menu
            FXMLLoader Loader = new FXMLLoader(getClass().getResource("../View_Controller/ModifyProduct.fxml"));
            Loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = Loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            ModifyProduct modifyProductController = Loader.getController();
            modifyProductController.selectedProduct(product.getId());

        } catch (Exception selectModifyPartError) {
            alertMenu("You must select a part in order to modify!");
        }
    }

    /**
     * Delete the selected Part, if no part is selected an error message pops up alerting the user to select a part
     * if the user selects a part, an alert will appear asking user to confirm deletion.
     * <p>
     * Error Exception happens when a user tries to delete a part, but the program is unable to delete it.
     * This is a fatal error. Part does not exist in the database.
     */
    @FXML
    void onActionDeletePart() {
        Part part = tblViewPart.getSelectionModel().getSelectedItem();
        if (part == null) {
            alertDeletePartError("Please select a part.");
            return;
        }

        //Popup for Part Confirmation Delete
        Optional<ButtonType> result = alertConfirmDelete("Confirm");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Part " + part.getName() + " has been deleted!");
            deletePart(part.getId());
        } else {
            alertDeletePartError("Unable to delete Part!.");
        }
    }

    /**
     * Delete Selected Part from the database
     * <p>
     * Error Exception happens when the user tries to delete a part, but the part is not selected.
     * User must select a part to proceed.
     *
     * @param id Deletes the part by the id that is supplied in the onActionDeletePart.
     */
    public void deletePart(int id) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        for (Model.Part Part : allParts) {
            if (Part.getId() == id) {
                Part selectedPart = tblViewPart.getSelectionModel().getSelectedItem();
                if (selectedPart == null) {
                    alertMenu("Error unable delete part because it doesn't exist");
                }
                alertPart(Part);
                return;
            }
        }
    }



    /**
     * Delete the selected Product, if no part is selected an error message pops up alerting the user to select a Product
     * if the user selects a Product, an alert will appear asking user to confirm deletion.
     * <p>
     * Error Exception happens when a user tries to delete a part, but the program is unable to delete it.
     * If a user tries to delete a product that has an association with a part it will return an error
     * User must remove part association before proceeding.
     */
    @FXML
    void onActionDeleteProduct() {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        //Popup for Product Confirmation Delete

        if (product == null) {
            alertDeletePartError("Please select a part.");
            return;
        }

        //delete product;
        if (product.getAssociatedParts().size() > 0) {
            alertPartAss();
        }
        Optional<ButtonType> result = alertConfirmDelete("CONFIRMATION");

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            alertDeleted(product.getName());
        }
    }

    /**
     * Delete Selected Product from the database
     * <p>
     * Error Exception happens when the user tries to delete a Product, but the Product is not selected.
     * User must select a part first.
     *
     * @param id Deletes the Product by the id that is supplied in the onActionDeleteProduct.
     */
    public void deleteProduct(int id) {
        Inventory.getAllProducts().stream().filter(Product -> Product.getId() == id).findFirst().ifPresent(Product -> Inventory.getAllProducts().remove(Product));
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

    private void alertPartAss() {
        Alert alertPartAss = new Alert(Alert.AlertType.INFORMATION);
        alertPartAss.setContentText("There is a part Association, please remove the association");
        alertPartAss.showAndWait();
    }

    private void alertDeleted(String name) {
        Alert partDeleted = new Alert(Alert.AlertType.NONE);
        partDeleted.setAlertType(Alert.AlertType.INFORMATION);
        partDeleted.setContentText("Part " + name + " has been deleted!");
        partDeleted.show();
    }

    private Optional<ButtonType> alertConfirmDelete(String confirmation) {
        Alert alertConfirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmDelete.setTitle(confirmation);
        alertConfirmDelete.setHeaderText("Deleting is Permanent");
        alertConfirmDelete.setContentText("Are you sure you want to DELETE?");
        return alertConfirmDelete.showAndWait();
    }

    private void alertPart(Part part) {
        Inventory.getAllParts().remove(part);
        alertDeleted(part.getName());
    }

    private void alertDeletePartError(String s) {
        Alert deletePartError = new Alert(Alert.AlertType.ERROR);
        deletePartError.setTitle("Error!");
        deletePartError.setContentText(s);
        deletePartError.showAndWait();
    }

    private void alertMenu(String s) {
        Alert addPartMenu = new Alert(Alert.AlertType.NONE);
        addPartMenu.setAlertType(Alert.AlertType.ERROR);
        addPartMenu.setContentText(s);
        addPartMenu.show();
    }

    private void switchWindow(ActionEvent event, String s, String t) throws java.io.IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource(s));
        stage.setTitle(t);
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    private Optional<ButtonType> alertMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to close program?");
        return alert.showAndWait();
    }
}