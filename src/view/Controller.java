package view;

import com.sun.xml.internal.bind.v2.model.core.ID;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.MyData;
import service.MyService;
import service.PrintInfo;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;  //主面板
    @FXML
    private Button startButton;     //开始按钮
    @FXML
    private Button stopButton;      //结束按钮
    @FXML
    private CheckBox onSaleCheckBox;    //在售选项
    @FXML
    private CheckBox forSaleCheckBox;   //待售
    @FXML
    private TextField searchWordTextField;  //搜索词
    @FXML
    private TextField changeWordTextField;  //条件词
    @FXML
    private GridPane changePanel;
    @FXML
    private TextField newWordTextField;
    @FXML
    private TextField oldWordTextField;
    @FXML
    private TextField IDTextField;      //ID
    @FXML
    private PasswordField passwordField;    //PWD
    @FXML
    private ComboBox typeComboBox;      //词类型
    @FXML
    private Label goodsSumLabel;        //总数
    @FXML
    private Label successCountLabel;    //成功数
    @FXML
    private Label failCountLabel;       //失败数
    @FXML
    private TextArea logTextArea;       //日志

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PrintInfo.setGoodsSumLabel(goodsSumLabel);
        PrintInfo.setSuccessCountLabel(successCountLabel);
        PrintInfo.setFailCountLabel(failCountLabel);
        PrintInfo.setLogTextArea(logTextArea);
    }

    @FXML
    private void close() {
        Alert information = new Alert(Alert.AlertType.CONFIRMATION, "是否继续退出?");
        information.setTitle("提示"); //设置标题，不设置默认标题为本地语言的information
        information.setHeaderText("提示"); //设置头标题，默认标题为本地语言的information
        Optional<ButtonType> result = information.showAndWait();
        if (result.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
            System.exit(0);
        }
    }

    @FXML
    private void windowMin() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void windowMax() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setFullScreen(true);
    }

    private double oldStageX;
    private double oldStageY;
    private double oldScreenX;
    private double oldScreenY;

    @FXML
    private void recordOldPoint(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        this.oldStageX = stage.getX();
        this.oldStageY = stage.getY();
        this.oldScreenX = event.getScreenX();
        this.oldScreenY = event.getScreenY();
    }

    @FXML
    private void windowMove(MouseEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setX(oldStageX + event.getScreenX() - oldScreenX);
        stage.setY(oldStageY + event.getScreenY() - oldScreenY);
    }

    @FXML
    private void clickTypeComboBox(){
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        if(type.equals("添加词")){
            changePanel.setVisible(false);
            changeWordTextField.setVisible(true);
        } else if (type.equals("替换首个") || type.equals("替换所有")) {
            changeWordTextField.setVisible(false);
            changePanel.setVisible(true);
        }
    }

    @FXML
    private void clickStartBtn(){
        //选择在售
        boolean isOnSale = onSaleCheckBox.isSelected();
        //选择待售
        boolean isForSale = forSaleCheckBox.isSelected();
        //搜索词
        String searchWord = searchWordTextField.getText().trim();
        //添加词
        String changeWord = changeWordTextField.getText().trim();
        //类型
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        //旧词
        String oldWordText = oldWordTextField.getText().trim();
        //新词
        String newWordText = newWordTextField.getText();

        String ID = IDTextField.getText().trim();
        String password = passwordField.getText().trim();

        if(!isOnSale && !isForSale) {
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","请选择要操作的业务!");
        }else if(searchWord.length()==0) {
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","搜索词错误!");
        }else if(type.equals("添加词") && changeWord.length()==0) {
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","添加词错误!");
        }else if((type.equals("替换首个") || type.equals("替换所有")) && oldWordText.length()==0){
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","替换词第一个输入框不能为空!");
        }else if(ID.length()==0) {
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","账号不能为空!");
        }else if(password.length()==0) {
            PrintInfo.alert(Alert.AlertType.ERROR,"信息","密码不能为空!");
        } else {
            Boolean boo = true;
            if(type.equals("替换词")) {
                Optional<ButtonType> result = PrintInfo.alert(Alert.AlertType.CONFIRMATION,"提示","您的替换词长度为"+changeWord.length()+",将自动替换商品标题的前"+changeWord.length()+"个字符,是否继续?");
                if (!result.get().getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                    boo = false;
                }
            }
            if (boo) {
                new Thread(() -> {
                    long startTime = System.currentTimeMillis(); // 获取开始时间
                    try {
                        startButton.setDisable(true);
                        if(MyData.getCookie().length()==0 || !MyData.getID().equals(ID)) {
                            Platform.runLater(() -> {
                                if(UI.getCookieApplication ==null){
                                    UI.getCookieApplication = new GetCookieApplication();
                                }
                                UI.getCookieApplication.showWindow(ID,password);
                            });
                        }

                        while(!MyData.isStopRun()){
                            if(MyData.getCookie()==null || MyData.getCookie().length()==0){
                                Thread.sleep(100);
                            }else{
                                Platform.runLater(()->UI.getCookieApplication.closeWindow());
                                break;
                            }
                        }
                        if(MyData.getVenderId()==null) {
                            PrintInfo.printLog("未获取到店铺的venderId,即将结束!");
                            return;
                        }
                        if (MyData.getCookie().length() > 0) {
                            MyData.setID(ID);
                            String saleStatus = "";
                            MyService myservice = new MyService();
                            if(isOnSale) {
                                saleStatus = "在售";
                                myservice.taskStart(searchWord, changeWord, type,saleStatus,oldWordText,newWordText);
                            }
                            if(isForSale) {
                                saleStatus = "待售";
                                myservice.taskStart(searchWord, changeWord, type,saleStatus,oldWordText,newWordText);
                            }

                        } else {
                            PrintInfo.printLog("未获取到Cookie!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        startButton.setDisable(false);
                        PrintInfo.printLog("任务结束,共耗时:" + (System.currentTimeMillis() - startTime) + "ms");
                    }
                }).start();
            }
        }
    }

    @FXML
    private void clickStopBtn(){
        MyData.setStopRun(true);
    }

}
