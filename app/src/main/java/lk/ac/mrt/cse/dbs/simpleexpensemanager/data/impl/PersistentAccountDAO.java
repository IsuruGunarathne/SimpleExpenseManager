package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

public class PersistentAccountDAO implements AccountDAO{
    private DatbaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        this.databaseHelper.getAccountNumbersList();
    }

    /***
     * Get a list of accounts.
     *
     * @return - list of Account objects.
     */
    public List<Account> getAccountsList(){
        this.databaseHelper.getAccountsList();
    }

    /***
     * Get the account given the account number.
     *
     * @param accountNo as String
     * @return - the corresponding Account
     * @throws InvalidAccountException - if the account number is invalid
     */
    public Account getAccount(String accountNo) throws InvalidAccountException{
        return this.databaseHelper.getAccount(accountNumber);
    }

    /***
     * Add an account to the accounts collection.
     *
     * @param account - the account to be added.
     */
    public void addAccount(Account account){
        databaseHelper.insertData();
    }

    /***
     * Remove an account from the accounts collection.
     *
     * @param accountNo - of the account to be removed.
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
     * @param accountNo   - account number of the respective account
     * @param expenseType - the type of the transaction
     * @param amount      - amount involved
     * @throws InvalidAccountException - if the account number is invalid
     */
    public void updateBalance(String accountNumber, ExpenseType expenseType, double amount) throws InvalidAccountException{
        databaseHelper.updateBalance(accountNumber, expenseType, amount);   
    }

}
