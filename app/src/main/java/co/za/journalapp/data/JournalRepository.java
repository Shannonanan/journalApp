package co.za.journalapp.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;

public interface JournalRepository {


    interface LoadInfoCallback {

        void onDataLoaded(String success);

        void onDataNotAvailable(String error);
    }


    //completeable is only indication of completion or exception
    Completable insertEntry(JournalEntryEntity event);

    LiveData<List<JournalEntryEntity>> getAllEntries();

    LiveData<JournalEntryEntity> getEntry(int id);

    Completable updateEntry(JournalEntryEntity entity);

  //  Completable deleteEntry(JournalEntryEntity entry);
}
