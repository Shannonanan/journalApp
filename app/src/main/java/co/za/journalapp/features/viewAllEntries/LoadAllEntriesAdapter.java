package co.za.journalapp.features.viewAllEntries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.za.journalapp.R;
import co.za.journalapp.data.localRepository.JournalEntryEntity;

public class LoadAllEntriesAdapter extends RecyclerView.Adapter<LoadAllEntriesAdapter.EntryViewHolder> {

    private Context mContext;
    private List<JournalEntryEntity>entryCollection;

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    final private ItemClickListener mItemClickListener;

    public LoadAllEntriesAdapter(Context mContext, ItemClickListener mItemClickListener) {
        this.mContext = mContext;
        this.mItemClickListener = mItemClickListener;
        this.entryCollection = Collections.emptyList();
    }


    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell_entry, viewGroup, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder entryViewHolder, int position) {
            JournalEntryEntity entryEntity = this.entryCollection.get(position);

            entryViewHolder.mEntry.setText(entryEntity.getWrittenEntry());
    }



    @Override
    public int getItemCount() {
        return (this.entryCollection != null) ? this.entryCollection.size() : 0 ;
    }

    public void setInfoCollection(List<JournalEntryEntity> INFO) {
        this.validateCollection(INFO);
        this.entryCollection = (List<JournalEntryEntity>) INFO;
        this.notifyDataSetChanged();
    }

    private void validateCollection(List<JournalEntryEntity> infoCollection) {
        if (infoCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    public List<JournalEntryEntity> getEntryCollection() {
        return entryCollection;
    }

    static class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.date) TextView mDate;
        @BindView(R.id.time)TextView mTime;
        @BindView(R.id.entry) TextView mEntry;


        public EntryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
