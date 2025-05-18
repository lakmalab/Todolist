package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.TodoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static java.lang.Double.parseDouble;

public class TodoForm {
    public TableColumn coldone;
    LocalDate currentDate = LocalDate.now();
    public ListView todoListView;
    public TextField txtDescription;
    public TextField txtTitle;
    public TableColumn colStat;
    public DatePicker dateTime;
    public void initialize() {
        dateTime.setValue(LocalDate.now());
        loadItems();
        loadTable();
    }
    @FXML
    private TableColumn<?, ?> colSalary;
    ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();
    ObservableList<TodoItem> doneList = FXCollections.observableArrayList();
    @FXML
    public TableView<TodoItem> tblDone;


    @FXML

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
        loadTable();
    }

    private boolean addTodoItem() throws SQLException {
        LocalDate selectedDate = dateTime.getValue();
        TodoItem newitem = new TodoItem(
                txtTitle.getText(),
                txtDescription.getText(),
                selectedDate.toString(),
                false
        );
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO active_tasks (task_title, task_description,created_at) VALUES (?, ?,?)");
        stm.setObject(1, newitem.getTitle());
        stm.setObject(2, newitem.getDescription());
        stm.setObject(3, newitem.getCompletiontime());
        int res = stm.executeUpdate();
        return res >0;
    }
    private boolean addTodoDoneItem(String title ,String description ) throws SQLException {
        TodoItem newitem = new TodoItem(
                title,
                description,
                currentDate.toString(),
                Boolean.TRUE
        );
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("INSERT INTO completed_tasks  (task_title, task_description) VALUES (?, ?)");
        stm.setObject(1, newitem.getTitle());
        stm.setObject(2, newitem.getDescription());
        int res = stm.executeUpdate();
        return res >0;
    }
    private void loadTable(){
        try {
            doneList.clear();
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * From completed_tasks");
            while(resultSet.next()){
                doneList.add(new TodoItem(
                        resultSet.getString(2),
                        resultSet.getString(4),
                        currentDate.toString(),
                        true));
                coldone.setCellValueFactory((new PropertyValueFactory<>("title")));
                colStat.setCellValueFactory((new PropertyValueFactory<>("description")));
                ObservableList<TodoItem> TodoItemdoneObservableArray= FXCollections.observableArrayList();
                doneList.forEach(TodoItem -> {TodoItemdoneObservableArray.add(TodoItem);});
                tblDone.setItems(TodoItemdoneObservableArray);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void loadItems(){
        todoItems.clear();
        try {
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * From active_tasks");
            while(resultSet.next()){
                todoItems.add(new TodoItem(resultSet.getString("task_title"), resultSet.getString("task_description"),resultSet.getString("created_at"), false));
            }

            todoListView.setItems(todoItems);
            todoListView.setCellFactory(param -> new ListCell<TodoItem>() {
                @Override
                protected void updateItem(TodoItem item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getTitle() == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label titleLabel = new Label(item.getTitle() );
                        Label detail = new Label( item.getCompletiontime() != null
                                ? item.getCompletiontime()
                                : "Undefined");
                        VBox layout = new VBox(titleLabel,detail);

                        CheckBox checkBox = new CheckBox();
                        checkBox.setSelected(item.getIsdone());

                        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected && item != null) {
                                try {
                                    addTodoDoneItem(item.getTitle(), item.getDescription());
                                    deleteItem(item);
                                    loadItems();
                                    loadTable();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });

                        HBox hBox = new HBox(10, checkBox, layout );
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.setHgrow(layout, Priority.ALWAYS);

                        setText(null);
                        setGraphic(hBox);
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void onrowselected(MouseEvent event) {

    }
    private boolean deleteItem(TodoItem selectedItem) throws SQLException {
        if (selectedItem == null) return false;
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("DELETE FROM active_tasks WHERE task_title = ?");
        stm.setObject(1, selectedItem.getTitle());
        int res = stm.executeUpdate();
        return res >0;
    }

    public void itemselected(MouseEvent mouseEvent) {
        TodoItem selectedCustomer = (TodoItem) todoListView.getSelectionModel().getSelectedItem();
        alert.setTitle("Description");
        alert.setHeaderText(selectedCustomer.getTitle());
        alert.setContentText(selectedCustomer.getDescription());
        alert.showAndWait();
    }
}
