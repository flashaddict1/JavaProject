package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author Sam Gonzales
 */

public class AddPart{

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
     * Changes Label Depending on Radio Button
     */
    @FXML
    public void SetMachineLbl() {
        if (rdbOutsourced.isSelected())
            this.lblMachineText.setText("Company Name");
        else
            this.lblMachineText.setText("MachineID");
    }

    /**
     * Cancel Adding Part
      * @param event Cancel Adding Part and returns to Main Window
     * @throws IOException If Unable to return to Main Window
     */
    @FXML
    public void partCancel(javafx.event.ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Save Adding Part
     * @param event Saves Adding Part to Inventory and returns to Main Window
     * @throws IOException If unable to return to Main Window
     */
    @FXML
    public void onActionSave(javafx.event.ActionEvent event) throws IOException {
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
    }

}

