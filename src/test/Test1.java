package test;

import model.HeadersData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.FileUtils;
import util.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QQ:5071246 on 2018/12/9.
 */
public class Test1 {
    public static void main(String[] args) {
        test4();
    }

    public static void test4(){
//        String url = "http://www.zdziyuan.com/inc/api.php?ac=list";
//        String htmlData = HttpUtils.sendGet(url,HeadersData.getGeneral_map());
//        FileUtils.writeToLocal("xmlTest",htmlData);
        String htmlData = FileUtils.readFileText("C:\\Users\\Administrator\\Desktop\\淘宝商品数据采集\\测试\\xmlTest.txt");
        Document doc = Jsoup.parse(htmlData);
//        Elements names = doc.select("name");
        Elements names = doc.getElementsByTag("name");
        for(Element name:names){
            System.out.println(name.text());
        }

    }

    public static void test3(){
        String s = "{\"uuid\":\"306fe35493d148808908f59490f9128f\",\"comment\":\"\",\"msg\":\"参数错误.商品已删除\"}";
        Pattern p = Pattern.compile("(?:\"msg\":\")(?<msg>.+?)(?:\"})");
        Matcher m = p.matcher(s);
        if(m.find()){
            System.out.println(m.group("msg"));
        }
    }

    public static void test2(){
        String str = "a,d,e,x,cc,oo";
        String[] strarr = str.split(",");
        for(String s:strarr){
            System.out.println(s);
        }

    }

    public static void test1(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("1","11");
        map.put("2","22");
        map.put("3","33");
        map.put("4","44");
        map.put("5","55");

        for(String key : map.keySet()){
            System.out.println(key+"----"+map.get(key));
        }
    }
}
