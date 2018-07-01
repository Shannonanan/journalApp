package co.za.journalapp.data.remoteRepository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.za.journalapp.AppExecutors;
import co.za.journalapp.R;
import co.za.journalapp.data.JournalRepository;
import co.za.journalapp.data.localRepository.JournalEntryEntity;
import co.za.journalapp.data.localRepository.JournalEntrySchema;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

public class RemoteDataSource implements JournalRepository {

  //  private final Context context;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final AppExecutors mExecutors;
    Context mContext;
    String postToDelete = "";


    public RemoteDataSource(AppExecutors mExecutors, Context context) {
        this.mExecutors = mExecutors;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.mContext = context;
    }

    @Override
    public void insertEntry(final JournalEntryEntity entry, final String email, final LoadInfoCallback callback) {
        checkNotNull(entry);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                db.collection(mContext.getString(R.string.path_to_posts))
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
    public void deleteEntry(final JournalEntryEntity entryToDelete, final String email) {
        checkNotNull(entryToDelete);

        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {

                Query query = db.collection(mContext.getString(R.string.path_to_posts));
                        query.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<JournalEntrySchema> list = new ArrayList<>();
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    JournalEntrySchema journalEntrySchema = documentSnapshot.toObject(JournalEntrySchema.class);
                                    if(journalEntrySchema.getId() == entryToDelete.getId()){
                                       postToDelete = documentSnapshot.getId();
                                       break;
                                    } }

                                db.collection(mContext.getString(R.string.path_to_posts)).document(postToDelete)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        };
        mExecutors.networkIO().execute(saveRunnable);
    }

    private JournalEntryEntity convertJournalPost(DocumentSnapshot documentSnapshot) {
        JournalEntryEntity entry = documentSnapshot.toObject(JournalEntryEntity.class);
        return entry;
    }

}
