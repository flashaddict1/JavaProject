package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/** Form for Modifying a part
 * @author Sam Gonzales
 * <p>
 * RUNTIME ERRORS
 * <br>Put below and explained in detail on every location that an error can occur.
 *
 * <br>FUTURE IMPROVEMENTS<br>
 * <br>Should combine the Add Part and Modify parts menus making for a streamlined experience and shrinking the size of the
 * overall program.
 * <br>The ability to add multiple parts at once
 * <br>The ability to view all parts currently existing
 * <br>Drop Down menu for the machine code already added to add more that match.
 * <br>Drop down menu for companies already added to quickly add more.
 * <br>Live editing that will not allow the user to type anything in the inventory field that is larger than Max
 * <br>Live editing that will not allow for the user to type anything in the Min field that is larger than Max</p>
 */

public class ModifyPart {
    private Part modifyPart;

    @FXML
    private RadioButton rdbOutsourced;
    @FXML
    private RadioButton rdbInHouse;
    @FXML
    private TextField txtModPartId;
    @FXML
    private TextField txtModPartName;
    @FXML
    private TextField txtModPartInv;
    @FXML
    private TextField txtModPartPrice;
    @FXML
    private TextField txtModPartMin;
    @FXML
    private TextField txtProdMachineCompanyID;
    @FXML
    private TextField txtModPartMax;
    @FXML
    private Label lblMachineText;

    /**
     * Canceling Modifying a part
     *
     * @param event Confirms that the user wants to cancel Modifying of a new part. Cancels the Modifying of a part.
     *              Returns to the Main Window and clears out the information already entered into the form.
     *              <p>
     *              Error Exception happens when the Main window is not able to be loaded.
     *              This causes the application to stall and not proceed any farther as there is no window for the user
     *              to return to.
     *              This is a fatal error.
     */
    public void onActionCancelModifyPart(javafx.event.ActionEvent event) {
        try {
            switchWindows(event);
        } catch (Exception modifyPartError) {
            alertWindow("Unable to find the Main Window");
        }
    }



    /**
     * Set Label to either MachineID or Company Name
     */
    @FXML
    public void SetMachineLbl() {
        if (rdbOutsourced.isSelected())
            this.lblMachineText.setText("Company Name");
        else
            this.lblMachineText.setText("MachineID");
    }

    /**
     * @param part Gets the data for the part being modified, matches it up with the ID part specified.
     *             Matches it to the radio button as well if the part is Machine or Outsourced.
     */
    public void getPart(Part part) {
        modifyPart = part;
        txtModPartId.setText(String.valueOf(part.getId()));
        txtModPartName.setText(part.getName());
        txtModPartInv.setText(String.valueOf(part.getStock()));
        txtModPartPrice.setText(String.valueOf(part.getPrice()));
        txtModPartMax.setText(String.valueOf(part.getMax()));
        txtModPartMin.setText(String.valueOf(part.getMin()));

        if (part instanceof InHouse) {
            txtProdMachineCompanyID.setText(String.valueOf(((InHouse) part).getMachineId()));
            lblMachineText.setText("Machine ID");
            rdbInHouse.setSelected(true);
        } else if (part instanceof Outsourced) {
            txtProdMachineCompanyID.setText(((Outsourced) part).getCompanyName());
            rdbOutsourced.setSelected(true);
        }
    }

    /**
     * Saves the part
     *
     * @param event Saves the Part that the user is Modifying. Takes the ID, Name, Inventory, Min, and Max from the form
     *              and saves it. After the part is saved into memory, the user is then brought into the Main Window.
     *              <p>
     *              Error checking happens if the user tries to enter in a Min amount that is greater then Max.
     *              Error checking happens if the user tries to enter in an Inv amount that is greater than Max.
     *              Error checking happens if the user does not fill in the form to completion alerting the user
     *              to finish editing the form.
     *              All errors alert the user to correct the form before continuing.
     */
    @FXML
    public void onActionSavePart(javafx.event.ActionEvent event) {
        try {
            if (rdbInHouse.isSelected()) {
                int max = Integer.parseInt(txtModPartMax.getText());
                int min = Integer.parseInt(txtModPartMin.getText());
                int invMax = Integer.parseInt(txtModPartInv.getText());

                int modifiedPart = Inventory.lookupPartIndex(modifyPart.getId());
                Part newPart = new InHouse(
                        txtModPartName.getText(),
                        modifyPart.getId(),
                        Double.parseDouble(txtModPartPrice.getText()),
                        Integer.parseInt(txtModPartInv.getText()),
                        Integer.parseInt(txtModPartMin.getText()),
                        Integer.parseInt(txtModPartMax.getText()),
                        Integer.parseInt(txtProdMachineCompanyID.getText())
                );

                //Error if Inv is Less then Zero
                if (invMax < 0) {
                    alertWindow("Inventory needs to be greater than 0.");
                    return;
                }

                //Error if Min is Greater then Max field
                if (min > max) {
                    alertWindow("Quantity Min needs to be smaller than Max");
                    return;
                }
                //Error if Inventory is Greater then Max Field
                if (invMax > max) {
                    alertWindow("Inventory needs to be smaller than Max");
                    return;
                }

                //Returns to Main Window
                Inventory.updatePart(modifiedPart, newPart);
                switchWindows(event);
            } else if (rdbOutsourced.isSelected()) {
                int max = Integer.parseInt(txtModPartMax.getText());
                int min = Integer.parseInt(txtModPartMin.getText());
                int invMax = Integer.parseInt(txtModPartInv.getText());
                int moddedPart = Inventory.lookupPartIndex(modifyPart.getId());
                Part newPart = new Outsourced(
                        txtModPartName.getText(),
                        modifyPart.getId(),
                        Double.parseDouble(txtModPartPrice.getText()),
                        Integer.parseInt(txtModPartInv.getText()),
                        Integer.parseInt(txtModPartMin.getText()),
                        Integer.parseInt(txtModPartMax.getText()),
                        txtProdMachineCompanyID.getText()
                );

                //Error if Min is Greater then Max field
                if (min > max) {
                    alertWindow("Quantity Min needs to be smaller than Max");
                    return;
                }
                //Error if Inventory is Greater then Max Field
                if (invMax > max) {
                    alertWindow("Inventory needs to be smaller than Max");
                    return;
                }

                //Returns to Main Window
                Inventory.updatePart(moddedPart, newPart);
                switchWindows(event);
            }
        } catch (Exception addPartError) {
            alertWindow("Form must be filled out completely!");
        }
    }

    private void alertWindow(String s) {
        Alert addPartAlert = new Alert(Alert.AlertType.NONE);
        addPartAlert.setAlertType(Alert.AlertType.ERROR);
        addPartAlert.setContentText(s);
        addPartAlert.showAndWait();
    }

    private void switchWindows(ActionEvent event) throws java.io.IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Main Window");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

}



