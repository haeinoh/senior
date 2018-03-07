package hi.simpleexpensemanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haein on 2/26/2018.
 */

public class RegisterRequest extends StringRequest {

    final static private String URL = "http://greenohi.cafe24.com/UserRegister.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userEmail, Response.Listener<String> listener){
        //send data with hidden
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID); //each paramter will match
        parameters.put("userPassword", userPassword);
        parameters.put("userEmail", userEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
