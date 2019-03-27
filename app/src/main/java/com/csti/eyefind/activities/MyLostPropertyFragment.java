package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MyLostPropertyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mVerticalListView;//竖直商品轮播图
    private Button addMoreButton;//点击增加更多
    private List<LostItem> mainVerticalList;//竖直商品，点击增加10项
    private View primeView;//本界面布局

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Bomb
    private BmobQuery<LostItem> query;
    private int length = 0;//原先列表长度 用于判断是否刷新
    private int mAddTimes = 0;//记录连续点击次数
    private SwipeRefreshLayout swipeRefreshLayout;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if ( msg.what == 0x0 ){
                setmVerticalListView();//配置竖直listView
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };

    public MyLostPropertyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        primeView = inflater.inflate(R.layout.fragment_my_lost_property, container, false);

        mVerticalListView = (RecyclerView) primeView.findViewById(R.id.main_prime_page_vertical_list);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        mVerticalListView.setLayoutManager(manager);
        mVerticalListView.setHasFixedSize(true);//设置recycler不可滑动
        mVerticalListView.setNestedScrollingEnabled(true);

        reFlesh();
        initVerticalData();
        return primeView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        initVerticalData();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //与水平相同设置竖直
    public void setmVerticalListView() {
        List<LostItem> data = mainVerticalList;
        mVerticalListView.setAdapter(new VerticalListAdapter(data));
        firstLoadAnimation();
    }

    class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.VH>{
        //② 创建ViewHolder
        class VH extends RecyclerView.ViewHolder{
            private final TextView name,introduce,place,price;
            private final ImageView image;
            public VH(View v) {
                super(v);
                v.getLayoutParams().height = 250 ;
                image = (ImageView) v.findViewById(R.id.main_prime_vertical_list_view_image);
                name = (TextView) v.findViewById(R.id.main_prime_vertical_list_view_name);
                introduce = (TextView) v.findViewById(R.id.main_prime_vertical_list_view_introduce);
                place = (TextView) v.findViewById(R.id.main_prime_vertical_list_view_place);
                price = (TextView) v.findViewById(R.id.main_prime_vertical_list_view_price);
            }
        }

        private List<LostItem> mDatas;
        public VerticalListAdapter(List<LostItem> data) {
            this.mDatas = data;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.name.setText(mDatas.get(position).getName());
            holder.introduce.setText(mDatas.get(position).getLabel());
            holder.place.setText(mDatas.get(position).getPickedPlace());
            holder.price.setText(mDatas.get(position).getPickedDate());
            holder.image.setImageBitmap(mDatas.get(position).getThumbnail());
            final int mListPosition = position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LostItem lostItem = mDatas.get(mListPosition);
                    Intent intent = new Intent(getActivity(),MyLostDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("mName", lostItem.getName());
                    bundle.putSerializable("mPlace", lostItem.getPickedPlace());
                    bundle.putSerializable("mFounder", lostItem.getFounder());
                    bundle.putSerializable("mDate", lostItem.getPickedDate());
                    bundle.putSerializable("mTel", lostItem.getTel());
                    bundle.putSerializable("mQQ", lostItem.getQQ());
                    bundle.putSerializable("mWeChat", lostItem.getWeChat());
                    bundle.putSerializable("bitmapA", lostItem.getImageA().getUrl());
                    bundle.putSerializable("bitmapB", lostItem.getImageB().getUrl());
                    bundle.putSerializable("objectId", lostItem.getObjectId());

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            //LayoutInflater.from指定写法
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_lost_property_list_view, parent, false);
            return new VH(v);
        }
    }

    private void setBitmap(final LostItem lostItem, final int size){
        new Thread(){
            @Override
            public void run() {
                lostItem.setThumbnail(getPicture(lostItem.getImageThumbnail().getUrl()));
                mainVerticalList.add(lostItem);
                length = mainVerticalList.size();
                if (length == size){
                    try {
                        sleep(300);
                        Message msg = new Message();
                        msg.what = 0x0;
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void initVerticalData(){

        Person user = BmobUser.getCurrentUser(Person.class);//先从云端读入数据
        query = new BmobQuery<>();
        query.addWhereEqualTo("mPerson", user);
        query.findObjects(new FindListener<LostItem>() {
            @Override
            public void done(final List<LostItem> object, BmobException e) {
                if(e==null){
                    mainVerticalList = new ArrayList<>();
                    new Thread(){
                        @Override
                        public void run() {
//                            try {
                                for (int i = 0 ; i < object.size() ; i++){
                                    setBitmap(object.get(i),object.size());
                                }
//                            } catch (InterruptedException e1) {
//                                e1.printStackTrace();
//                            }
                        }
                    }.start();
                }else{
                    Toast.makeText(getActivity(), "请检查网络设置",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reFlesh(){
        swipeRefreshLayout = primeView.findViewById(R.id.my_lost_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public Bitmap getPicture(String path){
        Bitmap bm = null;
        try{
            URL url = new URL(path);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }

    private void firstLoadAnimation(){
        //首次加载这种比较有效
        mVerticalListView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mVerticalListView.getViewTreeObserver().removeOnPreDrawListener(this);
                        final int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                        for (int i = 0; i < mVerticalListView.getChildCount(); i++) {
                            View v = mVerticalListView.getChildAt(i);
                            v.setTranslationX(screenWidth);
                            v.animate().translationX(0)
                                    .setDuration(600)
                                    .setStartDelay(i * 100)
                                    .start();
                        }

                        return true;
                    }
                });
    }

}

