module com.mygymspace.mygymspace {
    requires javafx.controls;
    requires javafx.fxml;


    opens graphicalcontrollers to javafx.fxml;
    exports start;
}