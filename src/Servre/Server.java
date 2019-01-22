package Servre;

import JsonConvert.JsonConvert;
import net.sf.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class Server extends WebSocketServer {
    public Server(int port) {
        super(new InetSocketAddress(port));
    }

    public Server(InetSocketAddress address) {
        super(address);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 0;
        if (getConfig() > 0) {
            port = getConfig();
        } else {
            System.exit(0);
        }
        Server s = new Server(port);
        s.start();
        System.out.println("started on port: " + s.getPort());

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
            s.broadcast(in);
            if (in.equals("exit")) {
                s.stop(1000);
                break;
            }
        }
    }

    /*在配置文件中找到打开的端口*/
    public static int getConfig() {
        File file = new File("Server_config.json");
        JSONObject Config = new JSONObject();
        if (file.exists()) {
            Config = JsonConvert.readConf("Server_config.json");
            return Config.getInt("Port");
        } else {
            System.out.println("no Config file!");
            return -1;
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " send a message");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " closed");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(conn + ": " + message);
        conn.send("sss");
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}
