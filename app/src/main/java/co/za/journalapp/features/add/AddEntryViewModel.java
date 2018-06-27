package co.za.journalapp.features.add;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

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

  //  JournalRepositoryImpl journalRepository;
    private String date;
    private String time;
    private String writtenEntry;

    private LiveData<JournalEntryEntity> entry;

//    public AddEntryViewModel(JournalRepositoryImpl journalRepository) {
//        this.journalRepository = journalRepository;
//        entry = database.taskDao().loadTaskById(taskId);
//    }

    public AddEntryViewModel(JournalEntryDatabase database, int taskId) {
      //  this.journalRepository = journalRepository;
        entry = database.journalEntryDao().getEntryById(taskId);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEntry() {
        return writtenEntry;
    }

    public void setEntry(String writtenEntry) {
        this.writtenEntry = writtenEntry;
    }


    public LiveData<JournalEntryEntity> getAndAddTask(){
        return entry;
    }

    public void addEntry(){
        JournalEntryEntity entry = new JournalEntryEntity( date, time, writtenEntry);
 //       journalEntryDao.a();
//        journalRepository.insertEntry(entry).observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Timber.d("onComplete - successfully added event");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.d(e, "onError - add:");
//                    }
//                });
    }
}

