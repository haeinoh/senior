package hi.simpleexpensemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Haein on 3/16/2018.
 */

public class ExpenseListAdapter extends BaseAdapter {
    private Context context2;
    private List<Expense> expenseList;

    public ExpenseListAdapter(Context context2, List<Expense> expenseList)
    {
        this.context2 = context2;
        this.expenseList = expenseList;
    }

    @Override
    public int getCount() {
        return expenseList.size();
    }

    @Override
    public Object getItem(int i) {
        return expenseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context2, R.layout.expense, null);

        TextView expenseDateText = (TextView) v.findViewById(R.id.expenseDateText);
        TextView expenseCategoryText = (TextView) v.findViewById(R.id.expenseCategoryText);
        TextView expenseNameText = (TextView) v.findViewById(R.id.expenseNameText);
        TextView expenseAmountText = (TextView) v.findViewById(R.id.expenseAmountText);

        expenseNameText.setText(expenseList.get(i).getName());
        expenseDateText.setText(expenseList.get(i).getDate());
        expenseCategoryText.setText(expenseList.get(i).getCategory());
        expenseAmountText.setText(expenseList.get(i).getAmount());

        v.setTag(expenseList.get(i).getName());
        return v;
    }
}
