import com.github.kevinsawicki.http.HttpRequest;

import java.net.URL;

public class Http extends HttpRequest {

    public Http(CharSequence url, String method) throws HttpRequestException {
        super(url, method);
    }

    public Http(URL url) throws HttpRequestException {
        super(url, "GET");
    }
}
