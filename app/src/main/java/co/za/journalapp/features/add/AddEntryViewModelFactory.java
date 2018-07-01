package co.za.journalapp.features.add;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;

public class AddEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final JournalEntryDatabase mDb;
    private JournalRepositoryImpl journalRepository;
    private final int mEntryId;

    public AddEntryViewModelFactory(JournalEntryDatabase mDb, JournalRepositoryImpl journalRepository, int mEntryId) {
        this.mDb = mDb;
        this.journalRepository = journalRepository;
        this.mEntryId = mEntryId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddEntryViewModel(mDb, journalRepository, mEntryId);
    }
}
