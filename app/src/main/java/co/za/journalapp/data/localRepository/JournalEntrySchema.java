package co.za.journalapp.data.localRepository;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class JournalEntrySchema {

    private int id;
    private String date;
    private String time;
    private String writtenEntry;
    private String email;


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("date", date);
        result.put("time", time);
        result.put("writtenEntry", writtenEntry);
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
