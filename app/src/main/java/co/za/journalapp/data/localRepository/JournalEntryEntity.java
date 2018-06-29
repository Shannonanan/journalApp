package co.za.journalapp.data.localRepository;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static co.za.journalapp.data.localRepository.JournalEntryEntity.TABLE_NAME;
//indices = {@Index(value = {"id"}, unique = true


@Entity(tableName = TABLE_NAME)
public class JournalEntryEntity {

    public static final String TABLE_NAME = "journal";


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String time;
    private String writtenEntry;

    @Ignore
    public JournalEntryEntity(String date, String time,
                                String writtenEntry) {
        this.date = date;
        this.time = time;
        this.writtenEntry = writtenEntry;
    }

    public JournalEntryEntity(int id, String date,
                                String time,
                                String writtenEntry) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.writtenEntry = writtenEntry;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWrittenEntry() {
        return writtenEntry;
    }

    public void setWrittenEntry(String writtenEntry) {
        this.writtenEntry = writtenEntry;
    }
}
