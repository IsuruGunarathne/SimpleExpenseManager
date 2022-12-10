package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private final Context context;

    public PersistentExpenseManager(Context context){
        this.context = context;
    }

    @Override
    public void setup() throws ExpenseManagerException{
        DatabaseHelper databaseHelper = new DatabaseHelper(context,"200189M",null,1);
        TransactionDAO transactionDAO = new PersistentTransactionDAO(databaseHelper);
        setTransactionsDAO(transactionDAO);
        AccountDAO accountDAO = new PersistentAccountDAO(databaseHelper);
        setAccountsDAO(accountDAO);
    }
}
