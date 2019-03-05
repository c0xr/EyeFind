package com.csti.eyefind.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MyGetPropertyFragment extends Fragment {

    private RecyclerView mHorizontalListView;//水平商品轮播图
    private RecyclerView mVerticalListView;//竖直商品轮播图
    private Button addMoreButton;//点击增加更多
    private List<PushLostItem> mainVerticalList;//竖直商品，点击增加10项
    private View primeView;//本界面布局


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Bomb
    private BmobQuery<PushLostItem> query;
    private int length = 0;//原先列表长度 用于判断是否刷新
    private int mAddTimes = 0;//记录连续点击次数
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if ( msg.what == 0x0 ){
                setmVerticalListView();//配置竖直listView
            }
        }
    };

    public MyGetPropertyFragment() {
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
        primeView = inflater.inflate(R.layout.fragment_my_get_property, container, false);
        addMore();
        initVerticalData();
        return primeView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //与水平相同设置竖直
    public void setmVerticalListView() {
        List<PushLostItem> data = mainVerticalList;
        mVerticalListView = (RecyclerView) primeView.findViewById(R.id.main_prime_page_vertical_list);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        mVerticalListView.setLayoutManager(manager);
        mVerticalListView.setHasFixedSize(true);//设置recycler不可滑动
        mVerticalListView.setNestedScrollingEnabled(false);
        mVerticalListView.getLayoutParams().height = 250 * data.size();
        mVerticalListView.setAdapter(new VerticalListAdapter(data));
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

        private List<PushLostItem> mDatas;
        public VerticalListAdapter(List<PushLostItem> data) {
            this.mDatas = data;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.name.setText("我的失物 " + position + 1 + ":");
            holder.introduce.setText(mDatas.get(position).getmName());
            holder.place.setText(mDatas.get(position).getmLabel());
            holder.price.setText("");
            holder.image.setImageResource(R.drawable.img7);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 点击事件
//                    Toast.makeText(getActivity(),"click",Toast.LENGTH_SHORT).show();
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

    private void initVerticalData(){
        Person user = BmobUser.getCurrentUser(Person.class);//先从云端读入数据
        query = new BmobQuery<>();
        query.addWhereEqualTo("mPerson", user);
        mainVerticalList = new ArrayList<>();
        query.findObjects(new FindListener<PushLostItem>() {
            @Override
            public void done(List<PushLostItem> object, BmobException e) {
                if(e==null){
                    for (int i = 0 ; i < object.size() ; i++){
                        mainVerticalList.add(object.get(i));
                    }
                }else{
                    Toast.makeText(getActivity(), "失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        new Thread(){
            @Override
            public void run() {
                while (mainVerticalList.size() == length){
                    try {
                        sleep(200);
                    }catch (Exception e){
                        Toast.makeText(getActivity(),"信息获取失败 T.T ",Toast.LENGTH_SHORT).show();
                    }
                }
                try {
                    sleep(300);
                    Message msg = new Message();
                    msg.what = 0x0;
                    handler.sendMessage(msg);
                    length = mainVerticalList.size();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void addMore(){
        addMoreButton = (Button) primeView.findViewById(R.id.main_prime_page_add_more);
        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVerticalData();
                initVerticalData();
            }
        });
    }

}