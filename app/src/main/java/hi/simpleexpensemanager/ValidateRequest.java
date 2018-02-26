package hi.simpleexpensemanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haein on 2/26/2018.
 */

public class ValidateRequest extends StringRequest {

    final static private String URL = "http://greenohi.cafe24.com/UserValidate.php";
    private Map<String, String> parameters;

    public ValidateRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID); //each paramter will match
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
