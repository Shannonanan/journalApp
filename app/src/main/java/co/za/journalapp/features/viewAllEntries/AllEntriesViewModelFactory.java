package co.za.journalapp.features.viewAllEntries;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;

@Singleton
public class AllEntriesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final JournalRepositoryImpl journalRepository;

    public AllEntriesViewModelFactory(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new EntryListViewModel(journalRepository);
    }
}
