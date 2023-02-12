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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import model.*;

/**
 * This class controls the logic for the Main Screen
 */
public class IMS_MainController implements Initializable {
    //Error Label
    @FXML
    public TextField productSearch;
    public TextField partSearch;

    //Part Table
    @FXML
    private TableView<Part> partTable;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInv;
    public TableColumn partCost;


    //Product Table
    @FXML
    private TableView<Product> productTable;
    public TableColumn productId;
    public TableColumn productName;
    public TableColumn productInv;
    public TableColumn productCost;

    /**
     * This method is called on init
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Insert Parts into part table
        partTable.setItems(Inventory.getAllParts());
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Insert Products into product table
        productTable.setItems(Inventory.getAllProducts());
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Method called when the Add button for part is pressed
     * Brings user to the Add Part Screen
     * @param actionEvent
     * @throws IOException
     */
    public void PartAddButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_AddPartForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 404);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is called when the Modify button for Part is pressed
     * Brings user to the Modify Part screen
     * @param actionEvent
     * @throws IOException
     */
    public void PartModifyButton(ActionEvent actionEvent) throws IOException {
        Part modifyPart = partTable.getSelectionModel().getSelectedItem();
        if (modifyPart != null) {

            //Pass Part to be modified to ModifyPart Controller
            IMS_ModifyPartController.getModifyPart(modifyPart);

            //Change Scene to Modify Part Form
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_ModifyPartForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 425, 404);
            stage.setTitle("Modify Part Form");
            stage.setScene(scene);
            stage.show();
        } else {
            errorLog("No part is selected.");
        }
    }

    /**
     * This method is called when the Delete button is pressed for the parts
     * Deletes part from inventory
     * @param actionEvent
     * @throws IOException
     */
    public void PartDeleteButton(ActionEvent actionEvent) throws IOException {
        Part deletePart = partTable.getSelectionModel().getSelectedItem();
        if (deletePart != null) {
            if(ConfirmationLog("You are about to delete part " + deletePart.getName() + ". Continue?")){

                if( Inventory.deletePart(deletePart)){
                }else{
                    errorLog("Cannot delete a Part that is associated with a Product.");
                }
            }

        } else {
            errorLog("No Part is Selected.");
        }
    }

    /**
     * This method is called when the Add button for Products is pressed
     * Brings user to the Add Product Screen
     * @param actionEvent
     * @throws IOException
     */
    public void ProductAddButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_AddProductForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 858, 558);
        stage.setTitle("Add Product Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is called when the Modify button for the Products is pressed
     * Brings user to the Modify Product screen
     * @param actionEvent
     * @throws IOException
     */
    public void ProductModifyButton(ActionEvent actionEvent) throws IOException {
        Product modifyProduct = productTable.getSelectionModel().getSelectedItem();
        if (modifyProduct != null) {
            //Pass Part to be modified to ModifyPart Controller
            IMS_ModifyProductController.getModifyProduct(modifyProduct);

            //Change Scene to Modify Part Form
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_ModifyProductForm.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 858, 558);
            stage.setTitle("Modify Product Form");
            stage.setScene(scene);
            stage.show();
        } else {
            errorLog("Error: No product is selected.");
        }
    }

    /**
     * This method is called when the Delete button is pressed for the Products
     * Deletes selected Product from Inventory
     * @param actionEvent
     * @throws IOException
     */
    public void ProductDeleteButton(ActionEvent actionEvent) throws IOException {
        Product deleteProduct = productTable.getSelectionModel().getSelectedItem();
        if (deleteProduct != null) {
            if(ConfirmationLog("You are about to delete Product " + deleteProduct.getName() + ". Continue?")){

                if(Inventory.deleteProduct(deleteProduct)){

                }else{
                    errorLog("Cannot delete Product that has associated parts. Please remove parts from product.");
                }
            }

        } else {
            errorLog("No Part is Selected.");
        }
    }

    /**
     * This method is called when the Search button is pressed on the Part Table
     * Searches in table for Id and name
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

            partTable.setItems(Inventory.getAllParts());

        }
        partId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * This method is called when the Search button is called for the Product Table
     * Searches in table for Id and Name
     * @param actionEvent
     */
    public void productSearchButton(ActionEvent actionEvent) {

        if(productSearch.getText() != ""){

            String searchString = productSearch.getText();

            ObservableList<Product> combinedSearch = FXCollections.observableArrayList();
            try{
                combinedSearch.addAll(Inventory.lookupProduct(Integer.parseInt(searchString)));
            } catch (Exception e){ }
            combinedSearch.removeAll(Inventory.lookupProduct(searchString));
            combinedSearch.addAll(Inventory.lookupProduct(searchString));

            if(combinedSearch.size() != 0 && !(combinedSearch.contains(null))) {
                productTable.setItems(combinedSearch);
            }else{
                errorLog("No results.");
                productTable.setItems(Inventory.getAllProducts());
                productSearch.setText("");
            }

        }else{

            productTable.setItems(Inventory.getAllProducts());

        }
        productId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));

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
     * This method handles user confirmation
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

    /**
     * Exit button closed Program
     * @param actionEvent
     */
    public void exitButton(ActionEvent actionEvent) {
        System.exit(0);
    }
}
