package co.za.journalapp.data;


import android.arch.lifecycle.LiveData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


public class JournalRepositoryImpl implements JournalRepository {

    private static JournalRepositoryImpl INSTANCE = null;

    private final JournalRepository mLocalDataSource;
    private final JournalRepository mRemoteDataSource;
    private JournalEntryDatabase mDb;
    public Map<Object, JournalEntryEntity> mCachedInfo;

    public JournalRepositoryImpl(JournalRepository mLocalDataSource, JournalRepository mRemoteDataSource,
                                 JournalEntryDatabase mDb) {
        this.mLocalDataSource = mLocalDataSource;
        this.mRemoteDataSource = mRemoteDataSource;
        this.mDb = mDb;
    }

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;


    @Override
    public void insertEntry(final JournalEntryEntity entry, String email) {

        mLocalDataSource.insertEntry(entry, email);
        mRemoteDataSource.insertEntry(entry, email);
//        if (entry == null) {
//            return Completable.error(new IllegalArgumentException("Event cannot be null"));
//        }
        // Do in memory cache update to keep the app UI up to date
        if (mCachedInfo == null) {
            mCachedInfo = new LinkedHashMap<>();
        }

        mCachedInfo.put(entry.getId(), entry);
//        return Completable.fromAction(new Action() {
//            @Override
//            public void run() throws Exception {
//
//                mDb.journalEntryDao().insertEntry(entry);
//                mCachedInfo.put(entry.getId(), entry);
//            }
//        });

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

    @Override
    public Completable updateEntry(final JournalEntryEntity entry) {
        if (entry == null) {
            return Completable.error(new IllegalArgumentException("Entry cannot be null"));
        }

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                mDb.journalEntryDao().updateEntry(entry);
            }
        });
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
    public static JournalRepositoryImpl getInstance(JournalRepository localDataSource, JournalRepository remoteDataSource, JournalEntryDatabase mDb) {
        if (INSTANCE == null) {
            INSTANCE = new JournalRepositoryImpl(localDataSource,remoteDataSource, mDb);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
