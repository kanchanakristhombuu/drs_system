module com.mycompany.disastermanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.disastermanagementsystem to javafx.fxml;
    opens com.mycompany.disastermanagementsystem.controllers to javafx.fxml;
    exports com.mycompany.disastermanagementsystem;
    exports com.mycompany.disastermanagementsystem.controllers;
}
