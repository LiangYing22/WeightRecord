package com.example.lying.weightrecord;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,View.OnTouchListener {

    ImageView barImageView;
    DrawerLayout drawer_layout;
    FrameLayout WeightFrameLayout;
    TextView barBackHome;

    RelativeLayout memuUserView;//菜单用户名一行
    TextView memuTextView;//菜单用户名
    TextView memuAddWeightView,memuLogOut;


    //当前Fragment
    Fragment nowFragment;

    //HomeFragment
    HomeFragment homeFragment = new HomeFragment();
    LoginFragment loginFragment = new LoginFragment();
    RegisterFragment registerFragment = new RegisterFragment();

    //定义手势检测器实例
    GestureDetector detector;

    //drawerLayout的宽
    int drawerLayoutWidth = 0;

    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //绑定控件
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        barImageView = (ImageView)findViewById(R.id.barImageView);
        WeightFrameLayout = (FrameLayout)findViewById(R.id.WeightFrameLayout);
        barBackHome = (TextView)findViewById(R.id.barBackHome);
        memuAddWeightView = (TextView)findViewById(R.id.memuAddWeightView);
        memuUserView = (RelativeLayout)findViewById(R.id.memuUserView);
        memuTextView = (TextView)findViewById(R.id.memuTextView);
        memuLogOut = (TextView)findViewById(R.id.memuLogOut);

        //initData;
        initData();

        //响应事件
        barImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        WeightFrameLayout.setOnTouchListener(this);
        barBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //回到主Fragment
                changeFragment(nowFragment,homeFragment);
            }
        });
        memuAddWeightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换到addFragment
                if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                    drawer_layout.closeDrawer(Gravity.LEFT);
                }
                changeFragment(nowFragment,new AddWeightFragment());
            }
        });
        memuLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memuTextView.setText("游客");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserID",memuTextView.getText().toString());
                editor.commit();
                changeFragment(nowFragment,homeFragment);
                memuLogOut.setVisibility(View.GONE);
            }
        });
        memuUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memuTextView.getText().toString().equals("游客")){
                    //切换到loginFragment
                    if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                        drawer_layout.closeDrawer(Gravity.LEFT);
                    }
                    changeFragment(nowFragment,loginFragment);
                }
            }
        });
    }

    //framelayout中fragment的切换
    private void changeFragment(Fragment fromFragment, Fragment toFragment) {

        if (nowFragment != toFragment) {
            nowFragment = toFragment;
        }
        //当前页面不是HomeFragment就显示“返回主页”，否则不显示。
        if(nowFragment == homeFragment){
            barBackHome.setVisibility(View.GONE);
        }else{
            barBackHome.setVisibility(View.VISIBLE);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (toFragment.isAdded() == false) {
            ft.hide(fromFragment).add(R.id.WeightFrameLayout, toFragment).commit();
        } else {
            ft.hide(fromFragment).show(toFragment).commit();
        }

    }

    //initData
    private void initData(){
        //创建sharedPreferences。
        sharedPreferences= getSharedPreferences("User", Context.MODE_PRIVATE);
        //根据sharedPreferences中的数据初始化登陆
        String spUserId = sharedPreferences.getString("UserID","游客");
        if(spUserId != null && !spUserId.equals("") && !spUserId.equals("游客")){
            memuTextView.setText(spUserId);
            memuLogOut.setVisibility(View.VISIBLE);
        }
        //把用户账号写入SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserID",memuTextView.getText().toString());
        editor.commit();
        //判断“退出登陆显不显示
        if(memuTextView.getText().toString().equals("游客")){
            memuLogOut.setVisibility(View.GONE);
        }else{
            memuLogOut.setVisibility(View.VISIBLE);
        }
        //初始化手势检测器
        detector = new GestureDetector(this,this);
        //drawerlayout的宽
        drawerLayoutWidth = drawer_layout.getWidth();
        nowFragment = homeFragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.WeightFrameLayout,homeFragment,"home").commit();
        //当前页面不是HomeFragment就显示“返回主页”，否则不显示。
        if(nowFragment == homeFragment){
            barBackHome.setVisibility(View.GONE);
        }else{
            barBackHome.setVisibility(View.VISIBLE);
        }
        //给登陆添加回调响应
        loginFragment.setLoginClickListener(new LoginFragment.LoginClickListener() {
            @Override
            public void loginClick(String UserID) { //点击登陆时
                memuTextView.setText(UserID);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("UserID",UserID);
                editor.commit();
                changeFragment(nowFragment,homeFragment);
                memuLogOut.setVisibility(View.VISIBLE);
            }

            @Override
            public void loginToRegisterClick() { //点击去注册时
                changeFragment(nowFragment,registerFragment);
            }
        });
        //给注册添加回调响应
        registerFragment.setRegisterClickListener(new RegisterFragment.RegisterClickListener() {
            @Override
            public void registerClick() {
                changeFragment(nowFragment,loginFragment);
            }

            @Override
            public void registerToLogin() {
                changeFragment(nowFragment,loginFragment);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //交给手势检测器处理
        return detector.onTouchEvent(event);
    }

    //按返回键 收起 DrawerLayout
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.i("startend","onDown");
        //不返回true其他手势不会响应
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //若移动的距离大于0，则弹出左侧菜单
        float start = e1.getX();
        float end = e2.getX();
        if(end - start > 0.0){
            drawer_layout.openDrawer(GravityCompat.START);
        }
        Log.i("startend","start="+start+",end="+end);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float start = e1.getX();
        float end  = e2.getX();
        if(end - start > 0.0 ){
            drawer_layout.openDrawer(GravityCompat.START);
        }
        Log.i("startend","huangdongzhong");
        return false;
    }

}
