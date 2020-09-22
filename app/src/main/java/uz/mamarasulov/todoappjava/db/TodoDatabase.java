package uz.mamarasulov.todoappjava.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import uz.mamarasulov.todoappjava.dao.DaoAccess;
import uz.mamarasulov.todoappjava.model.Note;

/**
 * Created by Zayniddin on Sep 09 - 04:03
 */

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
