package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.awt.Toolkit;

import control.MainViewController;
import expression.Expression;

/**
 * Главное окно приложения
 *
 * Класс является главным окном калькулятора, содержащее {@code MenuBar}
 * и основные элементы управления калькулятором: {@code AnchorPane(TextArea,Grid)}
 */
public class Main extends Application {

    final String os = System.getProperty("os.name");

    private Stage primaryStage;
    private BorderPane root = new BorderPane();

    private Scene scene;

    //История вычислений
    private ObservableList<Expression> historyLogs =  FXCollections.observableArrayList();

    public void addHistory(Expression expression) {
        historyLogs.add(expression);
    }

    /**
     * Точка входа в JavaFx приложение
     *
     * Метод загружает основные элементы приложения и их controller'ы
     * {@link #createMainView()}, {@link #createMenuBar()}
     *
     * @param primaryStage Окно
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JFXCalculator");

        //Загрузка иконки приложения
        try {
            if (os.startsWith("Mac")) {
                java.awt.Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
                com.apple.eawt.Application.getApplication().setDockIconImage(image);
            } else if (os.startsWith("Win")) {
                this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("[no dock icon found]");
        }

        createMainView();
        createMenuBar();

        //Размеры Scene в зависимости от OS
        if (os.startsWith("Mac")) {
            scene = new Scene(root);
        } else if (os.startsWith("Win")) {
            scene = new Scene(root,227,423);
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        //Закрыть остальные окна по закрытию главного
        primaryStage.setOnCloseRequest(event -> Platform.exit());
    }

    /**
     * Загрузка основных элементов калькулятора
     *
     * Стиль и расположение элементов в {@code AnchorPane} загружается с {@code fxml} файла.
     * Функционал передается {@code MainViewController}.
     */
    private void createMainView() {
        try {
            //Загрузка Fxml файла(отображение и логика)
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainView.fxml"));

            AnchorPane anchorPane = loader.load();

            MainViewController controller = loader.getController();
            controller.setDisplay(this);

            root.setCenter(anchorPane);

        } catch (IOException | IllegalStateException e) {
            System.err.println(e.getMessage());
            System.err.println("[no MainView.fxml file found]");
            System.exit(-1);
        }
    }

    /**
     * Загрузка {@code MenuBar}
     *
     * Загурзка меню-бара и добавление пунктов, подпунктов и действий по нажатию на них
     */
    private void createMenuBar() {

        //Пункт на панели меню
        Menu menu = new Menu("Menu");

        //Подпункты пункта Menu и что делать по их нажатию
        MenuItem history = new MenuItem("History");
        history.setOnAction(event -> new History(this,historyLogs));

        MenuItem close = new MenuItem("Close");
        close.setOnAction(event -> System.exit(0));

        //Сбор подпунктов в пункт
        menu.getItems().addAll(history, close);

        //Создание MenuBar и добавление в него пунтка
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        menuBar.getMenus().add(menu);

        if (os.startsWith("Mac")) {
            menuBar.useSystemMenuBarProperty().set(true);
        } else if (os.startsWith("Win")) {
            menuBar.setPrefHeight(30);
        }

        root.setTop(menuBar);
    }

    /**
     * Запуск приложения
     */
    public static void main(String[] args) {
        Main.launch(args);
    }
}
