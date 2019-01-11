package model;

/**
 * Created by QQ:5071246 on 2018/12/9.
 */
public class MyData {
    private static boolean stopRun = false;
    // cookie
    private static String cookie = "";
    //Referer
    private static String referer = "";
    //venderId
    private static String venderId = null;
    //ID
    private static String ID = "";

    public static String getVenderId() {
        return venderId;
    }

    public static void setVenderId(String venderId) {
        MyData.venderId = venderId;
    }

    public static String getCookie() {
        return cookie;
    }

    public static void setCookie(String cookie) {
        MyData.cookie = cookie;
    }

    public static boolean isStopRun() {
        return stopRun;
    }

    public static void setStopRun(boolean stopRun) {
        MyData.stopRun = stopRun;
    }

    public static String getReferer() {
        return referer;
    }

    public static void setReferer(String referer) {
        MyData.referer = referer;
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String ID) {
        MyData.ID = ID;
    }
}
