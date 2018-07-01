package co.za.journalapp.data.localRepository;

import android.arch.lifecycle.LiveData;
import java.util.List;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.data.JournalRepository;
import io.reactivex.Completable;


import static com.google.common.base.Preconditions.checkNotNull;

public class LocalDataSource implements JournalRepository {

    private final JournalEntryDao journalEntryDao;
    private final AppExecutors mExecutors;


    public LocalDataSource(JournalEntryDao journalEntryDao, AppExecutors mExecutors) {
        this.journalEntryDao = journalEntryDao;
        this.mExecutors = mExecutors;
    }


    @Override
    public void insertEntry(final JournalEntryEntity entry, String email, final LoadInfoCallback callback) {
        checkNotNull(entry);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                long getIndex =  journalEntryDao.insertEntry(entry);
                int i = (int) getIndex;
                callback.onDataLoaded(i);
            }
        };
        mExecutors.diskIO().execute(saveRunnable);

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
            return journalEntryDao.loadAllEntries();
    }

    @Override
    public LiveData<JournalEntryEntity> getEntry(int id) {
        return null;
    }

    @Override
    public void updateEntry(final JournalEntryEntity entry, final LoadInfoCallback callback) {
        checkNotNull(entry);
        Runnable saveRunnable =  new Runnable() {
            @Override
            public void run() {
                int success = 1;
                journalEntryDao.updateEntry(entry);
                callback.onDataLoaded(success);
            }
        };
        mExecutors.diskIO().execute(saveRunnable);
    }


    @Override
    public void deleteEntry(final JournalEntryEntity entry, final LoadInfoCallback callback) {
        checkNotNull(entry);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                int success = 1;
                journalEntryDao.deleteTask(entry);
                callback.onDataLoaded(success);
            }
        };
        mExecutors.diskIO().execute(saveRunnable);
    }


}
