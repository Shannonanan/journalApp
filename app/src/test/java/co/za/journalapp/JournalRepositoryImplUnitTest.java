package co.za.journalapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.JournalRepositoryImpl;
import co.za.journalapp.data.localRepository.JournalEntryDao;
import co.za.journalapp.data.localRepository.JournalEntryDatabase;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.LocalDataSource;
import co.za.journalapp.data.remoteRepository.RemoteDataSource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class JournalRepositoryImplUnitTest {

    private static JournalEntryEntity INFO = new JournalEntryEntity("2018-06-26",
                    "22:00",
                    "test entry 1",  "shannon@gmail.com" );

    private JournalRepositoryImpl journalRepositoryImpl;

    @Mock
    private JournalEntryDao entryDao;

    @Mock
    private JournalEntryDatabase mDb;

    @Mock
    private LocalDataSource mLocalDataSource;

    @Mock
    private RemoteDataSource mRemoteDataSource;

    @Mock
    private JournalRepository.LoadInfoCallback mLoadInfoCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<JournalRepository.LoadInfoCallback> mLoadInfoCallbackCaptor;

    @Before
    public void setupInfoRepo(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        journalRepositoryImpl = JournalRepositoryImpl.getInstance(mLocalDataSource, mRemoteDataSource, mDb);

    }

    @After
    public void destroyRepositoryInstance() {
        JournalRepositoryImpl.destroyInstance();
    }
    @Test
    public void insertEntry_() {
        String fakeEmail = "shannon@gmail.com";
        // Given a stub info with arguments
        JournalEntryEntity testEntry = new JournalEntryEntity("date", "time","test entry 1", fakeEmail);

        // When entries are saved to the EpicRespository repository
        journalRepositoryImpl.insertEntry(testEntry, fakeEmail);

        // Then the service API and persistent repository are called and the cache is updated

        verify(mLocalDataSource).insertEntry(testEntry, fakeEmail);
        assertThat(journalRepositoryImpl.mCachedInfo.size(), is(1));
    }

//    @Test
//    public void deleteEntry_(){
//        JournalEntryEntity testEntry2 = new JournalEntryEntity("date", "time","test entry 1");
//        journalRepositoryImpl.insertEntry(testEntry2);
//        verify(mLocalDataSource).insertEntry(testEntry2);
//
//        journalRepositoryImpl.deleteEntry(testEntry2);
//
//        assertThat(journalRepositoryImpl.mCachedInfo.size(), is(0));
//    }

    @Test
    public void getAllEntries_(){
        String fakeEmail = "shannon@gmail.com";
        JournalEntryEntity testEntry1 = new JournalEntryEntity("date", "time","test entry 1", fakeEmail);
        journalRepositoryImpl.insertEntry(testEntry1, fakeEmail);
        JournalEntryEntity testEntry2 = new JournalEntryEntity("date", "time","test entry 1", fakeEmail);
        journalRepositoryImpl.insertEntry(testEntry2, fakeEmail);
        JournalEntryEntity testEntry3 = new JournalEntryEntity("date", "time","test entry 1", fakeEmail);
        journalRepositoryImpl.insertEntry(testEntry3, fakeEmail);

        journalRepositoryImpl.getAllEntries();

        verify(mLocalDataSource).getAllEntries();
       
    }
}