package application;

import expression.Expression;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

class History {
    private ObservableList<Expression> historyLogs;

    private Stage historyStage = new Stage();

    private Group group = new Group();
    private Scene scene = new Scene(group);


    History(ObservableList<Expression> historyLogs) {
        this.historyLogs = historyLogs;

        historyStage.setTitle("History");
        historyStage.setResizable(false);

        createHistoryView();

        historyStage.setScene(scene);
        historyStage.show();
    }

    private void createHistoryView() {
        TableView<Expression> tableView = new TableView<>();

        TableColumn<Expression, String> expressionTableColumn = new TableColumn<>("Expression");
        expressionTableColumn.setMinWidth(200);
        expressionTableColumn.setResizable(false);
        expressionTableColumn.setCellValueFactory(new PropertyValueFactory<>("exp"));

        TableColumn<Expression, String> resultTableColumn = new TableColumn<>("Result");
        resultTableColumn.setMinWidth(50);
        resultTableColumn.setResizable(false);
        resultTableColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        tableView.setItems(historyLogs);
        tableView.getColumns().addAll(expressionTableColumn,resultTableColumn);

        group.getChildren().add(tableView);
    }
}
