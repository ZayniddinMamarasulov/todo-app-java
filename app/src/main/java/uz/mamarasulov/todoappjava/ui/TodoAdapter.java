package uz.mamarasulov.todoappjava.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import uz.mamarasulov.todoappjava.R;
import uz.mamarasulov.todoappjava.model.Note;
import uz.mamarasulov.todoappjava.util.AppUtils;
import uz.mamarasulov.todoappjava.util.NoteDiffUtil;

/**
 * Created by Zayniddin on Sep 09 - 03:30
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    private List<Note> notes;

    public TodoAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = getItem(position);

        holder.itemTitle.setText(note.getTitle());
        holder.itemDescription.setText(note.getDescription());
        holder.itemTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getItem(int position) {
        return notes.get(position);
    }

    public void addTasks(List<Note> newNotes) {
        NoteDiffUtil noteDiffUtil = new NoteDiffUtil(notes, newNotes);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(noteDiffUtil);
        notes.clear();
        notes.addAll(newNotes);
        diffResult.dispatchUpdatesTo(this);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTitle, itemDescription, itemTime;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.item_title);
            itemDescription = itemView.findViewById(R.id.item_desc);
            itemTime = itemView.findViewById(R.id.item_time);
        }

    }
}
