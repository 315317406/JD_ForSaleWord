package service;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.Optional;

/**
 * Created by QQ:5071246 on 2018/12/8.
 */
public class PrintInfo {
    private static TextArea logTextArea;       //日志
    private static Label goodsSumLabel;        //总数
    private static Label successCountLabel;    //成功数
    private static Label failCountLabel;       //失败数
    private static Alert alert;                //弹窗提示

    public static void printLog(String str) {
        Platform.runLater(()->logTextArea.appendText(str+"\r\n"));
    }

    public static void setSelfPlus(String type) {
        if(type.equals("success")) {
            Platform.runLater(()->{
                Integer count = Integer.parseInt(successCountLabel.getText());
                successCountLabel.setText(String.valueOf(++count));
            });
        }else if(type.equals("fail")) {
            Platform.runLater(()->{
                Integer count = Integer.parseInt(failCountLabel.getText());
                failCountLabel.setText(String.valueOf(++count));
            });
        }
    }

    public static void setGoodsSum(String recordTotal) {
        Platform.runLater(()->{
            Integer count = Integer.parseInt(goodsSumLabel.getText());
            if(count!=0) {
                count = count + Integer.parseInt(recordTotal);
                goodsSumLabel.setText(String.valueOf(count));
            }else {
                goodsSumLabel.setText(recordTotal);
            }
        });
    }

    public static Optional<ButtonType> alert(Alert.AlertType alertType,String title,String msg){
        alert = new Alert(alertType);
        alert.titleProperty().set(title);
        alert.headerTextProperty().set(msg);
        return alert.showAndWait();
    }

    public static void init(){
        Platform.runLater(()->{
            logTextArea.setText("");
            goodsSumLabel.setText("0");
            successCountLabel.setText("0");
            failCountLabel.setText("0");
        });
    }

    public static void setGoodsSumLabel(Label goodsSumLabel) {
        PrintInfo.goodsSumLabel = goodsSumLabel;
    }

    public static void setSuccessCountLabel(Label successCountLabel) {
        PrintInfo.successCountLabel = successCountLabel;
    }

    public static void setFailCountLabel(Label failCountLabel) {
        PrintInfo.failCountLabel = failCountLabel;
    }

    public static void setLogTextArea(TextArea logTextArea) {
        PrintInfo.logTextArea = logTextArea;
    }
}
