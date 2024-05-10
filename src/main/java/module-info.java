module org.deadog.oop {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.deadog.oop to javafx.fxml;
    exports org.deadog.oop;
}