package service;

import model.HeadersData;
import model.MyData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by QQ:5071246 on 2018/12/9.
 */
public class MyService {
    public void taskStart(String searchWord,String changeWord,String type,String saleStatus,String oldWordText,String newWordText){
        PrintInfo.init();
        PrintInfo.printLog("开始修改"+saleStatus+"商品标题...");
        if(MyData.getVenderId()==null || MyData.getVenderId().length()==0) {
            PrintInfo.printLog("未获取到店铺VenderId,停止运行!");
            return;
        }
        String url = null;
        try {
            if(saleStatus.equals("待售")) {
                //待售
                url = "https://ware.shop.jd.com/forSaleWare/forSaleWare_newDoSearch.action?page=0&onSaleForSaleWareQuery.wareName="+URLEncoder.encode(searchWord.trim(), "UTF-8")+"&onSaleForSaleWareQuery.skuId=&catLev_1=&categoryMemoryIds=&venderId="+MyData.getVenderId()+"&onSaleForSaleWareQuery.wareId=&onSaleForSaleWareQuery.jdPriceLowString=&onSaleForSaleWareQuery.jdPriceHighString=&onSaleForSaleWareQuery.shopCategoryLevel1=-2&onSaleForSaleWareQuery.shopCategoryLevel2=-2&onSaleForSaleWareQuery.itemNum=&onSaleForSaleWareQuery.stockNumLow=&onSaleForSaleWareQuery.stockNumHigh=&onSaleForSaleWareQuery.orderBy=forsaletime%2Cdesc&onSaleForSaleWareQuery.offlineTimeStartStr=&onSaleForSaleWareQuery.offlineTimeEndStr=&onSaleForSaleWareQuery.wareStatusStr=forSale&onSaleForSaleWareQuery.brandId=";
            }else {
                //在售
                url = "https://ware.shop.jd.com/onSaleWare/onSaleWare_newDoSearch.action?page=0&onSaleForSaleWareQuery.wareStatusStr=onSale&onSaleForSaleWareQuery.wareName="+URLEncoder.encode(searchWord.trim(), "UTF-8")+"&onSaleForSaleWareQuery.skuId=&catLev_1=&categoryMemoryIds=&venderId="+MyData.getVenderId()+"&onSaleForSaleWareQuery.wareId=&onSaleForSaleWareQuery.jdPriceLowString=&onSaleForSaleWareQuery.jdPriceHighString=&onSaleForSaleWareQuery.shopCategoryLevel1=-2&onSaleForSaleWareQuery.shopCategoryLevel2=-2&onSaleForSaleWareQuery.itemNum=&onSaleForSaleWareQuery.stockNumLow=&onSaleForSaleWareQuery.stockNumHigh=&onSaleForSaleWareQuery.orderBy=onsaletime%2Cdesc&onSaleForSaleWareQuery.onlineTimeStartStr=&onSaleForSaleWareQuery.onlineTimeEndStr=&onSaleForSaleWareQuery.brandId=";
            }
            MyData.setReferer(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //获取搜索的待售商品数据
        String htmlData = getHtmlData(url,HeadersData.getForSaleWare_newDoSearch_map());
        if(htmlData ==null) {
            PrintInfo.printLog("90:获取网页数据失败,即将退出!");
            return;
        }
        if(htmlData.contains("抱歉，未找到相关商品")) {
            PrintInfo.printLog("抱歉，未找到相关商品,即将退出!");
            return;
        }
        Pattern p = Pattern.compile("(?:共)(?<recordTotal>\\d+?)(?:条记录</span>\r\n.+?共)(?<pageTotal>\\d+)(?:页)");
        Matcher m = p.matcher(htmlData);
        //搜索词搜索的待售商品数量
        String recordTotal = null;
        //搜索词搜索的待售商品页数
        String pageTotalStr = null;
        while(m.find()) {
            recordTotal = m.group("recordTotal");
            pageTotalStr = m.group("pageTotal");
        }
        if(recordTotal==null || pageTotalStr==null) {
            PrintInfo.printLog("未获取到“"+searchWord+"”商品数量,即将退出!");
            return;
        }
        //界面显示所有关联商品总数
        PrintInfo.setGoodsSum(recordTotal);
        //提取商品编码和标题并修改
        updateGoodsTitle(htmlData,changeWord,type,oldWordText,newWordText);

        Integer pageTotal = Integer.parseInt(pageTotalStr);
        for(int page=2;page<=pageTotal;page++) {
            if(MyData.isStopRun()) {
                break;
            }
            try {
                if(saleStatus.equals("待售")) {
                    //待售
                    url = "https://ware.shop.jd.com/forSaleWare/forSaleWare_newDoSearch.action?onSaleForSaleWareQuery.wareName="+URLEncoder.encode(searchWord.trim(), "UTF-8")+"&onSaleForSaleWareQuery.shopCategoryLevel1=-2&onSaleForSaleWareQuery.shopCategoryLevel2=-2&onSaleForSaleWareQuery.wareStatusStr=forSale&onSaleForSaleWareQuery.orderBy=forsaletime%2Cdesc&venderId="+MyData.getVenderId()+"&page="+page;
                }else {
                    //在售
                    url = "https://ware.shop.jd.com/onSaleWare/onSaleWare_newDoSearch.action?onSaleForSaleWareQuery.wareName="+URLEncoder.encode(searchWord.trim(), "UTF-8")+"&onSaleForSaleWareQuery.shopCategoryLevel1=-2&onSaleForSaleWareQuery.shopCategoryLevel2=-2&onSaleForSaleWareQuery.wareStatusStr=onSale&onSaleForSaleWareQuery.orderBy=onsaletime%2Cdesc&venderId="+MyData.getVenderId()+"&page="+page;
                }
                MyData.setReferer(url);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //获取搜索的商品数据
            htmlData = getHtmlData(url,HeadersData.getForSaleWare_newDoSearch_map());
            if(htmlData ==null) {
                int num=0;
                if(page==pageTotal) {
                    num = Integer.parseInt(recordTotal)%10;
                }else {
                    num = 10;
                }
                for(int c=0;c<num;c++) {
                    PrintInfo.setSelfPlus("fail");
                }
                PrintInfo.printLog("未获取到“"+searchWord+"”第"+page+"页数据,跳过!");
                continue;
            }
            //提取商品编码和标题并修改
            updateGoodsTitle(htmlData,changeWord,type,oldWordText,newWordText);
        }
    }

    private int count = 0;
    private void updateGoodsTitle(String htmlData,String changeWord,String type,String oldWordText,String newWordText) {
        Document doc = Jsoup.parse(htmlData);
        Elements elements = doc.select("p[class=ware_title]");
        for(Element element:elements) {
            if(MyData.isStopRun()) {
                break;
            }
            String goodsCode = element.attr("data-url").trim();
            String title = element.children().text().trim();
            String goodsTitle = null;

            if(type.equals("添加词")){
                goodsTitle = changeWord + title;
            }else if(type.equals("替换首个")){
                goodsTitle = title.replaceFirst(oldWordText,newWordText);
            }else if(type.equals("替换所有")) {
                goodsTitle = title.replace(oldWordText,newWordText);
            }
            if(goodsTitle!=null && goodsTitle.length()>50) {
                PrintInfo.setSelfPlus("fail");
                PrintInfo.printLog("商品编码为:"+goodsCode+",标题过长,跳过!");
                continue;
            }
            String url = "https://ware.shop.jd.com/ware/newPublish/ware_updateWareTitle.action";
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("wareId", goodsCode);
            paramMap.put("wareTitle", goodsTitle);
            paramMap.put("randam", ""+new Random().nextDouble());
            String responseData = postHtmlData(url, paramMap, HeadersData.getWare_updateWareTitle_map());
//			String responseData = "0";
            if(responseData!=null && responseData.equals("0")) {
                PrintInfo.setSelfPlus("success");
                PrintInfo.printLog("第"+(++count)+"条,修改后:"+goodsTitle+",修改前:"+element.children().text().trim());
            }else {
                if(responseData!=null){
                    Pattern p = Pattern.compile("(?:\"msg\":\")(?<msg>.+?)(?:\"})");
                    Matcher m = p.matcher(responseData);
                    if(m.find()){
                        PrintInfo.printLog("失败:"+m.group("msg")+" "+goodsCode+goodsTitle);
                    }
                }
                PrintInfo.setSelfPlus("fail");
            }
        }
    }

    private String getHtmlData(String url,Map<String,String> map) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String htmlData = HttpUtils.sendGet(url, map);
        for(int i=0;i<5;i++) {
            if(MyData.isStopRun()) {
                break;
            }
            if(htmlData==null || htmlData.length()==0 || Jsoup.parse(htmlData).title().equals("出错啦")) {
                PrintInfo.printLog("请求数据失败,1秒后重试!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                htmlData = HttpUtils.sendGet(url, map);
            }
            if(htmlData!=null && htmlData.length()!=0) {
                break;
            }
        }
        return htmlData;
    }

    private String postHtmlData(String requestUrl,Map<String,String> paramMap, Map<String,String> headerMap) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String htmlData = HttpUtils.jsoupPost(requestUrl, paramMap, headerMap);
        for(int i=0;i<5;i++) {
            if(MyData.isStopRun()) {
                break;
            }
            if(htmlData==null || htmlData.length()==0) {
                PrintInfo.printLog("请求数据失败,1秒后重试!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                htmlData = HttpUtils.jsoupPost(requestUrl, paramMap, headerMap);
            }
            if(htmlData!=null && htmlData.length()!=0) {
                break;
            }
        }
        return htmlData;
    }
}
