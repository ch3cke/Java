package JsonConvert;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonConvert {
    //获取Json文件的内容
    public static JSONObject readConf(String jsonFile) {
        File file = new File(jsonFile);
        BufferedReader reader = null;
        String conf = "";
        try {
            reader = new BufferedReader(new FileReader(jsonFile));
            String temp = null;
            while ((temp = reader.readLine()) != null) {
                conf = conf + temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsStr = JSONObject.fromObject(conf);
        return jsStr;
    }
}
