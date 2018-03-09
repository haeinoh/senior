package hi.simpleexpensemanager;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haein on 3/9/2018.
 */

public class AddExpense extends StringRequest{

    final static private String URL = "http://greenohi.cafe24.com/AddExpense.php";
    private Map<String, String> parameters;

    public AddExpense(String expenseName, Double expenseAmount, String expenseCategory, String expenseDate, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<String, String>();

        parameters.put("expenseName", expenseName);
        parameters.put("expenseAmount", String.valueOf(expenseAmount));
        parameters.put("expenseCategory", expenseCategory);
        parameters.put("expenseDate", expenseDate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
