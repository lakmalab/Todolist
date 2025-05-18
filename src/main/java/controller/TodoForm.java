package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.TodoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Double.parseDouble;

public class TodoForm {

    public TableColumn coldone;
    public ListView todoListView;
    public TextField txtDescription;
    public TextField txtTitle;
    @FXML
    private TableColumn<?, ?> colSalary;
    ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();
    @FXML
    private TableView<?> tblDone;

    @FXML

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    ListView listView = new ListView();
    @FXML
    void addonaction(ActionEvent event) throws SQLException {
        if (addTodoItem()==true) {
            alert.setTitle("Information");
            alert.setHeaderText("Item Added Succesfully");
            alert.showAndWait();
        }else {
            alert.setTitle("Information");
            alert.setHeaderText("Failed to add the Item.....");
            alert.showAndWait();
        }
        loadItems();
    }

    private boolean addTodoItem() throws SQLException {
        TodoItem newitem = new TodoItem(
                txtTitle.getText(),
                txtDescription.getText(),
                false
        );
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO active_tasks (task_title, task_description) VALUES (?, ?)");
        stm.setObject(1, newitem.getTitle());
        stm.setObject(2, newitem.getDescription());
        int res = stm.executeUpdate();
        return res >0;
    }

    private void loadItems(){
        try {
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * From active_tasks");
            while(resultSet.next()){
                todoItems.add(new TodoItem(resultSet.getString("task_title"), resultSet.getString("task_description"), false));
            }
            todoListView.setItems(todoItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void onrowselected(MouseEvent event) {

    }

}
