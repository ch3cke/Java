package test;

import Factory.Factory;
import Factory.myMethods;
import JsonConvert.JsonConvert;
import WebChoice.WebChoice;
import net.sf.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * from apsa
 */
public class Main {

    public static void main(String[] args) throws Exception {
        boolean isExist;
        String Host = null;
        String Port = null;
        JSONObject Config = new JSONObject();
        String ConfigFile = "config.json";
        isExist = checkFile(ConfigFile);
        if (!isExist) {
            System.out.println("config file is not exist");
            System.exit(0);
        } else {
            Config = JsonConvert.readConf(ConfigFile);//获取配置参数
        }
        Host = Config.get("host").toString();
        Port = Config.get("port").toString();
        URI url = getUrl(Host, Port);//构造Uri类
        myMethods con = new Factory().getConnect(Config);//生成一个连接的类
        WebChoice ss = new WebChoice(url);               //测试用，稍后删除
        con.myConnect();                                 //保持连接
        //ss.connect();
        con.getFile("sss");
        Scanner input = new Scanner(System.in);
        String che = input.next();
//        try {
//            con.sendInfo(che);                          //发送信息
//            //ss.send(che);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        con.getFile("ssss");
//        con.myClose();                                  //关闭连接
        //System.out.println(con.isClosed());

    }

    /*
    判断文件是否存在 存在：true
    */
    private static boolean checkFile(String confFile) {
        boolean newFile = false;
        File file = new File(confFile);
        newFile = file.exists();
        return newFile;
    }

    public static URI getUrl(String Host, String Port) throws URISyntaxException {
        String urls = "ws://" + Host + ":" + Port;
        URI url = new URI(urls);
        return url;
    }

}




