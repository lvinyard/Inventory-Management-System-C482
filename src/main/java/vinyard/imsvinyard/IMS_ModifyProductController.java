package vinyard.imsvinyard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class handles all of the logic for the Modify Product screen
 */
public class IMS_ModifyProductController implements Initializable {

    private static Product modifyProduct = null;

    @FXML
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
     * This method handles passing the Product being modified from the main controller
     * @param product
     */
    public static void getModifyProduct(Product product){
        modifyProduct = product;
    }

    /**
     * This method is called on init
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set text fields from ModifyPart
        String ModifyId = String.valueOf(modifyProduct.getId());

        productId.setText(ModifyId);
        productName.setText(modifyProduct.getName());
        productInv.setText(String.valueOf(modifyProduct.getStock()));
        productCost.setText(String.valueOf(modifyProduct.getPrice()));
        productMax.setText(String.valueOf(modifyProduct.getMax()));
        productMin.setText(String.valueOf(modifyProduct.getMin()));
        productMachineId.setText(String.valueOf(modifyProduct.getMachineid()));
        associatedParts.addAll(modifyProduct.getPartAssociations());

        //Update associated part table
        associatedPartTable.setItems(associatedParts);
        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Update Part table
        partTable.setItems(Inventory.getAllParts());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    /**
     * This method is called when the Canceled button is pressed
     * Returns user to the main screen
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
     * This method is called when the Search Button is called for the parts
     * Searches for parts via Id and Name
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
     * This method is called when the Remove from Product button is pressed
     * The part is unassociated from the product
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
     * This method is called when the Add button is pressed
     * The part is added to the Product
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
     * This method is called when the Save button is pressed
     * The Product is updated and saved into the same index in the Inventory
     * @param actionEvent
     */
    public void SaveButton(ActionEvent actionEvent) {
        //Get All Values
        try {
            int updateProductId = Integer.parseInt(productId.getText());
            String updateProductName = productName.getText();
            int updateProductInv = Integer.parseInt(productInv.getText());
            double updateProductCost = Double.parseDouble(productCost.getText());
            int updateProductMax = Integer.parseInt(productMax.getText());
            int updateProductMin = Integer.parseInt(productMin.getText());
            int updateProductMachineId = Integer.parseInt(productMachineId.getText());

            //Validation
            if(updateProductName == null) {
                errorLog("Product Name cannot be Null.");
            }else if(updateProductMax <= updateProductMin){

                errorLog("Product Max must be larger than Min.");

            }else if(updateProductInv > updateProductMax || updateProductInv < updateProductMin){

                errorLog("Product Inventory must be between Inventory Max and Min");

            }else{

                Product updateProduct = new Product(updateProductId,updateProductName,updateProductCost,updateProductInv,updateProductMin,updateProductMax, updateProductMachineId, associatedParts);
                Inventory.updateProduct((Inventory.getAllProducts().indexOf(modifyProduct)), updateProduct);

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
     * Handles errors for the user
     * @param error
     */
    public void errorLog(String error){
        Alert errorLog = new Alert(Alert.AlertType.ERROR);
        errorLog.setContentText(error);
        errorLog.show();
    }

    /**
     * Handles Confirmation for the user
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