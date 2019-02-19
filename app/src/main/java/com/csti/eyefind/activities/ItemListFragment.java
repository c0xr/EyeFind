package com.csti.eyefind.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.List;

public class ItemListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;
    private List<LostItem> mLostItems;
    private ImageView mImageView;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mPlaceTextView;

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ItemHolder(LayoutInflater inflater,ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item,parent,false));
            mImageView=itemView.findViewById(R.id.bitmap_a);
            mNameTextView=itemView.findViewById(R.id.name_text_view);
            mDateTextView=itemView.findViewById(R.id.date_text_view);
            mPlaceTextView=itemView.findViewById(R.id.place_text_view);
        }

        public void bind(LostItem lostItem){
            mImageView.setImageBitmap(lostItem.getBitmapA());
            mNameTextView.setText(lostItem.getName());
            mDateTextView.setText(lostItem.getPickedDate());
            mPlaceTextView.setText(lostItem.getPickedPlace());
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class ItemListAdapter extends RecyclerView.Adapter<ItemHolder>{

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new ItemHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
            itemHolder.bind(mLostItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mLostItems.size();
        }
    }

    public static ItemListFragment newInstance() {

        Bundle args = new Bundle();

        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_item_list,container,false);
        mRecyclerView=v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLostItems=LostItemLab.getInstance(getActivity()).getLostItems();
        updateUI();
        return v;
    }

    private void updateUI(){
        if(mAdapter==null){
            mAdapter=new ItemListAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }else{

        }
    }
}
