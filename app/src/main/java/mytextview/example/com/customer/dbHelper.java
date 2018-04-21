package mytextview.example.com.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Kelvin on admin on 13-Mar-18.
 */

public class dbHelper extends SQLiteOpenHelper {

    private static String dbname="mydatabase";
    private static int dbvirsion=1;
    private static String tbname="data";
    private static String uid="id";
    private static String date="date";
    private static String Uniqued="Uniqued";
    private static String productname="productname";
    private static String Methodselected="Methodselected";

    ArrayList<purchasepojo> ar;
    dbHelper(Context context)
    {
        super(context,dbname,null,dbvirsion);

    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="create table "+tbname+" ("+uid+" integer primary key autoincrement, "+date+" varchar(30), "+Uniqued+" varchar(400), " +productname+" varchar(30), "+Methodselected+" varchar(100))";
        Log.d("query",sql);
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertdata(purchasepojo p)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(date,p.getDate());
        cv.put(Uniqued,p.getUnique());
        cv.put(productname,p.getProductname());
        cv.put(Methodselected,p.getMethodselected());

        db.insert(tbname,null,cv);
    }
    public ArrayList<purchasepojo> getdata()
    {
        ar=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();

        String sql="Select *, "+uid+" as _id from "+tbname;
        Cursor c=db.rawQuery(sql,null);
        while (c.moveToNext())
        {
            purchasepojo p=new purchasepojo();
            int id=c.getInt(0);
            p.setDate(c.getString(c.getColumnIndex(date)));
            p.setUnique(c.getString(c.getColumnIndex(Uniqued)));
            p.setProductname(c.getString(c.getColumnIndex(productname)));
            p.setMethodselected(c.getString(c.getColumnIndex(Methodselected)));
            p.setId(id+"");



            ar.add(p);
        }
        return ar;
    }


}
