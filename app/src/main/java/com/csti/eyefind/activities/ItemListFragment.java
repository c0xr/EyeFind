package com.csti.eyefind.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csti.eyefind.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ItemListFragment extends Fragment {
    private final static String ARG_ADAPTER_TYPE="adapter type";

    private RecyclerView mRecyclerView;
    private ItemListAdapter mAdapter;
    private List<LostItem> mLostItems;
    private NetworkHandler mNetworkHandler=new NetworkHandler();
    private String mAdapterType;
    private ImageLoader mImageLoader;
    private final static int VIEW_TYPE_ITEM=1;
    private final static int VIEW_TYPE_TIP=2;

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mNameTextView;
        private TextView mDateTextView;
        private TextView mPlaceTextView;
        private CardView mCardView;

        public ItemHolder(LayoutInflater inflater,ViewGroup parent,int viewType) {
            super(inflater.inflate(isViewTypeTip(viewType)?R.layout.list_item_bottom_tip:R.layout.list_item,
                    parent,false));
            if(isViewTypeTip(viewType)) {
                return;
            }
            mImageView = itemView.findViewById(R.id.bitmap_a);
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mDateTextView = itemView.findViewById(R.id.date_text_view);
            mPlaceTextView = itemView.findViewById(R.id.place_text_view);
            mCardView=itemView.findViewById(R.id.list_item_card_external);
            itemView.setOnClickListener(this);
        }

        public void bind(LostItem lostItem,int position){
            if(isPositionTip(position)) {
                return;
            }
            if(lostItem.getThumbnail()!=null) {
                mImageView.setImageBitmap(lostItem.getThumbnail());
            }
            mNameTextView.setText(lostItem.getName());

            mDateTextView.setText(getStringFromTime(lostItem.getTimeFromUpdate()));

            mPlaceTextView.setText(lostItem.getPickedPlace());
            if(position==mImageLoader.getSize()-1){
                ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams) mCardView.getLayoutParams();
                int px=dp2px(12,getActivity());
                params.setMargins(0,px,0,px);
                mCardView.setLayoutParams(params);
            }
            itemView.setTag(position);
        }

        @Override
        public void onClick(View v) {
            int position=(int)v.getTag();
            startActivity(DetailActivity.newIntent(getActivity(),mLostItems.get(position)));
        }
    }

    private class ItemListAdapter extends RecyclerView.Adapter<ItemHolder>{

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new ItemHolder(inflater,parent,viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
            itemHolder.bind(mLostItems.get(position),position);
        }

        @Override
        public int getItemCount() {
            return mLostItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            if(isPositionTip(position)){
                return VIEW_TYPE_TIP;
            }
            return VIEW_TYPE_ITEM;
        }
    }

    public static ItemListFragment newInstance(String adapterType) {

        Bundle args = new Bundle();
        args.putString(ARG_ADAPTER_TYPE,adapterType);

        ItemListFragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapterType=getArguments().getString(ARG_ADAPTER_TYPE);
        mNetworkHandler=new NetworkHandler();
        mLostItems=new ArrayList<>();
        mImageLoader=new ImageLoader(mAdapterType,mNetworkHandler,mLostItems);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_item_list,container,false);
        mRecyclerView=v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI(){
        if(mAdapter==null){
            mAdapter=new ItemListAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mImageLoader.load();
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NetworkHandler extends Handler{
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            for(int i=0;i<mLostItems.size();i++){
                LostItem lostItem=mLostItems.get(i);
                lostItem.setTimeFromUpdate(getTimeFromUpdate(lostItem.getUpdatedAt()));
            }
            Collections.sort(mLostItems,new Comparator<LostItem>() {
                @Override
                public int compare(LostItem o1, LostItem o2) {
                    if(o1.getTimeFromUpdate()>o2.getTimeFromUpdate()){
                        return 1;
                    }else if(o1.getTimeFromUpdate()<o2.getTimeFromUpdate()){
                        return -1;
                    }
                    return 0;
                }
            });
            mLostItems.add(new LostItem());
            mAdapter.notifyItemRangeInserted(0,mLostItems.size());
        }
    }

    private int dp2px(int dip, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip * scale + 0.5f);
    }

    private boolean isPositionTip(int position){
        return position==mImageLoader.getSize();
    }

    private boolean isViewTypeTip(int viewType){
        return viewType==VIEW_TYPE_TIP;
    }

    private long getTimeFromUpdate(String s){
        Log.d("mytag",s+" s");
        SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=f.parse(s,new ParsePosition(0));
        Log.d("mytag",date+" date");
        long updateTime=date.getTime();
        Date date2=new Date();
        long presentTime=date2.getTime();
        return (presentTime-updateTime)/1000;
    }

    private String getStringFromTime(long time){
        String s;
        if(time<60){
            s=time+"秒前";
        }else if(time<3600){
            s=time/60+"分钟前";
        }else if(time<86400){
            s=time/3600+"小时前";
        }else{
            s=time/86400+"天前";
        }
        Log.d("mytag",time+" time");
        return s;
    }
}
