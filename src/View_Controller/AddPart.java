package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

/** Form for creating a new part
 * @author Sam Gonzales
 * <p>
 * RUNTIME ERRORS
 * <br>Put below and explained in detail on every location that an error can occur.
 *
 * <p>FUTURE IMPROVEMENTS<br>
 * <br>Should combine the Add Part and Modify parts menus making for a streamlined experience and shrinking the size of the
 * overall program.
 * <br>The ability to add multiple parts at once
 * <br>The ability to view all parts currently existing
 * <br>Drop Down menu for the machine code already added to add more that match.
 * <br>Drop down menu for companies already added to quickly add more.
 * <br>Live editing that will not allow the user to type anything in the inventory field that is larger than Max
 * <br>Live editing that will not allow for the user to type anything in the Min field that is larger than Max</p>
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
        this.lblMachineText.setText(rdbOutsourced.isSelected() ? "Company Name" : "MachineID");
    }

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
    @FXML
    public void partCancel(javafx.event.ActionEvent event) {
        try {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setContentText("Are you sure you want to quit adding a part");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                returnToMain(event);
            }
        } catch (Exception windowNotFound) {
            filledOutForm("Unable to find the Main Window Form.");
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
     *              Error checking happens if the user does not fill in the form to completion.
     *              All errors alert the user to correct the form before continuing.
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

            //Error if Inv is Less then Zero
            if (stock < 0) {
                alertMethod("Inventory needs to be greater than 0.");
                return;
            }

            //Error if Min is Greater then Max field
            if (min > max) {
                alertMethod("Quantity Min needs to be smaller than Max");
                return;
            }
            //Error if Inventory is Greater then Max Field
            if (stock > max) {
                alertMethod("Inventory needs to be smaller than Max");
                return;
            }

            //Checks what radio button is selected
            if (rdbOutsourced.isSelected()) {
                String companyName = txtPartMachineCompanyID.getText();
                Inventory.addPart(new Outsourced(name, id, price, stock, min, max, companyName));
            }
            int machineId = Integer.parseInt(txtPartMachineCompanyID.getText());
            Inventory.addPart(new InHouse(name, id, price, stock, min, max, machineId));

            //Returns to Main Window
            returnToMain(event);
        } catch (Exception incompleteForm) {
            filledOutForm("Form must be filled out completely!");
        }
    }

    private void alertMethod(String s) {
        Alert minGreaterMax = new Alert(Alert.AlertType.ERROR);
        minGreaterMax.setTitle("Error!");
        minGreaterMax.setContentText(s);
        minGreaterMax.showAndWait();
    }

    private void filledOutForm(String s) {
        Alert formIncomplete = new Alert(Alert.AlertType.NONE);
        formIncomplete.setAlertType(Alert.AlertType.ERROR);
        formIncomplete.setContentText(s);
        formIncomplete.show();
    }

    private void returnToMain(ActionEvent event) throws java.io.IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}

