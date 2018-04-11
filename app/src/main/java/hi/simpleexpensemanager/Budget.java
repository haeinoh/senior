package hi.simpleexpensemanager;

/**
 * Created by Haein on 4/11/2018.
 */

public class Budget {
    String budgetAmount;

    public Budget(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
}

