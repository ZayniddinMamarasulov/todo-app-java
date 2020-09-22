package uz.mamarasulov.todoappjava.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import uz.mamarasulov.todoappjava.R;
import uz.mamarasulov.todoappjava.model.Note;
import uz.mamarasulov.todoappjava.repo.TodoRepository;
import uz.mamarasulov.todoappjava.util.AppConstants;
import uz.mamarasulov.todoappjava.util.AppUtils;
import uz.mamarasulov.todoappjava.util.NavigatorUtils;
import uz.mamarasulov.todoappjava.util.RecyclerItemClickListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        RecyclerItemClickListener.OnRecyclerViewItemClickListener, AppConstants {


    private TextView emptyView;
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private FloatingActionButton floatingActionButton;

    private TodoRepository todoRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.makeStatusBarDark(this);

        todoRepository = new TodoRepository(getApplicationContext());

        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        emptyView = findViewById(R.id.empty_view);

        updateTaskList();
    }

    private void updateTaskList() {
        todoRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (notes.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (todoAdapter == null) {
                        todoAdapter = new TodoAdapter(notes);
                        recyclerView.setAdapter(todoAdapter);

                    } else todoAdapter.addTasks(notes);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onItemClick(View parentView, View childView, int position) {
        Note note = todoAdapter.getItem(position);
        NavigatorUtils.redirectToEditTaskScreen(this, note);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data.hasExtra(INTENT_TASK)) {
                if (data.hasExtra(INTENT_DELETE)) {
                    todoRepository.deleteTask((Note) data.getSerializableExtra(INTENT_TASK));

                } else {
                    todoRepository.updateTask((Note) data.getSerializableExtra(INTENT_TASK));
                }
            } else {
                String title = data.getStringExtra(INTENT_TITLE);
                String desc = data.getStringExtra(INTENT_DESC);
                todoRepository.insertTask(title, desc);
            }
            updateTaskList();
        }
    }
}