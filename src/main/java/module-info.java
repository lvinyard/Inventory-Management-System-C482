module vinyard.imsvinyard {
    requires javafx.controls;
    requires javafx.fxml;


    opens vinyard.imsvinyard to javafx.fxml;
    exports vinyard.imsvinyard;

    exports model;
}