package co.za.journalapp.features.viewAllEntries;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EntryListViewModel extends ViewModel {

    private JournalRepositoryImpl journalRepository;


    public EntryListViewModel(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }


    LiveData<List<JournalEntryEntity>> getEntries(){
        return journalRepository.getAllEntries();
    }

}
