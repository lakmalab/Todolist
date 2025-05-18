package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class TodoForm {

    @FXML
    private TableColumn<?, ?> colSalary;

    @FXML
    private TableView<?> tblDone;

    @FXML
    private TextField txtadd;

    @FXML
    void addonaction(ActionEvent event) {

    }

    @FXML
    void onrowselected(MouseEvent event) {

    }

}
