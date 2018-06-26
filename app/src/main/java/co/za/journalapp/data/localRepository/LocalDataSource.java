package co.za.journalapp.data.localRepository;

import android.arch.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import co.za.journalapp.AppExecutors;
import co.za.journalapp.data.JournalRepository;

public class LocalDataSource implements JournalRepository {

    private final JournalEntryDao journalEntryDao;
    private final AppExecutors mExecutors;

    public LocalDataSource(JournalEntryDao journalEntryDao, AppExecutors mExecutors) {
        this.journalEntryDao = journalEntryDao;
        this.mExecutors = mExecutors;
    }


    @Override
    public void insertEntry(JournalEntryEntity event) {
        journalEntryDao.insertEntry(event);

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
            return journalEntryDao.loadAllEntries();
    }


    @Override
    public void deleteEntry(JournalEntryEntity entry) {
         journalEntryDao.deleteTask(entry);
    }
}
