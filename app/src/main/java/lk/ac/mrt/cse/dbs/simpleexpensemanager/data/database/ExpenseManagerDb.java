package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;


public class ExpenseManagerDb extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "ExpenseManager-190287R.db";
    private final static String TABLE_ACCOUNT = "accounts";
    private final static String TABLE_TRANSACTION = "transactions";
    private Context context;

    public ExpenseManagerDb(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table " + TABLE_ACCOUNT + " (accountNo varchar(20) primary key, bankName varchar(20) not null, accountHolderName varchar(20) not null, balance decimal(10, 2) not null)";
        sqLiteDatabase.execSQL(query);

        query = "create table " + TABLE_TRANSACTION + " (tid integer primary key autoincrement, date varchar(20) not null, accountNo varchar(20) not null, expenseType varchar(20) not null, amount decimal(10, 2) not null)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_TRANSACTION);
        onCreate(sqLiteDatabase);
    }

    public String getAccountTableName() { return TABLE_ACCOUNT; }

    public String getTransactionTableName() { return TABLE_TRANSACTION; }

    public void toastMessage(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
