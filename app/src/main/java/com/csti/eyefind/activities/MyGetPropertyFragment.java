package com.csti.eyefind.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class MyGetPropertyFragment extends Fragment {

    private RecyclerView mHorizontalListView;//水平商品轮播图
    private RecyclerView mVerticalListView;//竖直商品轮播图
    private Button addMoreButton;//点击增加更多
    private List<MyLostProperty> mainVerticalList;//竖直商品，点击增加10项
    private View primeView;//本界面布局


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        setmVerticalListView();//配置竖直listView
        addMore();
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
        List<MyLostProperty> data = initVerticalData();
        mVerticalListView = (RecyclerView) primeView.findViewById(R.id.main_prime_page_vertical_list);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        mVerticalListView.setLayoutManager(manager);
        mVerticalListView.setHasFixedSize(true);//设置recycler不可滑动
        mVerticalListView.setNestedScrollingEnabled(false);
        mVerticalListView.getLayoutParams().height = getActivity().getWindowManager().getDefaultDisplay().getWidth() * 3;
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

        private List<MyLostProperty> mDatas;
        public VerticalListAdapter(List<MyLostProperty> data) {
            this.mDatas = data;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.name.setText(mDatas.get(position).getName());
            holder.introduce.setText(mDatas.get(position).getIntroduce());
            holder.place.setText(mDatas.get(position).getPlace());
            holder.price.setText(mDatas.get(position).getPrice());
            holder.image.setImageResource(mDatas.get(position).getImageId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 点击事件
                    Toast.makeText(getActivity(),"click",Toast.LENGTH_SHORT).show();
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

    private List<MyLostProperty> initVerticalData(){
        mainVerticalList = new ArrayList<>();
        for (int i = 0 ; i < 13 ; i++){
            mainVerticalList.add(new MyLostProperty("name","introduce","NewYork","666", R.drawable.img5));
        }
        return mainVerticalList;
    }

    private void addMore(){
        addMoreButton = (Button) primeView.findViewById(R.id.main_prime_page_add_more);
        addMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"你已经进入聪明人都看得见的失物具体界面",Toast.LENGTH_SHORT).show();
                mVerticalListView.getLayoutParams().height += getActivity().getWindowManager().getDefaultDisplay().getWidth() * 5;
                for (int i = 0 ; i < 13 ; i++){
                    mainVerticalList.add(new MyLostProperty("失物名称","一些描述/介绍","失物标签","丢失日期", R.drawable.img6));
                }
                mVerticalListView.setAdapter(new VerticalListAdapter(mainVerticalList));
            }
        });
    }

}