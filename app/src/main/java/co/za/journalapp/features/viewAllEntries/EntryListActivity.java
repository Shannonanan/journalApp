package co.za.journalapp.features.viewAllEntries;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.Constants;
import co.za.journalapp.R;
import co.za.journalapp.authentication.GoogleSignInActivity;
import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import co.za.journalapp.data.remoteRepository.RemoteDataSource;
import co.za.journalapp.features.add.AddEntryActivity;
import co.za.journalapp.features.detail.DetailActivity;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class EntryListActivity extends AppCompatActivity implements LoadAllEntriesAdapter.ItemClickListener, AllEntriesContract {

    // Constant for logging
    private static final String TAG = EntryListActivity.class.getSimpleName();
    @BindView(R.id.fab) FloatingActionButton fabButton;
    @BindView(R.id.recyclerview_Entries) RecyclerView recyclerview;
    @BindView(R.id.tv_noPosts) TextView no_posts_view;
    @BindView(R.id.rl_progress_lottie) RelativeLayout rl_progress;
    @BindView(R.id.animation_view) LottieAnimationView pb_progress;
    private LoadAllEntriesAdapter mAdapter;
    private JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private EntryListViewModel entryListViewModel;
    private static final String ENTRY_ID = "ENTRY_ID";
    private String email;
    private static final String USER_EMAIL = "usersEmail";
    boolean checkRemote = true;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setupRecyclerView();
        setupFab();


          sharedpreferences = getApplicationContext().getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);

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
                        JournalEntryEntity entryEntity = entries.get(positionOfEntry);
                        entryListViewModel.deleteEntryInRemote(entryEntity);
                    }
                });

            }
        }).attachToRecyclerView(recyclerview);

        mDb = JournalEntryDatabase.getInstance(getApplicationContext());
        JournalEntryDao journalEntryDao = mDb.journalEntryDao();
        LocalDataSource localDataSource = new LocalDataSource(journalEntryDao, AppExecutors.getInstance());
        RemoteDataSource remoteDataSource = new RemoteDataSource(AppExecutors.getInstance(), this);
        journalRepository = new JournalRepositoryImpl(localDataSource, remoteDataSource, mDb);
        setupViewModel();
    }

    private void setupFab() {
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(EntryListActivity.this, AddEntryActivity.class);
                startActivity(addTaskIntent);
            }
        });
    }


    private void setupViewModel() {
        ViewModelProvider.Factory allEntriesViewModelFactory = new AllEntriesViewModelFactory(journalRepository);
        entryListViewModel = ViewModelProviders.of(this, allEntriesViewModelFactory)
                .get(EntryListViewModel.class);
        entryListViewModel.setView(this);

        if(checkRemote){
            showLoading();
            entryListViewModel.getEntriesRemotely(sharedpreferences.getString(Constants.EMAIL, ""));
            checkRemote = false;
        }

    }

    public void observeSwitchOn(){
        entryListViewModel.getEntries().observe(this, new Observer<List<JournalEntryEntity>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntryEntity> journalEntryEntities) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                if(!journalEntryEntities.isEmpty())
                {
                    mAdapter.setInfoCollection(journalEntryEntities);
                    if((no_posts_view.getVisibility() == View.VISIBLE)){
                        no_posts_view.setVisibility(View.GONE);
                    }}
            }
        });
    }

    private void setupRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new LoadAllEntriesAdapter(this,this);
        recyclerview.setAdapter(mAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        recyclerview.addItemDecoration(decoration);
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ENTRY_ID, itemId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        switch (menuItemThatWasSelected) {
            case R.id.menu_sign_out:
                GoogleSignInActivity.signOutFromMain();
                finish();
                break;
            case R.id.menu_settings:
                //go to preference frag
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
       Toast.makeText(this, getString(R.string.sign_out_press), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpdateSuccess(String status) {
        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEntriesLoaded(List<JournalEntryEntity> list) {
        hideLoading();
        if(!list.isEmpty()){
        mAdapter.setInfoCollection(list);
        if((no_posts_view.getVisibility() == View.VISIBLE)){
            no_posts_view.setVisibility(View.GONE);
        }}
        else{
            no_posts_view.setVisibility(View.VISIBLE);
        }

        observeSwitchOn();
    }


    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
    }


    public void hideLoading() {
        if (rl_progress != null) {
            this.rl_progress.setVisibility(View.GONE);
        }
    }
}
