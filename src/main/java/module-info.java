module com.example.shooter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires static lombok;
    requires com.google.gson;
    requires org.apache.logging.log4j;

//    opens com.example.shooter.game.app to javafx.fxml;
    exports com.example.shooter.game.model;
    opens com.example.shooter.game.model to javafx.fxml;
    exports com.example.shooter.game.app;
    opens com.example.shooter.game.app to javafx.fxml;
    opens com.example.shooter.net.model to com.google.gson;
}