package co.za.journalapp.features.detail;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.Constants;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import co.za.journalapp.data.remoteRepository.RemoteDataSource;


public class DetailActivity extends AppCompatActivity {

    private static final String ENTRY_ID = "ENTRY_ID";
    private DetailViewModel detailViewModel;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private LocalDataSource localDataSource;
    int entry_id;
    private EditText editEntry;
    JournalEntryEntity mJournalEntryEntity;
    @BindView(R.id.my_switcher)ViewSwitcher switcher;
    @BindView(R.id.tv_time)TextView tv_time;
    @BindView(R.id.tv_date) TextView tv_date;
    @BindView(R.id.tv_entry) TextView tv_entry;
    @BindView(R.id.btn_update) Button btn_update;
    private String email;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        LocalDataSource localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        RemoteDataSource remoteDataSource = new RemoteDataSource(AppExecutors.getInstance(), this);
        journalRepository = new JournalRepositoryImpl(localDataSource, remoteDataSource, mDb);
        setupViewModel();
        email = sharedpreferences.getString(Constants.EMAIL, "");

    }

    private void setupViewModel() {
        entry_id = getIntent().getIntExtra(ENTRY_ID, 0);
        ViewModelProvider.Factory addDetailViewModel = new DetailViewModelFactory(journalRepository);
        detailViewModel = ViewModelProviders.of(this, addDetailViewModel)
                .get(DetailViewModel.class);
        //gets entry by id
        detailViewModel.getEntry(entry_id).observe(this, new Observer<JournalEntryEntity>() {
            @Override
            public void onChanged(@Nullable JournalEntryEntity journalEntryEntity) {
                mJournalEntryEntity = journalEntryEntity;
                populateUi(journalEntryEntity);
            }
        });
    }

    private void populateUi(JournalEntryEntity journalEntryEntity) {
        //set date,time, entry
        tv_date.setText(journalEntryEntity.getDate());
        tv_time.setText(journalEntryEntity.getTime());
        tv_entry.setText(journalEntryEntity.getWrittenEntry());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        if(menuItemThatWasSelected == R.id.menu_update_post) {
            updateClicked();
        }
        return true;
    }

    public void updateClicked() {
        switcher.showNext(); //or switcher.showPrevious();
        editEntry = (EditText) switcher.findViewById(R.id.hidden_et_entry);
        editEntry.setText(mJournalEntryEntity.getWrittenEntry());
        btn_update.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_update)
    public  void updatetEntry(){
        JournalEntryEntity updateJournalEntryEntity = new JournalEntryEntity(mJournalEntryEntity.getId(), mJournalEntryEntity.getDate(),
        mJournalEntryEntity.getTime(), editEntry.getText().toString(), email);
        detailViewModel.updateEntry(updateJournalEntryEntity);
        finish();
    }


}
