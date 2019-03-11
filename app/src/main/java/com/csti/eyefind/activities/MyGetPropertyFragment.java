package com.csti.eyefind.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.OvershootInterpolator;
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
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;


public class MyGetPropertyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mVerticalListView;//竖直商品轮播图
    private Button addMoreButton;//点击增加更多
    private List<PushLostItem> mainVerticalList;//竖直商品，点击增加10项
    private View primeView;//本界面布局

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Bomb
    private BmobQuery<PushLostItem> query;
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

        mVerticalListView = (RecyclerView) primeView.findViewById(R.id.main_prime_page_vertical_list);
        firstLoadAnimation();
//        SlideInLeftAnimator animator = new SlideInLeftAnimator();
//        animator.setInterpolator(new OvershootInterpolator());
//// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
//        mVerticalListView.setItemAnimator(animator);
//
//        mVerticalListView.getItemAnimator().setAddDuration(1000);
//        mVerticalListView.getItemAnimator().setRemoveDuration(1000);
//        mVerticalListView.getItemAnimator().setMoveDuration(1000);
//        mVerticalListView.getItemAnimator().setChangeDuration(1000);

        reFlesh();
        initVerticalData();
        return primeView;
    }

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

        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        animator.setMoveDuration(1000);
        animator.setChangeDuration(1000);
        List<PushLostItem> data = mainVerticalList;
        mVerticalListView.getLayoutParams().height = 250 * data.size();
        mVerticalListView.setAdapter(new VerticalListAdapter(data));
        firstLoadAnimation();
//        mVerticalListView.setItemAnimator(new SlideInLeftAnimator());
// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
//        mVerticalListView.setItemAnimator(animator);
//
    }

    class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.VH>{
        //② 创建ViewHolder
        class VH extends RecyclerView.ViewHolder implements AnimateViewHolder {
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

            @Override
            public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

            }

            @Override
            public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
                ViewCompat.animate(itemView)
                        .translationY(-itemView.getHeight() * 0.3f)
                        .alpha(0)
                        .setDuration(300)
                        .setListener(listener)
                        .start();
            }

            @Override
            public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
                ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
                ViewCompat.setAlpha(itemView, 0);
            }

            @Override
            public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
                ViewCompat.animate(itemView)
                        .translationY(0)
                        .alpha(1)
                        .setDuration(300)
                        .setListener(listener)
                        .start();
            }

        }

        public void remove(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }

        public void add(PushLostItem text, int position) {
            mDatas.add(position, text);
            notifyItemInserted(position);
        }

        private List<PushLostItem> mDatas;

        public VerticalListAdapter(List<PushLostItem> data) {
            this.mDatas = data;
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.name.setText("我的失物 " + (position + 1) + " :");
            holder.introduce.setText("名称：" + mDatas.get(position).getmName());
            holder.place.setText("类型：" + mDatas.get(position).getmLabel());
            holder.price.setText("");
            holder.image.setImageResource(R.drawable.img7);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 点击事件
//                    Toast.makeText(getActivity(),"click",Toast.LENGTH_SHORT).show();
                    showNormalDialog(position);
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


        private void showNormalDialog(final int position){
            /* @setIcon 设置对话框图标
             * @setTitle 设置对话框标题
             * @setMessage 设置对话框消息提示
             * setXXX方法返回Dialog对象，因此可以链式设置属性
             */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(getActivity());
            normalDialog.setIcon(R.drawable.ic_cloud_done_black_24dp);
            normalDialog.setMessage("要删除这个失物？");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                            delete(position, normalDialog);
                            remove(position);
                        }
                    });
            normalDialog.setNegativeButton("关闭",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            // 显示
            normalDialog.show();
        }

        private void delete(int position, final AlertDialog.Builder normalDialo){
            final PushLostItem p2 = new PushLostItem();
            p2.setObjectId(mDatas.get(position).getObjectId());
            p2.delete(new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(getActivity(),"物品移除成功！",Toast.LENGTH_SHORT).show();
//                                normalDialo.
                    }else{
                        Toast.makeText(getActivity(),"删除失败，请检查网络",Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }

    }

    private void initVerticalData(){

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),1);
        mVerticalListView.setLayoutManager(manager);
        mVerticalListView.setHasFixedSize(true);//设置recycler不可滑动
        mVerticalListView.setNestedScrollingEnabled(false);

        Person user = BmobUser.getCurrentUser(Person.class);//先从云端读入数据
        query = new BmobQuery<>();
        query.addWhereEqualTo("mPerson", user);
        query.findObjects(new FindListener<PushLostItem>() {
            @Override
            public void done(final List<PushLostItem> object, BmobException e) {
                if(e==null){
                    mainVerticalList = new ArrayList<>();
                        new Thread(){
                            @Override
                            public void run() {
                                for (int i = 0 ; i < object.size() ; i++){
//                                    try {
                                        final PushLostItem lostItem = object.get(i);
                                        mainVerticalList.add(lostItem);
                                        length = mainVerticalList.size();
//                                        sleep(300);
//                                    } catch (InterruptedException e1) {
//                                        e1.printStackTrace();
//                                    }
                                }
                                Message msg = new Message();
                                msg.what = 0x0;
                                handler.sendMessage(msg);
                            }
                        }.start();
                }else{
                    Toast.makeText(getActivity(), "请检查网络配置",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void reFlesh(){
        swipeRefreshLayout = primeView.findViewById(R.id.my_get_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
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
                                    .setDuration(1200)
                                    .setStartDelay(i * 100)
                                    .start();
                        }

                        return true;
                    }
                });
    }

}