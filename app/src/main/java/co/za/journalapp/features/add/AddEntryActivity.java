package co.za.journalapp.features.add;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.RoomDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;

public class AddEntryActivity extends AppCompatActivity {


    private static final int DEFAULT_TASK_ID = -1;
    @BindView(R.id.et_entry)EditText et_entry;
    @BindView(R.id.tv_date)TextView tv_date;
    @BindView(R.id.tv_time) TextView tv_time;


    private AddEntryViewModel addEntryViewModel;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private LocalDataSource localDataSource;


    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        ButterKnife.bind(this);

        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        journalRepository = new JournalRepositoryImpl(localDataSource);

        setupClickListeners();
        setupViewModel();
    }


    private void setupViewModel() {
        ViewModelProvider.Factory addEntryViewModelFactory = new AddEntryViewModelFactory(mDb, journalRepository, mTaskId);
        addEntryViewModel = ViewModelProviders.of(this, addEntryViewModelFactory)
                .get(AddEntryViewModel.class);
        et_entry.setText(addEntryViewModel.getEntry());
        tv_date.setText("date 1");
        tv_time.setText("time 1");
    }


    private void setupClickListeners() {
        et_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addEntryViewModel.setEntry(editable.toString());
            }
        });
        tv_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addEntryViewModel.setDate("test 1");
            }
        });
        tv_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addEntryViewModel.setTime("test Time 1");
            }
        });
    }

    @OnClick(R.id.btn_post)
    public  void postEntry()
    {


        final JournalEntryEntity task = new JournalEntryEntity("test", "test", "test");
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mTaskId == DEFAULT_TASK_ID) {
                    // insert new task
                    mDb.journalEntryDao().insertEntry(task);
                } else {
                    //update task
                    task.setId(mTaskId);
                    mDb.journalEntryDao().updateTask(task);
                }
                finish();
            }
        });
//        addEntryViewModel.addEntry();
//        Toast.makeText(this, "journal entry saved", Toast.LENGTH_LONG).show();
//        finish();

    }
}
