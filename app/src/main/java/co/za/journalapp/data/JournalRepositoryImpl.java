package co.za.journalapp.data;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import co.za.journalapp.data.localRepository.JournalEntryEntity;


public class JournalRepositoryImpl implements JournalRepository {

    private static JournalRepositoryImpl INSTANCE = null;

   // private final RemoteDataSource mRemoteDataSource;
    private final JournalRepository mLocalDataSource;
    private  LiveData<List<JournalEntryEntity>> entries;
    public Map<Object, JournalEntryEntity> mCachedInfo;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    public JournalRepositoryImpl(JournalRepository mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }


    @Override
    public void insertEntry(JournalEntryEntity entry) {
        if (entry == null){
            // callback.onDataNotAvailable(Resources.getSystem().getString(R.string.null_event));
        }
        // Do in memory cache update to keep the app UI up to date
        if (mCachedInfo == null) {
            mCachedInfo = new LinkedHashMap<>();
        }
        mLocalDataSource.insertEntry(entry);
        mCachedInfo.put(entry.getId(), entry);

    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
            // Query the local storage if available. If not, query the network.
        // Do in memory cache update to keep the app UI up to date

        return entries = mLocalDataSource.getAllEntries();

        }


    @Override
    public void deleteEntry(JournalEntryEntity entry) {
        mLocalDataSource.deleteEntry(entry);
        mCachedInfo.clear();

    }
    /**
     * Returns the single instance of this class, creating it if necessary.
     *used for testing
     * @param localDataSource  the device storage data source
     * @return the {@link JournalRepositoryImpl} instance
     */
    public static JournalRepositoryImpl getInstance(JournalRepository localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new JournalRepositoryImpl(localDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
