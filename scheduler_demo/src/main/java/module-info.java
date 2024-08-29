module com.aastu.scheduler {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.aastu.scheduler to javafx.fxml;
    exports com.aastu.scheduler;
    exports com.aastu.scheduler.controller;
    opens com.aastu.scheduler.controller to javafx.fxml;
    exports com.aastu.scheduler.models;
    opens com.aastu.scheduler.models to javafx.fxml;

}