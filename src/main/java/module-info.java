module org.deadog.oop {
    requires javafx.controls;
    requires javafx.fxml;
    requires fontawesomefx;


    opens org.deadog.oop to javafx.fxml;
    exports org.deadog.oop;
    exports org.deadog.oop.controllers;
    opens org.deadog.oop.controllers to javafx.fxml;
    exports org.deadog.oop.entities;
    exports org.deadog.oop.utils;
}