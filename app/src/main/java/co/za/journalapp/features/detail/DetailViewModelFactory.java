package co.za.journalapp.features.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import co.za.journalapp.data.JournalRepositoryImpl;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private JournalRepositoryImpl journalRepository;


    public DetailViewModelFactory(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(journalRepository);
    }
}
