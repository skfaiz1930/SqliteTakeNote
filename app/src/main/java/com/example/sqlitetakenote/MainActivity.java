package com.example.sqlitetakenote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.sqlitetakenote.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Adapter adapter;
    List<Model> noteList;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        noteList = new ArrayList<>();

        database = new Database(this);
        fetchAllNotesFromDatabase();

        binding.recycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        adapter = new Adapter(this, MainActivity.this, noteList);
        binding.recycler.setAdapter(adapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
                startActivity(intent);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Model item = adapter.getList().get(position);
                adapter.removeItem(viewHolder.getAdapterPosition());

                Snackbar snackbar = Snackbar.make(binding.layoutMain, "Item Removed", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adapter.restoreItem(item, position);
                                binding.recycler.scrollToPosition(position);
                            }
                        }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);

                                if (!(event == DISMISS_EVENT_ACTION)) {
                                    Database db = new Database(MainActivity.this);
                                    db.deleteSingleItem(item.getId());
                                }

                            }
                        });
                snackbar.setActionTextColor(getResources().getColor(R.color.design_default_color_primary));
                snackbar.show();
            }
        }).attachToRecyclerView(binding.recycler);
    }

    void fetchAllNotesFromDatabase() {
        Cursor cursor = database.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No Notes Exists", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                noteList.add(new Model(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };

        searchView.setOnQueryTextListener(listener);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.delete_all_notes) {
            deleteAllNotes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNotes() {
        Database db = new Database(MainActivity.this);
        db.deleteAllNotes();
        recreate();
    }
}