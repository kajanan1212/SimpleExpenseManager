package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDb;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.InDatabaseTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager{
    private ExpenseManagerDb expenseManagerDb;

    public PersistentExpenseManager(@Nullable Context context) {
        this.expenseManagerDb = new ExpenseManagerDb(context);
        setup();
    }

    @Override
    public void setup() {
        TransactionDAO inDatabaseTransactionDAO = new InDatabaseTransactionDAO(expenseManagerDb);
        setTransactionsDAO(inDatabaseTransactionDAO);

        AccountDAO inDatabaseAccountDAO = new InDatabaseAccountDAO(expenseManagerDb);
        setAccountsDAO(inDatabaseAccountDAO);

        if(!inDatabaseAccountDAO.getAccountNumbersList().contains("190287R")) {
            Account dummyAcct1 = new Account("190287R", "BOC", "Kajanan S.", 10000.0);
            getAccountsDAO().addAccount(dummyAcct1);
        }
    }
}
