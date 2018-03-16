package hi.simpleexpensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpenseActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private static final String TAG = "ExpenseActivity";

    private String expenseName;
    private Double expenseAmount;
    private String expenseCategory;
    private String expenseDate;
    private AlertDialog dialog;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        mDisplayDate = (TextView) findViewById(R.id.selectDate);

        spinner = (Spinner) findViewById(R.id.expenseSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.expense, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText amountText = (EditText) findViewById(R.id.amountText);;

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //month = month + 1;
                year = year - 1900;
                //Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                Date d = new Date(year, month, day);
                SimpleDateFormat dateFormatter = new SimpleDateFormat(
                        "MM/dd/yyyy");
                String date = dateFormatter.format(d);

                //String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        Button saveExpenseButton = (Button) findViewById(R.id.saveExpenseButton);
        saveExpenseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                String expenseName = nameText.getText().toString();
                Double expenseAmount = Double.valueOf(amountText.getText().toString());
                String expenseCategory = spinner.getSelectedItem().toString();
                String expenseDate = mDisplayDate.getText().toString();

               /*if(expenseName.equals("") || expenseAmount.equals("") || expenseCategory.equals("") || expenseDate.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
                    dialog = builder.setMessage("Blank is not allowed")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }*/
                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
                                dialog = builder.setMessage("Saved successfully")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
                                dialog = builder.setMessage("Failed to save")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                AddExpense addExpense = new AddExpense(expenseName, expenseAmount, expenseCategory, expenseDate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ExpenseActivity.this);
                queue.add(addExpense);
            }
        });
    }
}
