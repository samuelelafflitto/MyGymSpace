module com.mygymspace.mygymspace {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires java.compiler;
    requires javafx.base;

    exports exceptions;
    exports utils.session;
    exports utils;
    exports models.user;
    exports models.training;
    exports models.booking;
    exports models.dailyschedule;
    exports controllers;
    exports beans;
    exports start;
    exports graphicalcontrollers.gui;
    opens graphicalcontrollers.gui to javafx.fxml;
    opens views to javafx.fxml;
    exports models.booking.record;
}