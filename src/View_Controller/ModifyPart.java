package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 *
 * @author Sam Gonzales
 */

public class ModifyPart{
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
     * Cancel Modifying Part
     * @param event Cancels Modifying the part and returns to Main Window
     * @throws IOException If unable to return to Main Window
     */
    public void onActionCancelModifyPart(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene((Parent) scene));
        stage.show();
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
     *
     * @param part Gets data for the Modified Part
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
        }

        else if (part instanceof Outsourced) {
            txtProdMachineCompanyID.setText(((Outsourced) part).getCompanyName());
            rdbOutsourced.setSelected(true);
        }
    }

    /**
     * Saves the data for the modified part
     * @param event Saves the data for the modified part and returns to Main Window
     * @throws IOException If unable to return to Main Window
     */
    @FXML
    public void onActionSavePart(javafx.event.ActionEvent event) throws IOException {
        if (rdbInHouse.isSelected()) {

            //For max and min inventory values in the exception set
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

            //Popup for excessive quantity
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Quantity Min needs to be smaller than Max");
                alert.showAndWait();
                return;
            }
            if (invMax > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Inventory Min needs to be smaller than Max");
                alert.showAndWait();
                return;
            }

            //Returns to Main Window
            Inventory.updatePart(modifiedPart, newPart);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
        else if (rdbOutsourced.isSelected()) {
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Quantity Min needs to be smaller than Max");
                alert.showAndWait();
                return;
            }
            //Error if Inventory is Greater then Max Field
            if (invMax > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setContentText("Inventory Min needs to be smaller than Max");
                alert.showAndWait();
                return;
            }
            Inventory.updatePart(moddedPart, newPart);

            //Returns to Main Window
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("../View_Controller/MainWindow.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();            }
    }
}



