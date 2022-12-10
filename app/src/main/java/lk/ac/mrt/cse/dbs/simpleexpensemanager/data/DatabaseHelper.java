package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper (Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        String query1 = "create table account(accountNumber varchar(20) primary key, bankName varchar(50),accountHolderName varchar(50), balance double)";
        sqLiteDatabase.execSQL(query1);
        String query2 = "create table Transactions( ID INTEGER PRIMARY KEY AUTOINCREMENT, expenseType varchar(10), amount double, accountNumber varchar(20), date varchar(50), foreign key(accountNumber) references account(accountNumber))";
        sqLiteDatabase.execSQL(query2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,int a, int b){
        sqLiteDatabase.execSQL("Drop table if exists account");
        sqLiteDatabase.execSQL("Drop table if exists Transactions");
        onCreate(sqLiteDatabase);
    }

    public void logTransaction(Date date, String accNo, ExpenseType expType, double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date.toString());
        values.put("accountNumber", accNo);
        values.put("expenseType", String.valueOf(expType));
        values.put("amount", amount);

        // insert row
        db.insert("Transactions", null, values);
        db.close();
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"date","accountNumber","expenseType","amount"};
        Cursor cursor = db.query("Transactions",columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            String data = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String accountNumber = cursor.getString(cursor.getColumnIndexOrThrow("accountNumber"));
            String expenseType = cursor.getString(cursor.getColumnIndexOrThrow("expenseType"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

            ExpenseType expType = null;

            if (expenseType.equals("INCOME")){
                expType = ExpenseType.INCOME;
            }
            else{
                expType = ExpenseType.EXPENSE;
            }
            Date date = null;
            try{
                date = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(data);
            }catch(ParseException e){
                e.printStackTrace();
            }
            Transaction transaction = new Transaction(date,accountNumber,expType,amount);
            transactionsList.add(transaction);
        }
        cursor.close();
        return transactionsList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {

        List<Transaction> transactions = new ArrayList<Transaction>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"date","accountNumber","expenseType","amount"};

        Cursor cursor = db.query("Transactions",columns,null,null,null,null,null);

        int size = cursor.getCount();

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Date date1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(date);
            String accountNumber = cursor.getString(cursor.getColumnIndex("accountNumber"));
            String type = cursor.getString(cursor.getColumnIndex("expenseType"));
            ExpenseType expenseType = ExpenseType.valueOf(type);
            double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            Transaction transaction = new Transaction(date1,accountNumber,expenseType,amount);

            transactions.add(transaction);
        }

        if (size <= limit) {
            return transactions;
        }

        return transactions.subList(size - limit, size);


    }

    public List<String> getAccountNumberList(){
        List<String> accountNumberList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"accountNumber"};
        Cursor cursor = db.query("account",columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            String accountNumber = cursor.getString(cursor.getColumnIndexOrThrow("accountNumber"));
            accountNumberList.add(accountNumber);
        }
        cursor.close();
        return accountNumberList;
    }

    public List<Account> getAccountsList(){
        List<Account> accountsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"accountNumber","bankName","accountHolderName","balance"};
        Cursor cursor = db.query("account",columns,null,null,null,null,null);

        while(cursor.moveToNext()){
            String accountNumber = cursor.getString(cursor.getColumnIndexOrThrow("accountNumber"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            Account account = new Account(accountNumber,bankName,accountHolderName,balance);
            accountsList.add(account);
        }
        cursor.close();
        return accountsList;
    }

    public Account getAccount(String accountNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"accountNumber","bankName","accountHolderName","balance"};
        String selection = "accountNumber = ?";
        String[] selectionArgs = {accountNumber};
        Cursor cursor = db.query("account",columns,selection,selectionArgs,null,null,null);

        Account account = null;
        while(cursor.moveToNext()){
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            account = new Account(accountNumber,bankName,accountHolderName,balance);
        }
        cursor.close();
        return account;
    }

    public void insertData(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("accountNumber",account.getAccountNo());
        values.put("bankName",account.getBankName());
        values.put("accountHolderName",account.getAccountHolderName());
        values.put("balance",account.getBalance());

        long newRowID = db.insert("account",null,values);
    }

    public void deleteData(String accountNumber){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "accountNumber = ?";
        String[] selectionArgs = {accountNumber};
        db.delete("account",selection,selectionArgs);
    }

    public void updateBalance(String accountNumber,ExpenseType expenseType,double amount){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "accountNumber = ?";
        String[] selectionArgs = {accountNumber};
        ContentValues values = new ContentValues();
        Account account = getAccount(accountNumber);
        double balance = account.getBalance();
        if (expenseType.equals(ExpenseType.INCOME)){
            balance = balance + amount;
        }
        else{
            balance = balance - amount;
        }
        values.put("balance",balance);
        db.update("account",values,selection,selectionArgs);
    }
}
