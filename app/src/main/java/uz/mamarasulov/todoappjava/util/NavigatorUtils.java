package uz.mamarasulov.todoappjava.util;

import android.app.Activity;
import android.content.Intent;

import uz.mamarasulov.todoappjava.ui.AddTodoActivity;
import uz.mamarasulov.todoappjava.model.Note;

/**
 * Created by Zayniddin on Sep 09 - 04:21
 */
public class NavigatorUtils implements AppConstants {

    public static void redirectToEditTaskScreen(Activity activity,
                                                Note note) {
        Intent intent = new Intent(activity, AddTodoActivity.class);
        intent.putExtra(INTENT_TASK, note);
        activity.startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    public static void redirectToViewNoteScreen(Activity activity,
                                                Note note) {
        Intent intent = new Intent(activity, AddTodoActivity.class);
        intent.putExtra(INTENT_TASK, note);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        activity.startActivity(intent);
        activity.finish();
    }
}
