package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDb;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InDatabaseAccountDAO implements AccountDAO {
    private final ExpenseManagerDb db;

    public InDatabaseAccountDAO(ExpenseManagerDb db) {
        this.db = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase emDb = db.getReadableDatabase();
        String query = "select accountNo from " + db.getAccountTableName();
        Cursor cursor = emDb.rawQuery(query, null);
        List<String> acNumbers = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                acNumbers.add(cursor.getString(cursor.getColumnIndex("accountNo")));
            } while (cursor.moveToNext());
        }
        emDb.close();
        return acNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase emDb = db.getReadableDatabase();
        String query = "select * from " + db.getAccountTableName();
        Cursor cursor = emDb.rawQuery(query, null);
        List<Account> accounts = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                String accountNo = cursor.getString(cursor.getColumnIndex("accountNo"));
                String bankName = cursor.getString(cursor.getColumnIndex("bankName"));
                String accountHolderName = cursor.getString(cursor.getColumnIndex("accountHolderName"));
                double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
                Account account = new Account(accountNo, bankName, accountHolderName, balance);
                accounts.add(account);
            } while (cursor.moveToNext());
        }
        emDb.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase emDb = db.getReadableDatabase();
        String query = "select * from " + db.getAccountTableName() + " where accountNo='" + accountNo + "'";
        Cursor cursor = emDb.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String accountNum = cursor.getString(cursor.getColumnIndex("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndex("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndex("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
            emDb.close();
            return new Account(accountNum, bankName, accountHolderName, balance);
        }
        emDb.close();
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase emDb = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("accountNo", account.getAccountNo());
        values.put("bankName", account.getBankName());
        values.put("accountHolderName", account.getAccountHolderName());
        values.put("balance", account.getBalance());

        Log.d("InDbAccountDAO", "addAccount: (" + account.getAccountNo() + ", " + account.getBankName() + ")");

        long rowNum = emDb.insert(db.getAccountTableName(), null, values);

        if (rowNum > 0) {
            Log.d("InDbAccountDAO", "Account was added");
            db.toastMessage("Account was added");
        }
        else {
            Log.d("InDbAccountDAO", "Account was not added");
            db.toastMessage("Account was not added");
        }
        emDb.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase emDb = db.getWritableDatabase();
        long rowNum = emDb.delete(db.getAccountTableName(), "accountNo=?", new String[]{accountNo});
        if (rowNum > 0) {
            Log.d("InDbAccountDAO", "Account was removed");
            db.toastMessage("Account was removed");
            emDb.close();
        }
        else {
            Log.d("InDbAccountDAO", "Account was not removed");
            db.toastMessage("Account was not removed");
            emDb.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double balance = getAccount(accountNo).getBalance();
        if (expenseType.equals(ExpenseType.INCOME)) {
            balance += amount;
        }
        else if (expenseType.equals(ExpenseType.EXPENSE)) {
            balance -= amount;
        }

        SQLiteDatabase emDb = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("balance", balance);
        long rowNum = emDb.update(db.getAccountTableName(), values, "accountNo=?", new String[]{accountNo});
        if (rowNum > 0) {
            Log.d("InDbAccountDAO", "Account balance was updated");
            db.toastMessage("Account balance was updated");
            emDb.close();
        }
        else {
            Log.d("InDbAccountDAO", "Account balance was not updated");
            db.toastMessage("Account balance was not updated");
            emDb.close();
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
}
