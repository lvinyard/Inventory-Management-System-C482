package vinyard.imsvinyard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
 * This class controls the logic in the Add Part Screen
 */
public class IMS_AddPartController implements Initializable {

    @FXML
    public RadioButton inHouseRadio;
    public RadioButton outSourcedRadio;
    public Label inHouseOroutSource;
    public TextField partId;
    public TextField partName;
    public TextField partInv;
    public TextField partCost;
    public TextField partMax;
    public TextField partMin;
    public TextField partinHouseOrOutSource;

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

        //Autogenerate ID
        partId.setText(String.valueOf(Inventory.getPartID()));
    }

    /**
     * This Method is called when Save Button is clicked
     * The Part is saved and added to inventory
     * @param actionEvent
     * @throws IOException
     */
    public void SaveButton(ActionEvent actionEvent) throws IOException {
        //Get All Values
        try {
            String newPartName = partName.getText();
            int newPartInv = Integer.parseInt(partInv.getText());
            double newPartCost = Double.parseDouble(partCost.getText());
            int newPartMax = Integer.parseInt(partMax.getText());
            int newPartMin = Integer.parseInt(partMin.getText());
            String newpartinHouseorOutSource = partinHouseOrOutSource.getText();

            //Validation
            if(newPartName == null) {
                errorLog("Part Name cannot be Null.");
            }else if(newPartMax <= newPartMin){

                errorLog("Part Max must be larger than Min.");

            }else if(newPartInv > newPartMax || newPartInv < newPartMin){

                errorLog("Part Inventory must be between Inventory Max and Min");

            }else{

                //Add part - verify if InHouse or Outsourced
                if(inHouseRadio.isSelected()){

                    int newpartID = Inventory.getPartID();
                    InHousePart newPart = new InHousePart(newpartID,newPartName,newPartCost,newPartInv,newPartMin,newPartMax,Integer.parseInt(newpartinHouseorOutSource));
                    Inventory.addPart(newPart);

                }else {

                    int newpartID = Inventory.getPartID();
                    OutsourcedPart newPart = new OutsourcedPart(newpartID,newPartName,newPartCost,newPartInv,newPartMin,newPartMax,newpartinHouseorOutSource);
                    Inventory.addPart(newPart);
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
     * This method is called when the Cancel button is pressed.
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
     * This method is called when the In House radio button is pressed.
     * @param actionEvent
     */
    public void inHouseRadioAction(ActionEvent actionEvent) {
        inHouseOroutSource.setText("Machine Id");
    }

    /**
     * This method is called when the Out Sourced radio button is pressed.
     * @param actionEvent
     */
    public void outSourcedRadioAction(ActionEvent actionEvent) {
        inHouseOroutSource.setText("Comp. Name");
    }

    /**
     * This method is to handle error messages for the user.
     * @param error
     */
    public void errorLog(String error){
        Alert errorLog = new Alert(Alert.AlertType.ERROR);
        errorLog.setContentText(error);
        errorLog.show();
    }

}