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

    private Group group = new Group();

    History(ObservableList<Expression> historyLogs) {
        this.historyLogs = historyLogs;

        Stage historyStage = new Stage();
        historyStage.setTitle("History");
        historyStage.setResizable(false);

        createHistoryView();

        Scene scene = new Scene(group);

        historyStage.setScene(scene);
        historyStage.show();
    }

    private void createHistoryView() {
        TableView<Expression> tableView = new TableView<>();

        TableColumn<Expression, String> expressionTableColumn = new TableColumn<>("Expression");
        expressionTableColumn.setMinWidth(250);
        expressionTableColumn.setResizable(true);
        expressionTableColumn.setCellValueFactory(new PropertyValueFactory<>("exp"));

        TableColumn<Expression, String> resultTableColumn = new TableColumn<>("Result");
        resultTableColumn.setMinWidth(150);
        resultTableColumn.setResizable(true);
        resultTableColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        tableView.setItems(historyLogs);
        tableView.getColumns().addAll(expressionTableColumn,resultTableColumn);

        group.getChildren().add(tableView);
    }
}
