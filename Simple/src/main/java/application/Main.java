package application;

//TODO correct calculations
//TODO LOGGING
//TODO HISTORY
//TODO APPLOGO
//TODO REFACTOR
//TODO TO FUNCTIONAL

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import control.MainViewController;


public class Main extends Application {

    private Stage primaryStage;

    Group root = new Group();
    Scene scene = new Scene(root);


    //PrimStag-Scene-BorderPane-MenuBar-Menu-MenuItem
    private BorderPane borderPane = new BorderPane();
    private MenuBar menuBar = new MenuBar();


    //PrimStag-Scene-AnchorPane
    private AnchorPane anchorPane;

    //Входная точка JFX
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JFXCalculator");


        mainView();
        addMenuBar();
    }

    public void addMenuBar() {

        ///////////////////////////////////
        Menu menu = new Menu("Menu");

        MenuItem history = new MenuItem("History");
        //TODO HISTORY
        history.setOnAction(actionEvent -> {});

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

        //MenuBar+borderPane
       // borderPane.setTop(menuBar);

        root.getChildren().add(menuBar);
        //(BorderPane+Scene) + Stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        //Запуск отдельного приложения, первым вызывается метод init() если нужно что то иниц, потом start()
        //который и есть входная точка JFX
        Main.launch(args);
    }

    public void mainView() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainView.fxml"));

            //GUI placement
            anchorPane = loader.load();

                //(AnchorPane+Scene) + Stage
            root.getChildren().add(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.show();

            //Logic
            MainViewController controller = loader.getController();
            controller.setMain(this);


        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            //System.err.println(e.getMessage() + "(FXML file not found)");
            System.exit(-1);
        }
    }
}
