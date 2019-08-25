package com.mikaelr.GUIapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class AdventureApp extends Application {


    public void start(Stage stage) {

        // menus
        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Game");
        MenuItem newGame = new MenuItem("New game");
        MenuItem quitGame = new MenuItem("Quit");
        menuGame.getItems().addAll(newGame, quitGame);

        Menu menuHelp = new Menu("Help");
        MenuItem getHelp = new MenuItem("Help");
        MenuItem getAbout = new MenuItem("About");
        menuHelp.getItems().addAll(getHelp, getAbout);
        menuBar.getMenus().addAll(menuGame, menuHelp);

        newGame.setOnAction(e -> {

            BorderPane gameView = new BorderPane();
            Controller control = new Controller();
            gameView.setTop(menuBar);
            gameView.setCenter(control.gameRoot());
            Scene scene = new Scene(gameView);
            stage.setScene(scene);
            stage.show();

        });
        quitGame.setOnAction(e -> {
            System.exit(0);
        });
        getHelp.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText("Help");
            info.setContentText("List of commands coming soon..");
            info.showAndWait();
        });
        getAbout.setOnAction(e -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setHeaderText("Cave Adventure v.1.0");
            info.setContentText("(c) Mikael Rauhala, 2019");
            info.showAndWait();
        });

        // game view
        BorderPane gameView = new BorderPane();
        Controller control = new Controller();
        gameView.setTop(menuBar);
        gameView.setCenter(control.gameRoot());
        Scene scene = new Scene(gameView);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
