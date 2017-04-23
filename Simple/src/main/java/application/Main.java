package application;

//TODO TO FUNCTIONAL

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.awt.Image;
import java.awt.Toolkit;

import control.MainViewController;
import expression.Expression;

public class Main extends Application {

    private Stage primaryStage;

    private Group root = new Group();
    private Scene scene = new Scene(root);

    ///////////////////////////////////////////////
    private ObservableList<Expression> historyLogs =  FXCollections.observableArrayList();

    public void addHistory(Expression expression) {
        historyLogs.add(expression);
    }
    ////////////////////////////////////////////////

    @Override
    public void start(Stage primaryStage) {

        try {
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("[no dock icon found]");
        }

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JFXCalculator");

        createMainView();
        createMenuBar();

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(actionEvent -> Platform.exit());
    }

    private void createMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainView.fxml"));

            AnchorPane anchorPane = loader.load();

            MainViewController controller = loader.getController();
            controller.setMainView(this);

            root.getChildren().add(anchorPane);
        } catch (IOException | IllegalStateException e) {
            System.err.println(e.getMessage());
            System.err.println("[no MainView.fxml file found]");
            System.exit(-1);
        }
    }

    private void createMenuBar() {
        ///////////////////////////////////
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");

        MenuItem history = new MenuItem("History");
        history.setOnAction(actionEvent -> new History(historyLogs));

        MenuItem close = new MenuItem("Close");
        close.setOnAction(actionEvent -> System.exit(0));

        menu.getItems().addAll(history, close);

        ////////////////////////////////////
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        menuBar.getMenus().add(menu);
        ////////////////////////////////////

        final String os = System.getProperty("os.name");

        if (os != null && os.startsWith("Mac")) {
             menuBar.useSystemMenuBarProperty().set(true);
        }

        root.getChildren().add(menuBar);
    }

    public static void main(String[] args) {
        Main.launch(args);
    }
}
