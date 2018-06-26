package co.za.journalapp.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import co.za.journalapp.data.localRepository.JournalEntryEntity;

public interface JournalRepository {


    interface LoadInfoCallback {

        void onDataLoaded(String success);

        void onDataNotAvailable(String error);
    }

    public interface DeleteInfoCallback {
        void onDataDeleted(String success);

        void onDataNotDeleted(String error);
    }

    void insertEntry(JournalEntryEntity event);

    LiveData<List<JournalEntryEntity>> getAllEntries();

    void deleteEntry(JournalEntryEntity entry);
}
