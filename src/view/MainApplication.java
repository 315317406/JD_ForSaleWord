package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        AnchorPane anchorPane = (AnchorPane)root.lookup("#anchorPane");
        Scene scene = new Scene(root, anchorPane.getPrefWidth(), anchorPane.getPrefHeight(),Color.TRANSPARENT);
        if(UI.mainStage==null){
            UI.mainStage  = new Stage();
        }
        UI.mainStage.setTitle("京东批量修改标题");
        UI.mainStage.setScene(scene);
        UI.mainStage.setMinWidth(anchorPane.getPrefWidth());
        UI.mainStage.setMinHeight(anchorPane.getPrefHeight());
        UI.mainStage.initStyle(StageStyle.TRANSPARENT);
        UI.mainStage.getIcons().add(new Image("image/icon.png"));
        UI.mainStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
