package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Sam Gonzales
 * Should combine the Add Part and Modify parts menus making for a streamlined experience and shrinking the size of the
 * overall program. Possibly adding in functionallilty to add multiple parts at once.
 */

public class AddPart {

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton rdbOutsourced;
    @FXML
    private Label lblMachineText;
    @FXML
    private TextField txtPartName;
    @FXML
    private TextField txtPartInventory;
    @FXML
    private TextField txtPartPrice;
    @FXML
    private TextField txtPartMax;
    @FXML
    private TextField txtPartMachineCompanyID;
    @FXML
    private TextField txtPartMin;

    /**
     * Changes Label Depending on Radio Button, options are Company Name and Machine ID
     */
    @FXML
    public void SetMachineLbl() {
        if (rdbOutsourced.isSelected())
            this.lblMachineText.setText("Company Name");
        else
            this.lblMachineText.setText("MachineID");
    }

    /**
     * Canceling adding a part
     *
     * @param event Cancels adding the part, Alerts the user to confirm canceling the part. Alerts the user if program
     *              is unable to return to main window.
     */
    @FXML
    public void partCancel(javafx.event.ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to quit adding a part");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (RuntimeException | IOException runtimeException) {
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Unable to find the Main Window");
            a.show();
        }
    }

    /**
     * Saves the part
     * @param event Saves the part and uses the details provided by the user, if the user enters an invalid character,
     *              it will alert the user if they do not create the part with alphanumeric charters.
     */
    @FXML
    public void onActionSave(javafx.event.ActionEvent event) {
        try {
            int id = Inventory.getAllParts().size() + 1;
            String name = txtPartName.getText();
            double price = Double.parseDouble(txtPartPrice.getText());
            int stock = Integer.parseInt(txtPartInventory.getText());
            int min = Integer.parseInt(txtPartMin.getText());
            int max = Integer.parseInt(txtPartMax.getText());

            //Checks what radio button is selected
            if (rdbOutsourced.isSelected()) {
                String companyName = txtPartMachineCompanyID.getText();
                Inventory.addPart(new Outsourced(name, id, price, stock, min, max, companyName));
            } else {
                int machineId = Integer.parseInt(txtPartMachineCompanyID.getText());
                Inventory.addPart(new InHouse(name, id, price, stock, min, max, machineId));
            }

            //Returns to Main Window
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NumberFormatException | IOException numberFormatException) {
            numberFormatException.printStackTrace();
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Invalid Character! Part must only contain AlphaNumeric characters!");
            a.show();
        }
    }
}

