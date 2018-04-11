package hi.simpleexpensemanager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haein on 4/11/2018.
 */

public class AddBudget extends StringRequest {
    final static private String URL = "http://greenohi.cafe24.com/AddBudget.php";
    private Map<String, String> parameters;

    public AddBudget(String budgetAmount, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<String, String>();

        parameters.put("budgetAmount", budgetAmount);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
