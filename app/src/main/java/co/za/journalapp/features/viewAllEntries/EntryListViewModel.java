package co.za.journalapp.features.viewAllEntries;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EntryListViewModel extends ViewModel {

    private JournalRepositoryImpl journalRepository;
    private AllEntriesContract allEntriesContract;


    public EntryListViewModel(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }

    public void setView(@NonNull AllEntriesContract view) {
        this.allEntriesContract = view;
    }

    LiveData<List<JournalEntryEntity>> getEntries(){
        return journalRepository.getAllEntries();
    }

    public void getEntriesRemotely(String email){
        journalRepository.getEntriesRemotely(email, new JournalRepository.LoadEntriesCallback() {
            @Override
            public void onEntriesLoaded(List<JournalEntryEntity> list) {
                allEntriesContract.onEntriesLoaded(list);
            }

            @Override
            public void OnDataUnavailable(String error) {

            }
        });
    }

    public void deleteEntryInRemote(JournalEntryEntity entity)
    {
        journalRepository.deleteEntry(entity, new JournalRepository.LoadInfoCallback() {
            @Override
            public void onDataLoaded(int success) {
                allEntriesContract.onUpdateSuccess("post deleted");
            }

            @Override
            public void onDataNotAvailable(String error) {
                allEntriesContract.onUpdateSuccess(error);
            }
        });
    }

}
