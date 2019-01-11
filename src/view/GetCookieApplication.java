package view;/**
 * Created by QQ:5071246 on 2018/12/9.
 */

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import model.MyData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import service.PrintInfo;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetCookieApplication extends Application {
    private Stage stage;
    private Parent root;
    private WebView webView;
    private WebEngine webEngine;
    private String ID;
    private String pwd;

    public GetCookieApplication(){
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("GetCookie.fxml"));
        webView = (WebView)root.lookup("#webView");
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                if(webEngine.getTitle()!=null && webEngine.getTitle().equals("登录京东")){
                    Document doc = webEngine.getDocument();
                    Element JDJRV = doc.getElementById("JDJRV-wrap-paipaiLoginSubmit");
                    if(JDJRV!=null){
                        System.out.println("52:出现滑动验证");
                        return;
                    }
                    //账号输入框
                    Element loginname = doc.getElementById("loginname");
                    loginname.setAttribute("value", ID);
                    //密码输入框
                    Element nloginpwd = doc.getElementById("nloginpwd");
                    nloginpwd.setAttribute("value",pwd);
                    //点击登录按钮
                    webEngine.executeScript("document.getElementById('paipaiLoginSubmit').click();");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doc = webEngine.getDocument();
                    JDJRV = doc.getElementById("JDJRV-wrap-paipaiLoginSubmit");
                    if(JDJRV!=null){
                        PrintInfo.alert(Alert.AlertType.INFORMATION,"滑动验证","出现滑动验证,请手动滑动一下");
                    }
                }else{
                    String url = webEngine.getLocation();
                    try {
                        URI uri = URI.create(url);
                        Map<String, List<String>> headers = new LinkedHashMap<String, List<String>>();
                        headers = java.net.CookieHandler.getDefault().get(uri, headers);
                        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                            for (String cookie : entry.getValue()) {
                                MyData.setCookie(cookie);
                            }
                        }
                        String htmlData = (String) webEngine.executeScript("document.documentElement.outerHTML");
                        Pattern p = Pattern.compile("(?:venderId=)(?<venderId>\\d+)");
                        Matcher m = p.matcher(htmlData);
                        String venderId = null;
                        while(m.find()) {
                            venderId = m.group("venderId");
                            break;
                        }
                        MyData.setVenderId(venderId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //String html = (String) webEngine.executeScript("document.documentElement.outerHTML");
                //String html = (String) webEngine.executeScript("document.cookie");
                //String url = webEngine.getHistory().getEntries().get(webEngine.getHistory().getCurrentIndex()).getUrl();

            }
        });
        stage.setTitle("登录窗口");
        stage.setScene(new Scene(root, 800, 600));
    }

    public void showWindow(String ID,String password){
        this.ID = ID;
        this.pwd = password;
        stage.show();
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        webEngine.load("https://passport.jd.com/common/loginPage?from=pop_vender&regTag=2&ReturnUrl=https%3A%2F%2Fshop.jd.com");
    }

    public void closeWindow(){
        stage.close();
    }
}
