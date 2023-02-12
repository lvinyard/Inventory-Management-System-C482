package vinyard.imsvinyard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;

/**
 * javadoc folder in located in the root of the zip file.
 *
 * FUTURE ENHANCEMENTS
 *
 * One future enhancement I would like to implement into the future is the ability
 * to "double click" a Part/Product in the Main Screen from the table and automatically open that
 * object in the Modify screen.
 *
 * Another future enhancement is to hook the program up to a database so the data will be saved upon exiting and
 * reloading. This enhancement would make the program usable instead of just a demo.
 *
 *
 *  RUNTIME ERROR
 * Index -1 out of bounds for length 1
 * This error I found after deleting a part/product, and then trying to modify another part with an id greater than
 * the previous deleted part/product.
 * This issue was due to how I was using the ID of the part/product as the index of the object to delete in the inventory.
 * For example, if I had parts with ids' 1,2 and 3. When deleting the 1st product, I would delete id - 1 in the list, which would work for the first deletion.
 * Afterwards the list would shrink, part with id 2 would become at index 0, and part id 3 would be index 1. When attempting to delete part id 3, the code would
 * attempt to delete index 2 (3 - 1). I fixed this by when deleting/updating an object, I would search for the index of the object first rather than relying on the object id.
 *
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/vinyard/imsvinyard/IMS_MainForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    private static boolean firstTime = true;

    /**
     * This Method inputs test data into the application at the start of Main.
     */
    private static void addTestData(){
        if(!firstTime){
            return;
        }
        firstTime = false;
        InHousePart r = new InHousePart(Inventory.getPartID(),"O-ring 1/2 in",1.99,10,5,15,14);
        Inventory.addPart(r);
        OutsourcedPart q = new OutsourcedPart(Inventory.getPartID(),"Label Arm",40.99, 3,2,5,"Hobart");
        Inventory.addPart(q);
        InHousePart w = new InHousePart(Inventory.getPartID(),"Timing Belt 32T",17.99,10,2,12,122);
        Inventory.addPart(w);
        OutsourcedPart h = new OutsourcedPart(Inventory.getPartID(),"Rollers",3.99,5,1,10,"Quazar");
        Inventory.addPart(h);

        ObservableList<Part> emptyList = FXCollections.observableArrayList();
        ObservableList<Part> filledList = FXCollections.observableArrayList();
        filledList.add(h);
        filledList.add(w);
        Product p = new Product(Inventory.getProductID(),"Wand Assy", 100.99, 2,2,10,123,emptyList);
        Inventory.addProduct(p);
        Product k = new Product(Inventory.getProductID(),"Label Uptake",139.99,3,1,5,123,filledList);
        Inventory.addProduct(k);
        Product i = new Product(Inventory.getProductID(),"Package Elevator", 179.99,1,1,2,456,emptyList);
        Inventory.addProduct(i);
    }

    /**
     *  Main
     */
    public static void main(String[] args) {

        addTestData();
        launch();

    }
}