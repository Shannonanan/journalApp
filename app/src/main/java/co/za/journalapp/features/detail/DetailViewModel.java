package co.za.journalapp.features.detail;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import co.za.journalapp.R;
import co.za.journalapp.SnackbarMessage;
import co.za.journalapp.data.JournalRepository;
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
    private DetailContract detailContract;

   // private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    public DetailViewModel(JournalRepositoryImpl journalRepository) {
        this.journalRepository = journalRepository;
    }

    public void setView(@NonNull DetailContract view) {
        this.detailContract = view;
    }

//    SnackbarMessage getSnackbarMessage() {
//        return mSnackbarText;
//    }
//    private void showSnackbarMessage(Integer message) {
//        mSnackbarText.setValue(message);
//    }


    LiveData<JournalEntryEntity>getEntry(int id){
        return journalRepository.getEntry(id);
    }

    public void updateEntry(JournalEntryEntity journalEntryEntity){
         journalRepository.updateEntry(journalEntryEntity, new JournalRepository.LoadInfoCallback() {
             @Override
             public void onDataLoaded(int success) {
                 //speak back to view

                // showSnackbarMessage((R.string.app_name));
                 detailContract.onUpdateSuccess("success");
             }

             @Override
             public void onDataNotAvailable(String error) {

             }
         });
    }
}
