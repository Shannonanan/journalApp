package co.za.journalapp.data.remoteRepository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import co.za.journalapp.AppExecutors;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoteDataSource implements JournalRepository {

  //  private final Context context;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final AppExecutors mExecutors;

    public RemoteDataSource(AppExecutors mExecutors) {
        this.mExecutors = mExecutors;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void insertEntry(final JournalEntryEntity entry, final String email, final LoadInfoCallback callback) {
        checkNotNull(entry);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                db.collection("users/" + email + "/entries" )
                        .add(entry.toMap())
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                callback.onDataLoaded(1);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onDataNotAvailable("error");
                            }
                        });
            }
        };
        mExecutors.networkIO().execute(saveRunnable);
    }

    @Override
    public LiveData<List<JournalEntryEntity>> getAllEntries() {
        return null;
    }

    @Override
    public LiveData<JournalEntryEntity> getEntry(int id) {
        return null;
    }

    @Override
    public Completable updateEntry(JournalEntryEntity entity) {
        return null;
    }

    @Override
    public void deleteEntry(JournalEntryEntity entry, final String email) {

    }

}
