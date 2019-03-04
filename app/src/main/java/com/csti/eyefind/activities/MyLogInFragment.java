package com.csti.eyefind.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.csti.eyefind.R;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Context.MODE_PRIVATE;


public class MyLogInFragment extends Fragment {

    private EditText account;
    private EditText password;
    private Button Login;
    private Button register;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isremember;
    private String s_account;
    private String s_password;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyLogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLogInFragment newInstance(String param1, String param2) {
        MyLogInFragment fragment = new MyLogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_my_log_in, container, false);
        account = view.findViewById(R.id.account);
        password = view.findViewById(R.id.password);
        checkBox = view.findViewById(R.id.remember_pass);
        Login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);
        sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        isremember = sharedPreferences.getBoolean("isremember", false);
        if (isremember) {
            s_account = sharedPreferences.getString("account", "");
            s_password = sharedPreferences.getString("password", "");
            account.setText(s_account);
            password.setText(s_password);
            checkBox.setChecked(true);
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Person user = new Person();
                //此处替换为你的用户名
                user.setUsername(account.getText().toString());
                //此处替换为你的密码
                user.setPassword(password.getText().toString());
                user.login(new SaveListener<Person>() {
                    @Override
                    public void done(Person bmobUser, BmobException e) {
                        if (e == null) {
                            Person user = BmobUser.getCurrentUser(Person.class);
                            Toast.makeText(getActivity(),"欢迎回来"+user.getmName(),Toast.LENGTH_SHORT).show();

                            BmobQuery<LostItem> query = new BmobQuery<>();
                            query.addWhereEqualTo("mPerson", user);
                            query.findObjects(new FindListener<LostItem>() {
                                @Override
                                public void done(List<LostItem> object, BmobException e) {
                                    if(e==null){
                                        //Toast.makeText(LoginActivity.this,"成功"+object.size(),Toast.LENGTH_SHORT).show();
                                        String[] Lostthing = new String[object.size()];
                                        for(int i=0;i<object.size();i++){
                                            Lostthing[i]=object.get(i).getLabel();
                                        }
                                        BmobInstallationManager.getInstance().subscribe(Arrays.asList(Lostthing), new InstallationListener<BmobInstallation>() {
                                            @Override
                                            public void done(BmobInstallation bmobInstallation, BmobException e) {
                                                if (e == null) {
                                                    //Toast.makeText(LoginActivity.this, "批量订阅成功", Toast.LENGTH_SHORT).show();

                                                } else {
                                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                    }else{
                                        Toast.makeText(getActivity(),"失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                            editor=sharedPreferences.edit();
                            if (checkBox.isChecked()) {
                                editor.putString("account",user.getUsername());/////////////////////////////////
                                editor.putString("password",user.getmPassword());
                                editor.putBoolean("isremember",true);
                            }else {
                                editor.clear();
                            }
                            editor.apply();
                            MainActivity activity = (MainActivity)getActivity();
                            activity.setObjectId(user.getObjectId());
                            activity.setPerson(user);
                            activity.changeToHomePageFragment();
                        } else {
                            Toast.makeText(getActivity(),"失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                String objectId = ((MainActivity)getActivity()).getObjectId();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("objectId",objectId);
                editor.apply();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
