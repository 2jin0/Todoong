package com.jica.todochack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todoong.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터베이스가 생성이 될 때 호출
        //데이터베이스 -> 테이블 -> 컬럼 -> 값
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT NOT NULL, writeDate TEXT NOT NULL, checkBox TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //SELECT 문 (할일 목록들을 조회)

    public ArrayList<TodoItem> getTodoList() {
        String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return getTodoListFromDB(curDate);
    }

    public ArrayList<TodoItem> getTodoList(String curDate) {
        return getTodoListFromDB(curDate);
    }

    public ArrayList<String> loadCompleateDate() {
        ArrayList<String> compleateDate = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT DISTINCT writeDate " +
                "FROM TodoList t " +
                "WHERE NOT EXISTS (SELECT * " +
                "                  FROM TodoList " +
                "                  WHERE t.writeDate = writeDate AND checkbox = 'false')";

        Log.d("TAG", "실행 sql : " + sql);

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            //조회온 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {   //다음으로 이동할 데이터가 있을때까지
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));
                compleateDate.add(writeDate);
            }
        }
        cursor.close();
        return compleateDate;
    }

    private ArrayList<TodoItem> getTodoListFromDB(String curDate) {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM TodoList WHERE writeDate = '" + curDate + "' ORDER BY writeDate DESC";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            //조회온 데이터가 있을때 내부 수행
            while (cursor.moveToNext()) {   //다음으로 이동할 데이터가 있을때까지
                int id = cursor.getInt(cursor.getColumnIndex("id"));    // 체크박스, 메뉴버튼 추가?
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));
                String checkBox = cursor.getString(cursor.getColumnIndex("checkBox"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItem.setCheckBox(checkBox);

                Log.d("TAG", "DBHelper-getTodoList(String) 날짜값:" + writeDate);

                todoItems.add(todoItem);
            }
        }
        cursor.close();
        return todoItems;
    }

    //INSERT문(할일 목록을 DB에 넣는다.)
    public void InsertTodo(String _content, String _writeDate, String _checkBox) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList (content, writeDate, checkBox) VALUES('" + _content + "', '" + _writeDate + "', '" + _checkBox + "');");   //JAVA언어가 아니라 SQL명령어
    }

    //UPDATE 문(DB 내용을 수정한다.)
    public void UpdateTodo(String _content, String _writeDate, int _id, String _checkBox) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET content='" + _content + "', writeDate='" + _writeDate + "', checkBox='" + _checkBox + "' WHERE id='" + _id + "'");

    }

    public void UpdateTodo(int _id, String _checkBox) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET checkBox='" + _checkBox + "' WHERE  id = " + _id);
    }

    //DELETE 문 (DB에서 제거한다.)
    public void DeleteTodo(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM TodoList WHERE id='" + _id + "'");
            Log.d(TAG, _id + " 자료의 row를 삭제했습니다.");

        } catch (Exception ex) {
            Log.e(TAG, "Exception in executing delete SQL.", ex);
        }

    }

}
