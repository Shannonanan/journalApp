package co.za.journalapp.features.add;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


public class AddEntryActivity extends AppCompatActivity {


    private static final int DEFAULT_TASK_ID = -1;
    @BindView(R.id.et_entry)EditText et_entry;
    @BindView(R.id.tv_date)TextView tv_date;
    @BindView(R.id.tv_time) TextView tv_time;


    private AddEntryViewModel addEntryViewModel;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;

    private String email;
    SharedPreferences sharedpreferences;


    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        ButterKnife.bind(this);
        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        LocalDataSource localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        RemoteDataSource remoteDataSource = new RemoteDataSource(AppExecutors.getInstance(), this);
        journalRepository = new JournalRepositoryImpl(localDataSource, remoteDataSource, mDb);

        setupClickListeners();
        setupViewModel();
        email = sharedpreferences.getString(Constants.EMAIL, "");
    }


    private void setupViewModel() {
        ViewModelProvider.Factory addEntryViewModelFactory = new AddEntryViewModelFactory(mDb, journalRepository, mTaskId);
        addEntryViewModel = ViewModelProviders.of(this, addEntryViewModelFactory)
                .get(AddEntryViewModel.class);
        et_entry.setText(addEntryViewModel.getEntry());
        tv_date.setText(getCurrentDate());
        tv_time.setText(getCurrentTime());

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
    }

    @OnClick(R.id.btn_post)
    public  void postEntry()
    {
        final JournalEntryEntity entry = new JournalEntryEntity(getCurrentDate(), getCurrentTime(), et_entry.getText().toString(), email);
        addEntryViewModel.addEntry(entry, email);
        Toast.makeText(this, getString(R.string.save_post), Toast.LENGTH_LONG).show();
        finish();

    }

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = toDate(c);
        return formattedDate;
    }

    public String getCurrentTime(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = toTime(c);
        return formattedDate;
    }

    public String toDate(Date timestamp) {
        DateTime posted = new DateTime(timestamp);

        int days = posted.toLocalDate().getDayOfMonth();
        String months = posted.monthOfYear().getAsShortText();
        int years = posted.toLocalDate().getYear();
        String dayName = posted.toLocalDate().dayOfWeek().getAsShortText();

        String day = String.valueOf(days);
        String year = String.valueOf(years);

        return dayName + " " + day + " " + months + " " + year;
    }

    public String toTime(Date timestamp) {
        DateTime posted = new DateTime(timestamp);

        int hours = posted.toDateTime().getHourOfDay();
        int minutes = posted.toDateTime().getMinuteOfHour();


        String hoursInDay = String.valueOf(hours);
        String minInDay = String.valueOf(minutes);


        if(hours < 10)
            return "0" + hoursInDay + ":" + minInDay;

        else{
            return hoursInDay + ":" + minInDay;}
    }
}
