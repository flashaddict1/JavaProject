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
 * @author Sam Gonzales
 * Should add functionallity to select more than one part at a time to modify or delete.
 * Should add the ability to have the search field update while the user is typing.
 */

public class MainWindow implements Initializable {

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
     *
     * @param event Changes the Stage to Add Part Menu if unable to open Add Part Menu an alert pops up alerting the
     *              user that system was unable to open up the menu.
     */
    @FXML
    public void OpenAddPartMenu(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/AddPart.fxml"));
            stage.setTitle("Add Part");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        } catch (RuntimeException | IOException runtimeException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Unable to open the Add a Part Menu!");
            a.show();
        }
    }

    /**
     * Opens up the Add Product Menu
     *
     * @param event Changes the Stage to Add Product Menu if unable to open Add Product Menu an alert pops up alerting
     *              the user that system was unable to open up the menu.
     */
    @FXML
    public void OpenAddProductMenu(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../View_Controller/AddProduct.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (RuntimeException | IOException runtimeException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Unable to open the Add a Product Menu!");
            a.show();
        }
    }

    /**
     * Opens Modify Part Menu and completes the form with the data selected
     *
     * @param event Changes the stage to Modify Part Menu if the user has a part selected, if the user does not have
     *              a part selected an alert will alert the user to select the part before being able to proceed.
     */
    @FXML
    public void onActionModifyPart(javafx.event.ActionEvent event) {
        try {
            //Selects Table View Row
            Part part = tblViewPart.getSelectionModel().getSelectedItem();
            if (part == null) {
                Alert partSelection = new Alert(Alert.AlertType.NONE);
                partSelection.setAlertType(Alert.AlertType.ERROR);
                partSelection.setContentText("You must select a part in order to modify!");
                partSelection.show();
                return;
            }

            //Opens Modify Menu
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View_Controller/ModifyPart.fxml"));
            Parent ParentModifyPart = loader.load();
            Scene SceneModifyPart = new Scene(ParentModifyPart);
            ModifyPart PartController = loader.getController();
            PartController.getPart(part);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(SceneModifyPart);
            stage.show();
        } catch (Exception noSelectionModify) {
            Alert noSelection = new Alert(Alert.AlertType.NONE);
            noSelection.setAlertType(Alert.AlertType.ERROR);
            noSelection.setContentText("You must select a part in order to modify!");
            noSelection.show();
        }
    }

    /**
     * An alert pops up alerting the user with a confirmation message. If the user selects okay, the program will close
     * if the user selects cancel, the alert message will close and user is returned to Main Window.
     */
    public void ExitMenu() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to close program?");
        Optional<ButtonType> result = alert.showAndWait();
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
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("You must select a part in order to modify!");
                a.show();
                return;
            }
            //Opens Modify Menu
            FXMLLoader Loader = new FXMLLoader(getClass().getResource("../View_Controller/ModifyProduct.fxml"));
            Loader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = Loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
            ModifyProduct modifyProductController = Loader.getController();
            modifyProductController.selectedProduct(product.getId());
        } catch (RuntimeException | IOException runtimeException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("You must select a part in order to modify!");
            a.show();
        }
    }

    /**
     * Delete the selected Part, if no part is selected an error message pops up alerting the user to select a part
     * if the user selects a part, an alert will appear asking user to confirm deletion.
     */
    @FXML
    void onActionDeletePart() {
        Part part = tblViewPart.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Please select a part.");
            alert.showAndWait();
            return;
        }

        //Popup for Part Confirmation Delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText("Deleting is Permanent");
        alert.setContentText("Are you sure you want to DELETE?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Part " + part.getName() + " has been deleted!");
            deletePart(part.getId());
        } else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error!");
            alert1.setContentText("Unable to delete Part!.");
            alert1.showAndWait();
        }
    }

    /**
     * Delete Selected Part
     *
     * @param id Deletes the part by the id that is supplied in the onActionDeletePart.
     */
    public void deletePart(int id) {
        for (Part Part : Inventory.getAllParts()) {
            if (Part.getId() == id) {
                Part selectedPart = tblViewPart.getSelectionModel().getSelectedItem();
                if (selectedPart != null) {
                    Alert a = new Alert(Alert.AlertType.NONE);
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Error unable to associate part");
                    a.show();
                } else {
                    Inventory.getAllParts().remove(Part);
                }
                return;
            }
        }
    }

    /**
     * Delete the selected Product, if no part is selected an error message pops up alerting the user to select a part
     * if the user selects a Product, an alert will appear asking user to confirm deletion.
     */
    @FXML
    void onActionDeleteProduct() {
        Product product = tblViewProduct.getSelectionModel().getSelectedItem();
        //Popup for Product Confirmation Delete

        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setContentText("Please select a part.");
            alert.showAndWait();
            return;
        }

        //delete product;
        if (product.getAssociatedParts().size() > 0) {
            Alert alertPartAss = new Alert(Alert.AlertType.INFORMATION);
            alertPartAss.setContentText("There is a part Association, please remove the association");
            alertPartAss.showAndWait();
        } else {
            Alert alertConfirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmDelete.setTitle("CONFIRMATION");
            alertConfirmDelete.setHeaderText("Deleting is Permanent");
            alertConfirmDelete.setContentText("Are you sure you want to DELETE?");
            Optional<ButtonType> result = alertConfirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Alert alertDelete = new Alert(Alert.AlertType.INFORMATION);
                alertDelete.setContentText("Part has been deleted.");
                alertDelete.showAndWait();
                deleteProduct(product.getId());
            }
        }
    }

    /**
     * Delete Selected Product
     *
     * @param id Deletes the part by the id that is supplied in the onActionDeleteProduct.
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
}

