module com.example.finansist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires org.json;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires jdk.httpserver;

    opens com.dz.finansist to javafx.fxml;
    exports com.dz.finansist;
    exports com.dz.finansist.controllers;
    opens com.dz.finansist.controllers to javafx.fxml;

}