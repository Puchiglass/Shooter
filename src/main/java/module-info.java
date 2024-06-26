open module com.example.shooter {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires org.postgresql.jdbc;
    requires java.sql;
    requires java.persistence;
    requires java.naming;
    requires static lombok;


    exports com.example.shooter;
    exports com.example.shooter.server;
    exports com.example.shooter.messages;
    exports com.example.shooter.client;
    exports com.example.shooter.messages.MsgData;
    exports com.example.shooter.client.visual;
}