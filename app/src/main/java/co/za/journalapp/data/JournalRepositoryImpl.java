package co.za.journalapp.data;


import android.arch.lifecycle.LiveData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;
import io.reactivex.functions.Action;


public class JournalRepositoryImpl implements JournalRepository {

    private static JournalRepositoryImpl INSTANCE = null;

    // private final RemoteDataSource mRemoteDataSource;
    private final JournalRepository mLocalDataSource;
    private JournalEntryDatabase mDb;
    private LiveData<List<JournalEntryEntity>> entries;
    public Map<Object, JournalEntryEntity> mCachedInfo;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

//    public JournalRepositoryImpl(JournalRepository mLocalDataSource) {
//        this.mLocalDataSource = mLocalDataSource;
//    }

    public JournalRepositoryImpl(JournalRepository mLocalDataSource, JournalEntryDatabase mDb) {
        this.mLocalDataSource = mLocalDataSource;
        this.mDb = mDb;
    }


    @Override
    public Completable insertEntry(final JournalEntryEntity entry) {

        if (entry == null) {
            return Completable.error(new IllegalArgumentException("Event cannot be null"));
        }
        // Do in memory cache update to keep the app UI up to date
        if (mCachedInfo == null) {
            mCachedInfo = new LinkedHashMap<>();
        }
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
              //    mLocalDataSource.insertEntry(entry);
                mDb.journalEntryDao().insertEntry(entry);
                mCachedInfo.put(entry.getId(), entry);
            }
        });

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
        //CHECK HERE FOR INFO IN LOCAL IF SOMETHING ASK TO BACK UP TO CLOUD OR IF NOTHING PULL FROM CLOUD?
        // Query the local storage if available. If not, query the network.
        // Do in memory cache update to keep the app UI up to date

        return mLocalDataSource.getAllEntries();

    }

    @Override
    public LiveData<JournalEntryEntity> getEntry(final int id) {
        return mDb.journalEntryDao().getEntryById(id);
    }


//    @Override
//    public Completable deleteEntry(final JournalEntryEntity entry) {
//       return Completable.fromAction(new Action() {
//           @Override
//           public void run() throws Exception {
//               mLocalDataSource.deleteEntry(entry);
//               mCachedInfo.clear();
//           }
//       });


//}

    /**
     * Returns the single instance of this class, creating it if necessary.
     * used for testing
     *
     * @param localDataSource the device storage data source
     * @return the {@link JournalRepositoryImpl} instance
     */
    public static JournalRepositoryImpl getInstance(JournalRepository localDataSource, JournalEntryDatabase mDb) {
        if (INSTANCE == null) {
            INSTANCE = new JournalRepositoryImpl(localDataSource, mDb);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
