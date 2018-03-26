package hi.simpleexpensemanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IncomeActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    //from AddIncome
    private String incomeName;
    private String incomeAmount;
    private String incomeCategory;
    private String incomeDate;
    private AlertDialog dialog;
    //private boolean validate = false;

    private static final String TAG = "IncomeActivity";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        mDisplayDate = (TextView) findViewById(R.id.selectDate);

        spinner = (Spinner) findViewById(R.id.incomeSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.income, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText amountText = (EditText) findViewById(R.id.amountText);;

        //date
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        IncomeActivity.this,
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

        Button saveIncomeButton = (Button) findViewById(R.id.saveIncomeButton);
        saveIncomeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                String incomeName = nameText.getText().toString();
                String incomeAmount = amountText.getText().toString();
                String incomeCategory = spinner.getSelectedItem().toString();
                String incomeDate = mDisplayDate.getText().toString();

                /*if(incomeName.equals("") || incomeAmount.equals("") || incomeCategory.equals("") || incomeDate.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this);
                                dialog = builder.setMessage("Saved successfully")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(IncomeActivity.this);
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
                AddIncome addIncome = new AddIncome(incomeName, incomeAmount, incomeCategory, incomeDate, responseListener);
                RequestQueue queue = Volley.newRequestQueue(IncomeActivity.this);
                queue.add(addIncome);
            }
        });
    }  //onCreate

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}
