package model;

import java.util.HashMap;
import java.util.Map;

public class HeadersData {
	/**
	 * 获取普通请求头
	 */
	private static Map<String,String> general_map = new HashMap<String,String>();
	public static Map<String, String> getGeneral_map() {
		general_map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		general_map.put("Accept-Encoding","gzip, deflate, br");
		general_map.put("Accept-Language","zh-CN,zh;q=0.9");
		general_map.put("Connection","keep-alive");
		general_map.put("Upgrade-Insecure-Requests","1");
		general_map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		return general_map;
	}
	
	/**
	 * 获取待售商品搜索请求头
	 * 获取可能网页标题为“出错啦”此时应重新获取
	 * GET:https://ware.shop.jd.com/forSaleWare/forSaleWare_newDoSearch.action?page=0&onSaleForSaleWareQuery.wareName=%E5%8D%AB%E8%A1%A3&onSaleForSaleWareQuery.skuId=&catLev_1=&categoryMemoryIds=&venderId=769594&onSaleForSaleWareQuery.wareId=&onSaleForSaleWareQuery.jdPriceLowString=&onSaleForSaleWareQuery.jdPriceHighString=&onSaleForSaleWareQuery.shopCategoryLevel1=-2&onSaleForSaleWareQuery.shopCategoryLevel2=-2&onSaleForSaleWareQuery.itemNum=&onSaleForSaleWareQuery.stockNumLow=&onSaleForSaleWareQuery.stockNumHigh=&onSaleForSaleWareQuery.orderBy=forsaletime%2Cdesc&onSaleForSaleWareQuery.offlineTimeStartStr=&onSaleForSaleWareQuery.offlineTimeEndStr=&onSaleForSaleWareQuery.wareStatusStr=forSale&onSaleForSaleWareQuery.brandId=
	 */
	private static Map<String,String> forSaleWare_newDoSearch_map = new HashMap<String,String>();
	public static Map<String, String> getForSaleWare_newDoSearch_map() {
		forSaleWare_newDoSearch_map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		forSaleWare_newDoSearch_map.put("Accept-Encoding","gzip, deflate, br");
		forSaleWare_newDoSearch_map.put("Accept-Language","zh-CN,zh;q=0.9");
		forSaleWare_newDoSearch_map.put("Connection","keep-alive");
		forSaleWare_newDoSearch_map.put("Cookie",MyData.getCookie());
		forSaleWare_newDoSearch_map.put("Host","ware.shop.jd.com");
		forSaleWare_newDoSearch_map.put("Referer","https://ware.shop.jd.com/forSaleWare/forSaleWare_newDoSearch.action?firstQuery=1");
		forSaleWare_newDoSearch_map.put("Upgrade-Insecure-Requests","1");
		forSaleWare_newDoSearch_map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		return forSaleWare_newDoSearch_map;
	}
	
	/**
	 * 修改待售商品标题请求头
	 * POST(Form Data):https://ware.shop.jd.com/ware/newPublish/ware_updateWareTitle.action
	 * 参数:
	 * 	wareId:12193943927
	 *	wareTitle:2018%20%E7%A7%8B%E5%86%AC%E6%96%B0%E6%AC%BE%E7%BB%A3%E8%8A%B1%E6%AF%9B%E8%A1%A3%E5%A5%B3%E5%A5%97%E5%A4%B4%E7%9F%AD%E6%AC%BE%E6%89%93%E5%BA%95%E8%A1%AB%E5%AE%BD%E6%9D%BE%E9%9F%A9%E7%89%88%E9%95%BF%E8%A2%96%E9%92%88%E7%BB%87%E8%A1%AB%E4%B8%8A%E8%A1%A3
	 *	randam:0.5875374210474797
	 */
	private static Map<String,String> ware_updateWareTitle_map = new HashMap<String,String>();
	public static Map<String, String> getWare_updateWareTitle_map() {
		ware_updateWareTitle_map.put("Accept","*/*");
		ware_updateWareTitle_map.put("Accept-Encoding","gzip, deflate, br");
		ware_updateWareTitle_map.put("Accept-Language","zh-CN,zh;q=0.9");
		ware_updateWareTitle_map.put("Connection","keep-alive");
		ware_updateWareTitle_map.put("Content-Length","469");
		ware_updateWareTitle_map.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
		ware_updateWareTitle_map.put("Cookie",MyData.getCookie());
		ware_updateWareTitle_map.put("Host","ware.shop.jd.com");
		ware_updateWareTitle_map.put("Origin","https://ware.shop.jd.com");
		ware_updateWareTitle_map.put("Referer",MyData.getReferer());
		ware_updateWareTitle_map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		ware_updateWareTitle_map.put("X-Requested-With","XMLHttpRequest");
		return ware_updateWareTitle_map;
	}
	
	/**
	 * 店铺后台首页请求头
	 * https://shop.jd.com/
	 */
	private static Map<String,String> shop_jd_map = new HashMap<String,String>();
	public static Map<String, String> getShop_jd_map() {
		shop_jd_map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		shop_jd_map.put("Accept-Encoding","gzip, deflate, br");
		shop_jd_map.put("Accept-Language","zh-CN,zh;q=0.9");
		shop_jd_map.put("Cache-Control","max-age=0");
		shop_jd_map.put("Connection","keep-alive");
		shop_jd_map.put("Cookie",MyData.getCookie());
		shop_jd_map.put("Host","shop.jd.com");
		shop_jd_map.put("Referer","https://shop.jd.com/");
		shop_jd_map.put("Upgrade-Insecure-Requests","1");
		shop_jd_map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		return shop_jd_map;
	}
	
	
}
