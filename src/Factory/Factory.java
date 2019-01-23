package Factory;

import FileTransfer.FileReceive;
import JsonConvert.JsonConvert;
import com.arronlong.httpclientutil.common.HttpHeader;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Factory {
    public myMethods getConnect(JSONObject str) throws URISyntaxException {
        String mode = str.getString("mode");
        URI sockURI = new URI("ws://" + str.getString("host") + ":" + str.getString("port"));
        String httpURL = "http://" + str.getString("host") + ":" + str.getString("port");
        switch (mode) {
            case "Socket":
            case "socket":
                return new mySocket(sockURI);
            case "http":
            case "Http":
                return new myHttp(httpURL);
            default:
                return new mySocket(sockURI);
        }
    }
}

class mySocket extends WebSocketClient implements myMethods {
    private String mess = null;
    private int flag = 0;

    public mySocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onMessage(String message) {
        mess = message;
        flag = 1;
        System.out.println("received: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public String getMess() {
        String info;//传出得到的信息
        while (true) {
            if (flag == 1) {
                info = mess;
                this.setMess();
                return info;
            } else {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMess() {
        mess = null;
        flag = 0;
    }

    public String sendInfo(JSONObject info) {
        this.send(info.toString());
        return getMess();
    }

    public void myConnect() {
        this.connect();
    }

    public void myClose() {
        this.close();
    }

    /*
    远程文件下载
     */
    public void getFile(String fileName) throws Exception {
        Socket conn = this.getSocket();
        OutputStream fileNam = conn.getOutputStream();
        this.send("download");
        FileReceive d = new FileReceive();//file为下载文件所在的文件夹
        d.service();
    }

}

class myHttp implements myMethods {
    private static ArrayList<String> fileList = new ArrayList<String>();
    private String url = null;

    /**
     * http的头文件，可以自己定义
     */
    public myHttp(String httpUrl) {
        this.url = httpUrl;
    }

    @Override
    public String sendInfo(JSONObject info) throws com.arronlong.httpclientutil.exception.HttpProcessException {
        Map<String, Object> map = new HashMap<>();
        com.arronlong.httpclientutil.common.HttpConfig config = com.arronlong.httpclientutil.common.HttpConfig.custom();
        HttpHeader.Headers[] headers = (HttpHeader.Headers[]) HttpHeader.custom().userAgent("Mozilla/5.0").build();
        config.headers((Header[]) headers);
        return com.arronlong.httpclientutil.HttpClientUtil.post(config);
    }

    @Override
    public void myConnect() {

    }

    @Override
    public void myClose() {

    }


    @Override
    public void getFile(String fileName) throws FileNotFoundException, com.arronlong.httpclientutil.exception.HttpProcessException {
        JSONObject conf = new JSONObject();
        conf = JsonConvert.readConf("config.json");
        String file = conf.getString("file").replace(" ", "");
        String[] files = file.split(",");
        for (String fi : files) {
            System.out.println(fi);
            String Url = this.url + "/" + fi;
            System.out.println(Url);
            File doc = new File("e:/" + fi);
            com.arronlong.httpclientutil.HttpClientUtil.down(com.arronlong.httpclientutil.common.HttpConfig.custom().url(Url).out(new FileOutputStream(doc)));
            if (doc.exists()) {
                System.out.println("图片下载成功了！存放在：" + doc.getPath());
            }
        }

    }


}
