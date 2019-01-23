package Factory;

import net.sf.json.JSONObject;

import java.io.IOException;

public interface myMethods {
    String sendInfo(JSONObject info) throws com.arronlong.httpclientutil.exception.HttpProcessException;

    void myConnect();

    void myClose() throws IOException;


    void getFile(String fileName) throws Exception;

}
