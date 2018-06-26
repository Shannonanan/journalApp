package co.za.journalapp.data;

import android.arch.lifecycle.LiveData;
import android.content.res.Resources;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.za.journalapp.R;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import co.za.journalapp.data.remoteRepository.RemoteDataSource;

public class JournalRepositoryImpl implements JournalRepository {

    private static JournalRepositoryImpl INSTANCE = null;

    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private  LiveData<List<JournalEntryEntity>> entries;


    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    public JournalRepositoryImpl(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }


    @Override
    public void insertEntry(JournalEntryEntity entry, final LoadInfoCallback callback) {
        if (entry == null){
             callback.onDataNotAvailable(Resources.getSystem().getString(R.string.null_event));
        }
        mRemoteDataSource.insertEntry(entry, new LoadInfoCallback() {
            @Override
            public void onDataLoaded(String success) {
                callback.onDataLoaded(success);
            }

            @Override
            public void onDataNotAvailable(String error) {

            }
        });
    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
            // Query the local storage if available. If not, query the network.
        return entries = mLocalDataSource.getAllEntries();
        }


    @Override
    public void deleteEntry(JournalEntryEntity entry, final DeleteInfoCallback callback ) {
        mRemoteDataSource.deleteEntry(entry);
        callback.onDataDeleted("success");
    }
}
