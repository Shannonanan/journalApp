package co.za.journalapp.data.localRepository;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link JournalEntryDatabase}
 */

@Dao
public interface JournalEntryDao {

    @Query("Select * FROM journal")
    LiveData<List<JournalEntryEntity>> loadAllEntries();


    @Query("Select * From journal WHERE id = :id")
    LiveData<JournalEntryEntity> getEntryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEntry(JournalEntryEntity entry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntry(JournalEntryEntity entry);

    @Delete
    void deleteTask(JournalEntryEntity entry);

}
