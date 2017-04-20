package application;

//TODO correct calculations
//TODO REFACTOR + DIAGR
//TODO LOGGING
//TODO HISTORY
//TODO TO FUNCTIONAL


import java.io.IOException;

import java.awt.Image;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.apple.eawt.*;

import control.MainViewController;

public class Main extends Application {

    private Stage primaryStage;

    //Stage-scene-group
    private Group root = new Group();
    private Scene scene = new Scene(root);


    //PrimStag-Scene-Group-BorderPane-MenuBar-Menu-MenuItem
    //private BorderPane borderPane = new BorderPane();
    private MenuBar menuBar = new MenuBar();


    //PrimStag-Scene-Group-AnchorPane
    private AnchorPane anchorPane;

    //Входная точка JFX
    @Override
    public void start(Stage primaryStage) {

        try {
            //TODO REALATIVE
            Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/icon.png");
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }

        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        primaryStage.setTitle("JFXCalculator");


        mainView();
        addMenuBar();

        primaryStage.setScene(scene);
        primaryStage.show();
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
        //  primaryStage.setScene(scene);
        // primaryStage.show();
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

                //(AnchorPane)Group + Stage
            root.getChildren().add(anchorPane);

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
