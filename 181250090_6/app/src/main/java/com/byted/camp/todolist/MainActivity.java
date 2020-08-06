package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.FontsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.operation.db.FeedReaderContract;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.byted.camp.todolist.db.TodoContract.TodoEntry.COLUME_NAME_STATE;
import static com.byted.camp.todolist.db.TodoContract.TodoEntry.COLUMN_NAME_CONTENT;
import static com.byted.camp.todolist.db.TodoContract.TodoEntry.COLUMN_NAME_DATE;
import static com.byted.camp.todolist.db.TodoContract.TodoEntry.COLUMN_NAME_PRIORITY;
import static com.byted.camp.todolist.db.TodoContract.TodoEntry.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;
    private TodoDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new TodoDbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
                notesAdapter.refresh(loadNotesFromDatabase());
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
                notesAdapter.refresh(loadNotesFromDatabase());
            }
        });
        recyclerView.setAdapter(notesAdapter);

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Note> todo = new ArrayList<>();

        String[] projection = {
                BaseColumns._ID,
                TodoContract.TodoEntry.COLUMN_NAME_DATE,
                TodoContract.TodoEntry.COLUME_NAME_STATE,
                TodoContract.TodoEntry.COLUMN_NAME_CONTENT,
                TodoContract.TodoEntry.COLUMN_NAME_PRIORITY
        };

        String sortOrder = _ID + " DESC";

        //从数据库中查询数据并放在cursor中
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );


        while(cursor.moveToNext())
        {
            Note temp = new Note(cursor.getLong(cursor.getColumnIndexOrThrow(TodoContract.TodoEntry._ID)));
            String myDate = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = format.parse(myDate);
                temp.setDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT)));
            int s= (int) cursor.getLong(cursor.getColumnIndex(COLUME_NAME_STATE));
            State state;
            if(s==0)
            {
                state=State.TODO;
            }else{
                state=State.DONE;
            }

            temp.setState(state);
            todo.add(temp);

        }
        db.close();

        return todo;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = _ID+" = ?";
        String[] selectionargs = {Integer.toString((int) note.id)};
        db.delete(TABLE_NAME,selection,selectionargs);

        db.close();

    }

    private void updateNode(Note note) {
        // 更新数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        long state = note.getState().intValue;
        ContentValues values = new ContentValues();
        values.put(COLUME_NAME_STATE,state);

        //两种方法都可以
//        String selection=_ID+" LIKE ?";
//        String[] selectionargs={Integer.toString((int) note.id)};
//
//        db.update(TABLE_NAME,
//                values,
//                selection,
//                selectionargs);

        String selection = _ID + " = " + note.id;

        db.update(TABLE_NAME,
                values,
                selection,
                null);

        db.close();
    }

}
