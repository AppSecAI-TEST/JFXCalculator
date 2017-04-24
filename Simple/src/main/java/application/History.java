package application;

import expression.Expression;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

class History {
    private ObservableList<Expression> historyLogs;

    private Group root = new Group();
    private Scene scene;

    History(Main main, ObservableList<Expression> historyLogs) {
        this.historyLogs = historyLogs;

        Stage historyStage = new Stage();
        historyStage.setTitle("History");
        historyStage.setResizable(false);

        createHistoryView();

        if (main.os.startsWith("Mac")) {
            scene = new Scene(root);
        } else if (main.os.startsWith("Win")) {
            historyStage.getIcons().add(new Image("https://psv4.userapi.com/c810132/u212633253/docs/3a8d2f3adffc/icon.png"));
            scene = new Scene(root,390,388);
        }

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

        root.getChildren().add(tableView);
    }
}
