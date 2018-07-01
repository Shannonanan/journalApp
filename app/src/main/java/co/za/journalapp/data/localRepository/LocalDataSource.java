package co.za.journalapp.data.localRepository;

import android.arch.lifecycle.LiveData;
import java.util.List;
import co.za.journalapp.AppExecutors;
import co.za.journalapp.data.JournalRepository;
import io.reactivex.Completable;


import static com.google.common.base.Preconditions.checkNotNull;

public class LocalDataSource implements JournalRepository {

    private final JournalEntryDao journalEntryDao;
    private final AppExecutors mExecutors;

    public LocalDataSource(JournalEntryDao journalEntryDao, AppExecutors mExecutors) {
        this.journalEntryDao = journalEntryDao;
        this.mExecutors = mExecutors;
    }


    @Override
    public void insertEntry(final JournalEntryEntity entry, String email) {
//        if(event == null) {
//            return Completable.error(new IllegalArgumentException("Event cannot be null"));
//        }
//       return Completable.fromAction(new Action() {
//           @Override
//           public void run() throws Exception {
//
//               String test = "hello";
//           }
//       });
        checkNotNull(entry);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                journalEntryDao.insertEntry(entry);
            }
        };
        mExecutors.diskIO().execute(saveRunnable);

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
            return journalEntryDao.loadAllEntries();
    }

    @Override
    public LiveData<JournalEntryEntity> getEntry(int id) {
        return null;
    }

    @Override
    public Completable updateEntry(JournalEntryEntity entity) {
        return null;
    }


//    @Override
//    public Completable deleteEntry(final JournalEntryEntity entry) {
//        return Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Exception {
//                journalEntryDao.deleteTask(entry);
//            }
//        });
//    }
}
