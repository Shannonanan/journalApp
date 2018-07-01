package co.za.journalapp.data;


import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


public class JournalRepositoryImpl implements JournalRepository {

    private static JournalRepositoryImpl INSTANCE = null;
    private static String TAG = "uploads";

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
    public void insertEntry(final JournalEntryEntity entry, final String email, LoadInfoCallback callback) {
        mLocalDataSource.insertEntry(entry, email, new LoadInfoCallback() {
            @Override
            public void onDataLoaded(int uniqueId) {
                entry.setId(uniqueId);
                mRemoteDataSource.insertEntry(entry, email, new LoadInfoCallback() {
                    @Override
                    public void onDataLoaded(int success) {
                        Log.d(TAG, "successful upload");
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        Log.d(TAG, "failed upload");
                    }
                });
            }
            @Override
            public void onDataNotAvailable(String error) {

            }
        });

        if (mCachedInfo == null) {
            mCachedInfo = new LinkedHashMap<>();
        }

        mCachedInfo.put(entry.getId(), entry);
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
    public void updateEntry(final JournalEntryEntity entry, final LoadInfoCallback callback) {
        mLocalDataSource.updateEntry(entry, new LoadInfoCallback() {
            @Override
            public void onDataLoaded(int uniqueId) {
                mRemoteDataSource.updateEntry(entry, new LoadInfoCallback() {
                    @Override
                    public void onDataLoaded(int success) {
                        callback.onDataLoaded(success);
                        Log.d(TAG, "successful upload");
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        Log.d(TAG, "failed upload");
                    }
                });
            }
            @Override
            public void onDataNotAvailable(String error) {
            }
        });
    }

    @Override
    public void deleteEntry(final JournalEntryEntity entry, final LoadInfoCallback callback) {
        mLocalDataSource.deleteEntry(entry, new LoadInfoCallback() {
            @Override
            public void onDataLoaded(int success) {
                mRemoteDataSource.deleteEntry(entry, new LoadInfoCallback() {
                    @Override
                    public void onDataLoaded(int success) {
                        callback.onDataLoaded(1);
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        callback.onDataNotAvailable(error);
                    }
                });
            }

            @Override
            public void onDataNotAvailable(String error) {

            }
        });

    }

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
