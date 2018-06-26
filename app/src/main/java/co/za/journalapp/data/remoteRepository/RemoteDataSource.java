package co.za.journalapp.data.remoteRepository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.localRepository.JournalEntryEntity;

public class RemoteDataSource implements JournalRepository {


    @Override
    public void insertEntry(JournalEntryEntity event) {

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
        return null;
    }

    @Override
    public void deleteEntry(JournalEntryEntity entry) {

    }

}
