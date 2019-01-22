import Factory.Factory;
import Factory.myMethods;
import JsonConvert.JsonConvert;
import WebChoice.WebChoice;
import net.sf.json.JSONObject;
import org.java_websocket.exceptions.WebsocketNotConnectedException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * from apsa
 */
public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {
        boolean isExist;
        String Mode = null;
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
        Mode = Config.get("mode").toString();
        Host = Config.get("host").toString();
        Port = Config.get("port").toString();
        URI url = getUrl(Host, Port);//构造Uri类
//        WebChoice con = new WebChoice(url);
//        con.connect();//连接数据
//        try{
//            con.send(che);
//        }catch (WebsocketNotConnectedException ex){
//            ex.printStackTrace();
//        }
//        //con.close();
//        System.out.println(con.getMess());
//        URL sd = new URL("http://www.baidu.com");
//        Http ss = new Http(sd);
//        System.out.println(ss.date());
        myMethods con = new Factory().getConnect(Config);
        WebChoice ss = new WebChoice(url);
        con.myConnect();
        //ss.connect();
        Scanner input = new Scanner(System.in);
        String che = input.next();
        try {
            con.sendInfo(che);
            //ss.send(che);
        } catch (WebsocketNotConnectedException ex) {
            ex.printStackTrace();
        }
        con.myClose();
        //System.out.println(con.isClosed());
    }

    //判断文件是否存在 存在：true
    public static boolean checkFile(String confFile) {
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

//    public static String getRecv(WebChoice con){
//        String info=null;
//        int i=1;
//        System.out.println(info);//这个不能删，我也不知道为啥
//        while (true){
//            if(con.getMess()!=null) {
//                info = con.getMess();
//                break;
//            }
//           i=i+0;
//        }
//        con.setMess();
//        return info;
//    }
}




