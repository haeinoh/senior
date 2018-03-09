package hi.simpleexpensemanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Haein on 3/9/2018.
 */

public class IncomeListAdapter extends BaseAdapter{

    private Context context;
    private List<Income> incomeList;

    public IncomeListAdapter(Context context, List<Income> incomeList)
    {
        this.context = context;
        this.incomeList = incomeList;
    }

    @Override
    public int getCount() {
        return incomeList.size();
    }

    @Override
    public Object getItem(int i) {
        return incomeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.income, null);
        TextView incomeDateText = (TextView) v.findViewById(R.id.incomeDateText);
        TextView incomeCategoryText = (TextView) v.findViewById(R.id.incomeCategoryText);
        TextView incomeNameText = (TextView) v.findViewById(R.id.incomeNameText);
        TextView incomeAmountText = (TextView) v.findViewById(R.id.incomeAmountText);

        incomeNameText.setText(incomeList.get(i).getName());
        incomeDateText.setText(incomeList.get(i).getDate());
        incomeCategoryText.setText(incomeList.get(i).getCategory());
        incomeAmountText.setText(incomeList.get(i).getAmount());

        v.setTag(incomeList.get(i).getName());
        return v;
    }
}
