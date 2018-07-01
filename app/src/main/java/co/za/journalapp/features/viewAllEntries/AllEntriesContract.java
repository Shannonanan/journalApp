package co.za.journalapp.features.viewAllEntries;

import java.util.List;

import co.za.journalapp.data.localRepository.JournalEntryEntity;

public interface AllEntriesContract {

    void onUpdateSuccess(String status);
    void onEntriesLoaded(List<JournalEntryEntity> list);
}
