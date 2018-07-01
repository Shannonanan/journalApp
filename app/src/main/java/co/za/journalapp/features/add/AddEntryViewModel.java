package co.za.journalapp.features.add;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import javax.inject.Inject;

import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class AddEntryViewModel extends ViewModel{

    private JournalRepositoryImpl journalRepository;
    private String writtenEntry;

    private LiveData<JournalEntryEntity> entry;

    public AddEntryViewModel(JournalEntryDatabase database, JournalRepositoryImpl journalRepository, int taskId) {
        this.journalRepository = journalRepository;
        entry = database.journalEntryDao().getEntryById(taskId);
    }


    public String getEntry() {
        return writtenEntry;
    }

    public void setEntry(String writtenEntry) {
        this.writtenEntry = writtenEntry;
    }

    public void addEntry(JournalEntryEntity entry, String email){
        journalRepository.insertEntry(entry, email);
    }
}

