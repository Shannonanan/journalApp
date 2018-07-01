package co.za.journalapp.data.localRepository;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static co.za.journalapp.data.localRepository.JournalEntryEntity.TABLE_NAME;



@Entity(tableName = TABLE_NAME)
public class JournalEntryEntity {

    public static final String TABLE_NAME = "journal";


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String time;
    private String writtenEntry;
    private String email;

    @Ignore
    public JournalEntryEntity(String date, String time,
                                String writtenEntry,String email) {
        this.date = date;
        this.time = time;
        this.writtenEntry = writtenEntry;
        this.email = email;
    }

    public JournalEntryEntity(int id, String date,
                                String time,
                                String writtenEntry, String email) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.writtenEntry = writtenEntry;
        this.email = email;
    }

    @Ignore
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("date", date);
        result.put("time", time);
        result.put("entry", writtenEntry);
        result.put("email", email);
        return result;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
