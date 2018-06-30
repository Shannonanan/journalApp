package co.za.journalapp.features.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DetailViewModel extends ViewModel {


    private JournalRepositoryImpl journalRepository;

    public DetailViewModel(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }


    LiveData<JournalEntryEntity>getEntry(int id){
        return journalRepository.getEntry(id);
    }

    public void updateEntry(JournalEntryEntity journalEntryEntity){
         journalRepository.updateEntry(journalEntryEntity).observeOn(AndroidSchedulers.mainThread())
                 .subscribeOn(Schedulers.io())
                 .subscribe(new CompletableObserver() {
                     @Override
                     public void onSubscribe(Disposable d) {

                     }

                     @Override
                     public void onComplete() {

                     }

                     @Override
                     public void onError(Throwable e) {

                     }
                 });
    }
}
