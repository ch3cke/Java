package Factory;

import com.github.kevinsawicki.http.HttpRequest;
import net.sf.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class Factory {
    public myMethods getConnect(JSONObject str) throws URISyntaxException, MalformedURLException {
        String mode = str.getString("mode");
        URI sockURI = new URI("ws://" + str.getString("host") + ":" + str.getString("port"));
        URL httpURL = new URL("http://" + str.getString("host") + ":" + str.getString("port"));
        if (mode.equals("Socket") || mode.equals("socket")) {
            return new Socket(sockURI);
        } else if (mode.equals("http") || mode.equals("Http")) {
            return new Http(httpURL);
        } else {
            return new Http(httpURL);
        }
    }
}

class Socket extends WebSocketClient implements myMethods {
    private String mess = null;
    private int flag = 0;

    public Socket(URI serverUri) {
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
        while (true) {
            if (flag == 1) {
                return mess;
            } else {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setMess() {
        mess = null;
        flag = 0;
    }

    public void sendInfo(String info) {
        this.send(info);
    }

    public void myConnect() {
        this.connect();
    }

    public void myClose() {
        this.close();
    }

}

class Http extends HttpRequest implements myMethods {

    public Http(CharSequence url) throws HttpRequestException {
        super(url, "GET");
    }

    public Http(URL url) throws HttpRequestException {
        super(url, "GET");
    }

    public void sendInfo(String info) {
        this.send(info.getBytes());
    }

    public void myConnect() {
        this.getConnection();
    }

    public void myClose() throws IOException {
        this.closeOutput();
    }
}

