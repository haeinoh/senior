package hi.simpleexpensemanager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Created by Haein on 3/13/2018.
 */

public class BudgetDialog extends DialogFragment {
    private Fragment fragment;
    private static final String TAG="BudgetDialog";
    private EditText mBudgetInput;
    private Button mBudgetSaveButton, mBudgetCloseButton;
    private String budgetAmount;
    private String mBudgetValue;

    public BudgetDialog(){
    }

    public interface OnInputSelected{
        void sendInput(String input);
    }
    public OnInputSelected mOnInputSelected;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog, container, false);
        mBudgetSaveButton = view.findViewById(R.id.budgetSaveButton);
        mBudgetCloseButton = view.findViewById(R.id.budgetCloseButton);
        mBudgetInput = view.findViewById(R.id.budgetInput);

        mBudgetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });

        mBudgetSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: input");

                String budgetAmount = mBudgetInput.getText().toString();
                if(!budgetAmount.equals(""))
                {
                    //TodayFragment fragment = (TodayFragment) getActivity().getSupportFragmentManager().findFragmentByTag("TodayFragment");
                    //fragment.mBudgetValue.setText(input);
                    mOnInputSelected.sendInput(budgetAmount);
                }
                else{
                    getDialog().dismiss();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success)
                            {
                                getDialog().dismiss();

                            }
                            else
                            {
                                getDialog().dismiss();

                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };

                AddBudget addBudget = new AddBudget(budgetAmount, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(addBudget);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try{
            mOnInputSelected = (OnInputSelected) getTargetFragment();
        }catch(Exception e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }
}
