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

/**
 * Окно с историей вычислений
 *
 * Данный класс формирует {@code TableView} в котором будут отображаться все вычисления и их результаты.
 * Окно отркывается при нажатии пункта {@code MenuBar} History. Создание всех и заполнение их данными происходит в
 * {@link #createHistoryView()}.
 */
class History {
    private ObservableList<Expression> historyLogs;

    private Group root = new Group();
    private Scene scene;

    /**
     * Конструктор, вызываемый ри нажатии пункта {@code MenuBar} History в {@code MainView}
     *
     * @param main MainView
     * @param historyLogs список операций
     */
    History(Main main, ObservableList<Expression> historyLogs) {
        this.historyLogs = historyLogs;

        Stage historyStage = new Stage();
        historyStage.setTitle("History");
        historyStage.setResizable(false);

        createHistoryView();

        if (main.os.startsWith("Mac")) {
            scene = new Scene(root);
        } else if (main.os.startsWith("Win")) {
            historyStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
            scene = new Scene(root,390,388);
        }

        historyStage.setScene(scene);
        historyStage.show();
    }

    /**
     * Создание и заполнение таблицы данными
     */
    private void createHistoryView() {
        TableView<Expression> tableView = new TableView<>();

        //Колонка Выражений <Класс, Тип поля класса>
        TableColumn<Expression, String> expressionTableColumn = new TableColumn<>("Expression");
        expressionTableColumn.setMinWidth(250);
        expressionTableColumn.setResizable(true);
        //exp - название поля класса
        expressionTableColumn.setCellValueFactory(new PropertyValueFactory<>("exp"));

        //Колонка Результатов <Класс, Тип поля класса>
        TableColumn<Expression, String> resultTableColumn = new TableColumn<>("Result");
        resultTableColumn.setMinWidth(150);
        resultTableColumn.setResizable(true);
        //result - название поля класса
        resultTableColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        //заполнение таблицы
        tableView.getColumns().addAll(expressionTableColumn,resultTableColumn);
        tableView.setItems(historyLogs);

        root.getChildren().add(tableView);
    }
}
