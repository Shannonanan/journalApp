package co.za.journalapp.data.remoteRepository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;

public class RemoteDataSource implements JournalRepository {


    @Override
    public Completable insertEntry(JournalEntryEntity event) {
        return null;
    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
        return null;
    }

    @Override
    public LiveData<JournalEntryEntity> getEntry(int id) {
        return null;
    }

    @Override
    public Completable updateEntry(JournalEntryEntity entity) {
        return null;
    }


}
