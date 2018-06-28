package co.za.journalapp.features.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import co.za.journalapp.AppExecutors;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;

public class DetailActivity extends AppCompatActivity {

    private static final String ENTRY_ID = "ENTRY_ID";
    private DetailViewModel detailViewModel;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private LocalDataSource localDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        journalRepository = new JournalRepositoryImpl(localDataSource, mDb);
        setupViewModel();

    }

    private void setupViewModel() {
        int entry_id = getIntent().getIntExtra(ENTRY_ID, 0);
        ViewModelProvider.Factory addDetailViewModel = new DetailViewModelFactory(journalRepository);
        detailViewModel = ViewModelProviders.of(this, addDetailViewModel)
                .get(DetailViewModel.class);
        //gets entry by id
        detailViewModel.getEntry(entry_id).observe(this, new Observer<JournalEntryEntity>() {
            @Override
            public void onChanged(@Nullable JournalEntryEntity journalEntryEntity) {
                populateUi(journalEntryEntity);
            }
        });
    }

    private void populateUi(JournalEntryEntity journalEntryEntity) {
        //setdate,time, entry
        Toast.makeText(this, "update", Toast.LENGTH_LONG).show();

    }
}
