package vinyard.imsvinyard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is in controls the logic in the Add Product screen
 */
public class IMS_AddProductController implements Initializable {


    public TextField productId;
    public TextField productName;
    public TextField productInv;
    public TextField productCost;
    public TextField productMax;
    public TextField productMin;
    public TextField productMachineId;
    public TableView partTable;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInv;
    public TableColumn partCost;
    public TextField partSearch;
    public TableView associatedPartTable;
    public TableColumn assocPartId;
    public TableColumn assocPartName;
    public TableColumn assocPartInv;
    public TableColumn assocPartCost;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * This method is called on init
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Autogenerate ID
        productId.setText(String.valueOf(Inventory.getProductID()));

        //Insert Parts into part table
        partTable.setItems(Inventory.getAllParts());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method is called when the Cancel Button is called
     * Brings user back to main screen
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
     * This method is called when the Save Button is called
     * The product is created and added to the inventory
     * @param actionEvent
     */
    public void SaveButton(ActionEvent actionEvent) {
        //Get All Values
        try {
            int newProductId = Integer.parseInt(productId.getText());
            String newProductName = productName.getText();
            int newProductInv = Integer.parseInt(productInv.getText());
            double newProductCost = Double.parseDouble(productCost.getText());
            int newProductMax = Integer.parseInt(productMax.getText());
            int newProductMin = Integer.parseInt(productMin.getText());
            int newProductMachineId = Integer.parseInt(productMachineId.getText());

            //Validation
            if(newProductName == null) {
                errorLog("Product Name cannot be Null.");
            }else if(newProductMax <= newProductMin){

                errorLog("Product Max must be larger than Min.");

            }else if(newProductInv > newProductMax || newProductInv < newProductMin){

                errorLog("Product Inventory must be between Inventory Max and Min");

            }else{

                Product newProduct = new Product(newProductId,newProductName,newProductCost,newProductInv,newProductMin,newProductMax, newProductMachineId, associatedParts);
                Inventory.addProduct(newProduct);

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
     * This method is called when the Add button is called
     * The part selected in the table is added to the part associations of the Product
     * @param actionEvent
     */
    public void partAddtoProduct(ActionEvent actionEvent) {

        Part addPart = (Part) partTable.getSelectionModel().getSelectedItem();

        if (addPart != null) {

            if(!(associatedParts.contains(addPart))){

                //Add part to associated part list
                associatedParts.add(addPart);

                //Update associated part table
                associatedPartTable.setItems(associatedParts);
                assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
                assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
                assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
                assocPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

                partSearch.setText("");

            }

        } else {
            errorLog("Nothing is selected.");
        }

    }

    /**
     * This method is called when the Remove button is called
     * The selected Part in the table is unassociated from the Product
     * @param actionEvent
     */
    public void partRemovefromProduct(ActionEvent actionEvent) {

        Part removePart = (Part) associatedPartTable.getSelectionModel().getSelectedItem();

        if(removePart != null) {

            if (ConfirmationLog("Are you sure you want to remove this part?")) {
                associatedParts.remove(removePart);

                associatedPartTable.setItems(associatedParts);
                assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
                assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
                assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
                assocPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

            }

        } else {
            errorLog("Nothing is Selected.");
        }
    }

    /**
     * This method is called when the Search Button is pressed
     * The part table is searched by Id and Part Name
     * @param actionEvent
     */
    public void partSearchButton(ActionEvent actionEvent) {
        if(partSearch.getText() != ""){

            String searchString = partSearch.getText();

            ObservableList<Part> combinedSearch = FXCollections.observableArrayList();
            try{
                combinedSearch.addAll(Inventory.lookupPart(Integer.parseInt(searchString)));
            } catch (Exception e){ }
            combinedSearch.removeAll(Inventory.lookupPart(searchString));
            combinedSearch.addAll(Inventory.lookupPart(searchString));
            if(combinedSearch.size() != 0 && !(combinedSearch.contains(null))) {
                partTable.setItems(combinedSearch);
            }else{
                errorLog("No results.");
                partTable.setItems(Inventory.getAllParts());
                partSearch.setText("");
            }

        }else{

            ObservableList<Part> combinedSearch = FXCollections.observableArrayList();
            combinedSearch.addAll(Inventory.getAllParts());
            partTable.setItems(combinedSearch);

        }
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method handles presenting errors to the user
     * @param error
     */
    public void errorLog(String error){
        Alert errorLog = new Alert(Alert.AlertType.ERROR);
        errorLog.setContentText(error);
        errorLog.show();
    }

    /**
     * This method handles the actions that require Confirmation
     * @param error
     * @return
     */
    public boolean ConfirmationLog(String error){
        Alert confirmationLog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationLog.setContentText(error);
        Optional<ButtonType> result = confirmationLog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        }else{
            return false;
        }
    }
}