package co.za.journalapp.data.localRepository;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {JournalEntryEntity.class}, version =1)
@TypeConverters(DateConverter.class)
public abstract class JournalEntryDatabase extends RoomDatabase
{
    private static final String LOG_TAG = JournalEntryDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "journal";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static JournalEntryDatabase sInstance;

    public static JournalEntryDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        JournalEntryDatabase.class, JournalEntryDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract JournalEntryDao journalEntryDao();


}
