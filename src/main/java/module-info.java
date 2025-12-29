module com.mygymspace.mygymspace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens graphicalcontrollers to javafx.fxml;
    exports start;
}