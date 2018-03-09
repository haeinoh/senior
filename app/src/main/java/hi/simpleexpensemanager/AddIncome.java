package hi.simpleexpensemanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haein on 3/7/2018.
 */

public class AddIncome extends StringRequest {
    final static private String URL = "http://greenohi.cafe24.com/AddIncome.php";
    private Map<String, String> parameters;

    public AddIncome(String incomeName, Double incomeAmount, String incomeCategory, String incomeDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<String, String>();

        parameters.put("incomeName", incomeName);
        parameters.put("incomeAmount", String.valueOf(incomeAmount));
        parameters.put("incomeCategory", incomeCategory);
        parameters.put("incomeDate", incomeDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
