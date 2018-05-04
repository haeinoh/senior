package hi.simpleexpensemanager;

/**
 * Created by Haein on 5/4/2018.
 */

public class MonthExpense {
    String expenseAmount;
    String expenseCategory;

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public MonthExpense(String expenseAmount, String expenseCategory) {
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }
}
