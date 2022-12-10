package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return this.databaseHelper.getAccountNumberList();
    }

    /***
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    public List<Account> getAccountsList(){
        return this.databaseHelper.getAccountsList();
    }

    /***
     * Get the account given the account number.
     *
     * @param accountNumber as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    public Account getAccount(String accountNumber) throws InvalidAccountException {
        return this.databaseHelper.getAccount(accountNumber);
    }

    /***
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    public void addAccount(Account account){

        databaseHelper.insertData(account);
    }

    /***
     * Remove an account from the accounts collection.
     *
     * @param accountNumber - of the account to be removed.
     * @throws InvalidAccountException - if the account number is invalid
     */
    public void removeAccount(String accountNumber) throws InvalidAccountException{
        databaseHelper.deleteData(accountNumber);
    }

    /***
     * Update the balance of the given account. The type of the expense is specified in order to determine which
     * action to be performed.
     * <p/>
     * The implementation has the flexibility to figure out how the updating operation is committed based on the type
     * of the transaction.
     *
     * @param accountNumber   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException - if the account number is invalid
     */
    public void updateBalance(String accountNumber, ExpenseType expenseType, double amount) throws InvalidAccountException{
        databaseHelper.updateBalance(accountNumber, expenseType, amount);   
    }

}
