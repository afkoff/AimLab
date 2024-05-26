module com.nicepeopleproject.aim_labfxapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.nicepeopleproject.aim_labfxapp to javafx.fxml;
    exports com.nicepeopleproject.aim_labfxapp;
}