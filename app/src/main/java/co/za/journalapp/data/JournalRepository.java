package co.za.journalapp.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface JournalRepository {


    interface LoadInfoCallback {

        void onDataLoaded(int success);

        void onDataNotAvailable(String error);
    }


    void insertEntry(JournalEntryEntity event, String email, LoadInfoCallback callback);

    LiveData<List<JournalEntryEntity>> getAllEntries();

    LiveData<JournalEntryEntity> getEntry(int id);

    void updateEntry(JournalEntryEntity entity, LoadInfoCallback callback);

    void deleteEntry(JournalEntryEntity entry, LoadInfoCallback callback);


}
