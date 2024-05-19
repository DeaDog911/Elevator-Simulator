module org.deadog.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;
    requires java.xml;
    requires java.sql;
    requires java.desktop;


    opens org.deadog.oop to javafx.fxml;
    exports org.deadog.oop;
    exports org.deadog.oop.controllers;
    opens org.deadog.oop.controllers to javafx.fxml;
    opens org.deadog.oop.stages to javafx.fxml;
    exports org.deadog.oop.entities;
    exports org.deadog.oop.utils;
}