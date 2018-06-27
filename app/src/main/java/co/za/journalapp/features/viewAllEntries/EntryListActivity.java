package co.za.journalapp.features.viewAllEntries;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import co.za.journalapp.features.add.AddEntryActivity;

public class EntryListActivity extends AppCompatActivity implements LoadAllEntriesAdapter.ItemClickListener {

    // Constant for logging
    private static final String TAG = EntryListActivity.class.getSimpleName();
    @BindView(R.id.fab) FloatingActionButton fabButton;
    @BindView(R.id.recyclerview_Entries) RecyclerView recyclerview;
    private LoadAllEntriesAdapter mAdapter;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private LocalDataSource localDataSource;
    private EntryListViewModel entryListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupRecyclerView();
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an AddTaskActivity
                Intent addTaskIntent = new Intent(EntryListActivity.this, AddEntryActivity.class);
                startActivity(addTaskIntent);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int positionOfEntry = viewHolder.getAdapterPosition();
                        List<JournalEntryEntity>entries = mAdapter.getEntryCollection();
                        mDb.journalEntryDao().deleteTask(entries.get(positionOfEntry));
                    }
                });

            }
        }).attachToRecyclerView(recyclerview);

        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        journalRepository = new JournalRepositoryImpl(localDataSource);
        setupViewModel();
    }

    private void setupViewModel() {
        ViewModelProvider.Factory allEntriesViewModelFactory = new AllEntriesViewModelFactory(journalRepository);
        entryListViewModel = ViewModelProviders.of(this, allEntriesViewModelFactory)
                .get(EntryListViewModel.class);
        entryListViewModel.getEntries().observe(this, new Observer<List<JournalEntryEntity>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntryEntity> journalEntryEntities) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setInfoCollection(journalEntryEntities);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LoadAllEntriesAdapter(this,this);
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onItemClickListener(int itemId) {
        //This will be to go to Edit task Activity
    }
}
