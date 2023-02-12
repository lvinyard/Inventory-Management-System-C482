package vinyard.imsvinyard;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class handles all of the logic for the Modify part screen
 */
public class IMS_ModifyPartController implements Initializable {

    private static Part modifyPart = null;
    public RadioButton inHouseRadio;
    public RadioButton outSourcedRadio;
    public Label inHouseOroutSource;
    public TextField partId;
    public TextField partinHouseOrOutSource;
    public TextField partMin;
    public TextField partMax;
    public TextField partInv;
    public TextField partCost;
    public TextField partName;

    /**
     * This method is used to pass the part being modified from the main controller
     * @param part
     */
    public static void getModifyPart(Part part){
        modifyPart = part;
    }

    /**
     * This method is called on init
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Create toggle group for Radio Buttons
        ToggleGroup group = new ToggleGroup();
        inHouseRadio.setToggleGroup(group);
        outSourcedRadio.setToggleGroup(group);

        //Set text fields from ModifyPart
        String ModifyId = String.valueOf(modifyPart.getId());

        partId.setText(ModifyId);
        partName.setText(modifyPart.getName());
        partInv.setText(String.valueOf(modifyPart.getStock()));
        partCost.setText(String.valueOf(modifyPart.getPrice()));
        partMax.setText(String.valueOf(modifyPart.getMax()));
        partMin.setText(String.valueOf(modifyPart.getMin()));
        if(modifyPart instanceof InHousePart) {

            inHouseRadio.setSelected(true);
            partinHouseOrOutSource.setText(String.valueOf(((InHousePart) modifyPart).getMachineId()));
            inHouseOroutSource.setText("Machine Id");

        }else if(modifyPart instanceof OutsourcedPart) {

            outSourcedRadio.setSelected(true);
            partinHouseOrOutSource.setText(((OutsourcedPart) modifyPart).getCompanyName());
            inHouseOroutSource.setText("Comp. Name");
        }

    }

    /**
     * This method is called when the cancel button is pressed
     * Brings user to the main screen
     * @param actionEvent
     * @throws IOException
     */
    public void CancelButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is called when the In House Radio button is pressed
     * @param actionEvent
     */
    public void inHouseRadioAction(ActionEvent actionEvent) {
        inHouseOroutSource.setText("Machine Id");
        partinHouseOrOutSource.setText("");
    }

    /**
     * This method is called when the Out Sourced Radio button is pressed
     * @param actionEvent
     */
    public void outSourcedRadioAction(ActionEvent actionEvent) {
        inHouseOroutSource.setText("Comp. Name");
        partinHouseOrOutSource.setText("");
    }

    /**
     * This method is called when the Save button is pressed
     * Part will be updated and saved to the same index of the Part inventory
     * @param actionEvent
     */
    public void SaveButton(ActionEvent actionEvent) {
        //Get All Values
        try {
            int updatepartID = Integer.parseInt(partId.getText());
            String updatePartName = partName.getText();
            int updatePartInv = Integer.parseInt(partInv.getText());
            double updatePartCost = Double.parseDouble(partCost.getText());
            int updatePartMax = Integer.parseInt(partMax.getText());
            int updatePartMin = Integer.parseInt(partMin.getText());
            String updatepartinHouseorOutSource = partinHouseOrOutSource.getText();

            //Validation
            if(updatePartName == null) {
                errorLog("Part Name cannot be Null.");
            }else if(updatePartMax <= updatePartMin){

                errorLog("Part Max must be larger than Min.");

            }else if(updatePartInv > updatePartMax || updatePartInv < updatePartMin){

                errorLog("Part Inventory must be between Inventory Max and Min");

            }else{

                //Add part - verify if InHouse or Outsourced
                if(inHouseRadio.isSelected()){

                    InHousePart updatedPart = new InHousePart(updatepartID,updatePartName,updatePartCost,updatePartInv,updatePartMin,updatePartMax,Integer.parseInt(updatepartinHouseorOutSource));
                    Inventory.updatePart((Inventory.getAllParts().indexOf(modifyPart)),updatedPart);

                }else {

                    OutsourcedPart updatedPart = new OutsourcedPart(updatepartID,updatePartName,updatePartCost,updatePartInv,updatePartMin,updatePartMax,updatepartinHouseorOutSource);
                    Inventory.updatePart((Inventory.getAllParts().indexOf(modifyPart)),updatedPart);
                }

                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_MainForm.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 400);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();
            }

            //Catch exception when trying to save.
        } catch(Exception e){
            errorLog("Error with input variables.");
        }

    }

    /**
     * This method handles showing errors to the user
     * @param error
     */
    public void errorLog(String error){
        Alert errorLog = new Alert(Alert.AlertType.ERROR);
        errorLog.setContentText(error);
        errorLog.show();
    }
}