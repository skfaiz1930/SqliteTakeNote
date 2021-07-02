package com.example.sqlitetakenote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {

    Context context;
    Activity activity;
    List<Model> noteList;
    List<Model> newList;
    Animation anim;
    

    public Adapter(Context context, Activity activity, List<Model> noteList) {
        this.context = context;
        this.activity = activity;
        this.noteList = noteList;
        newList = new ArrayList<>(noteList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
        holder.title.setText(noteList.get(position).getTitle());
        holder.description.setText(noteList.get(position).getDescription());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateNotesActivity.class);
                intent.putExtra("title", noteList.get(position).getTitle());
                intent.putExtra("description", noteList.get(position).getDescription());
                intent.putExtra("id", noteList.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Model item : newList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteList.clear();
            noteList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public List<Model> getList() {
        return noteList;
    }

    public void removeItem(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Model item, int position) {
        noteList.add(position, item);
        notifyItemInserted(position);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        RelativeLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            layout = itemView.findViewById(R.id.note_layout);
            anim = AnimationUtils.loadAnimation(context,R.anim.animation);
            layout.setAnimation(anim);
        }
    }

}
