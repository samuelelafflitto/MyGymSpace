module com.mygymspace.mygymspace {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mygymspace.mygymspace to javafx.fxml;
    exports com.mygymspace.mygymspace;
}