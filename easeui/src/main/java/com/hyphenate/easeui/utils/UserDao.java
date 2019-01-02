package com.hyphenate.easeui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushenghui on 2017/8/21.
 */

public class UserDao {
    private static final String DB_NAME = "wbxchatusermall.db";//数据库名称
    private static final String TABLE_NAME = "hxuser";//数据表名称
    private static final int DB_VERSION = 1;//数据库版本
    //表的字段名
    private static String KEY_ID = "id";
    private static String KEY_NICK_NAME = "nickname";
    private static String KEY_AVA = "ava";
    private static String KEY_USER_NAME = "username";
    private SQLiteDatabase mDatabase;
    private Context mContext;
    private TreeDBOpenHelper mDbOpenHelper;//数据库打开帮助类
    public UserDao(Context context){
        mContext = context;
    }
    //打开数据库
    public void openDataBase() {
        mDbOpenHelper = new TreeDBOpenHelper(mContext, DB_NAME, null, DB_VERSION);
        try {
            mDatabase = mDbOpenHelper.getWritableDatabase();//获取可写数据库
        } catch (SQLException e) {
            mDatabase = mDbOpenHelper.getReadableDatabase();//获取只读数据库
        }
    }

    //关闭数据库
    public void closeDataBase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    //插入一条数据
    public long insertData(EaseUser user) {
        ContentValues values = new ContentValues();
        String nickname = user.getNickname();
        values.put(KEY_USER_NAME,user.getUsername());
        values.put(KEY_NICK_NAME, nickname);
        values.put(KEY_AVA, user.getAvatar());
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    //删除一条数据
    public long deleteData(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null);
    }

    //删除所有数据
    public long deleteAllData() {
        return mDatabase.delete(TABLE_NAME, null, null);
    }

    //更新一条数据
    public long updateData(long id, EaseUser user) {
        ContentValues values = new ContentValues();
        values.put(KEY_NICK_NAME, user.getNickname());
        values.put(KEY_AVA, user.getAvatar());
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null);
    }

    //查询一条数据
    public EaseUser queryData(String name) {
        Cursor cursor = mDatabase.rawQuery("select * from "+TABLE_NAME +" where "+KEY_USER_NAME +" like ? ",new String[]{name});
        EaseUser easeUser = new EaseUser(name);
        while (cursor.moveToNext()){
                easeUser.setNickname(cursor.getString(2));
                easeUser.setAvatar(cursor.getString(3));
        }
        return easeUser;
    }

    //查询所有数据
    public List<EaseUser> queryDataList() {
//        Cursor results = mDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_AGE, KEY_PRICE},
//                null, null, null, null, null);
        return null;

    }

    private List<EaseUser> convertToTree(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }
        List<EaseUser> mTreeList = new ArrayList<>();
        for (int i = 0; i < resultCounts; i++) {
//            EaseUser tree = new EaseUser);
//            tree.setId(cursor.getInt(0));
//            tree.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
//            tree.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
//            tree.setPrice(cursor.getFloat(cursor.getColumnIndex(KEY_PRICE)));
//            mTreeList.add(tree);
            cursor.moveToNext();
        }
        return mTreeList;
    }

    /**
     * 数据表打开帮助类
     */
    private static class TreeDBOpenHelper extends SQLiteOpenHelper {

        public TreeDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            final String sqlStr = "create table if not exists " + TABLE_NAME + " (" + KEY_ID + " integer primary key autoincrement, " + KEY_USER_NAME + " text, " + KEY_NICK_NAME+" text ,"+ KEY_AVA + " text );";
            db.execSQL(sqlStr);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            final String sqlStr = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sqlStr);
            onCreate(db);
        }
    }
}
