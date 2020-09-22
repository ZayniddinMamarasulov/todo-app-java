package uz.mamarasulov.todoappjava.repo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;

import uz.mamarasulov.todoappjava.db.TodoDatabase;
import uz.mamarasulov.todoappjava.model.Note;
import uz.mamarasulov.todoappjava.util.AppUtils;

/**
 * Created by Zayniddin on Sep 09 - 04:02
 */
public class TodoRepository {

    private String DB_NAME = "db_task";

    private TodoDatabase todoDatabase;

    public TodoRepository(Context context) {
        todoDatabase = Room.databaseBuilder(context, TodoDatabase.class, DB_NAME).build();
    }

    public void insertTask(String title, String description) {
        insertTask(title, description, false, null);

    }

    public void insertTask(String title,
                           String description,
                           boolean isEncryted,
                           String password) {

        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setCreatedAt(AppUtils.getCurrentDateTime());
        note.setModifiedAt(AppUtils.getCurrentDateTime());
        note.setEncrypt(false);

        insertTask(note);
    }

    public void insertTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                todoDatabase.daoAccess().insertTask(note);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Note note) {
        note.setModifiedAt(AppUtils.getCurrentDateTime());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                todoDatabase.daoAccess().updateTask(note);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final int id) {
        final LiveData<Note> task = getTask(id);
        if (task != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    todoDatabase.daoAccess().deleteTask(task.getValue());
                    return null;
                }
            }.execute();
        }
    }

    public void deleteTask(final Note note) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                todoDatabase.daoAccess().deleteTask(note);
                return null;
            }
        }.execute();
    }

    public LiveData<Note> getTask(int id) {
        return todoDatabase.daoAccess().getTask(id);
    }

    public LiveData<List<Note>> getTasks() {
        return todoDatabase.daoAccess().fetchAllTasks();
    }
}
