package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.ExpenseManagerDb;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InDatabaseTransactionDAO implements TransactionDAO {

    private final ExpenseManagerDb db;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");

    public InDatabaseTransactionDAO(ExpenseManagerDb db) {
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        if (accountNo == null) {
            Log.d("InDbTransactionDAO", "Transaction was not logged");
            db.toastMessage("Transaction was not logged");
            return;
        }
        SQLiteDatabase emDb = db.getWritableDatabase();

        ContentValues values = new ContentValues();

        String strDate = sdf.format(date);

        values.put("date", strDate);
        values.put("accountNo", accountNo);
        values.put("expenseType", expenseType.equals(ExpenseType.EXPENSE)? "EXPENSE": "INCOME");
        values.put("amount", amount);

        Log.d("InDbTransactionDAO", "addTransaction: (" + strDate + ", " + accountNo + ")");

        long rowNum = emDb.insert(db.getTransactionTableName(), null, values);

        if (rowNum > 0) {
            Log.d("InDbTransactionDAO", "Transaction was logged");
            db.toastMessage("Transaction was logged");
        }
        else {
            Log.d("InDbTransactionDAO", "Transaction was not logged");
            db.toastMessage("Transaction was not logged");
        }
        emDb.close();
    }

    private enum LimitType {
        ALL, PAGINATED
    }

    private List<Transaction> getTransactions(LimitType lt, int limit) {
        SQLiteDatabase emDb = db.getReadableDatabase();
        String query = "";
        if (lt.equals(LimitType.ALL)) {
            query = "select * from " + db.getTransactionTableName() + " order by tid desc";
        }
        else if (lt.equals(LimitType.PAGINATED)) {
            query = "select * from " + db.getTransactionTableName() + " order by tid desc limit " + limit;
        }
        Cursor cursor = emDb.rawQuery(query, null);
        List<Transaction> transactions = new LinkedList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Date date = sdf.parse(cursor.getString(cursor.getColumnIndex("date")));
                    String accountNo = cursor.getString(cursor.getColumnIndex("accountNo"));
                    String expenseSType = cursor.getString(cursor.getColumnIndex("expenseType"));
                    ExpenseType expenseType = expenseSType.equals("EXPENSE")? ExpenseType.EXPENSE: ExpenseType.INCOME;
                    double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

                    Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                    transactions.add(transaction);
                } catch (ParseException e) {emDb.close();}
            } while (cursor.moveToNext());
        }
        emDb.close();
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getTransactions(LimitType.ALL, -1);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return getTransactions(LimitType.PAGINATED, limit);
    }
}
