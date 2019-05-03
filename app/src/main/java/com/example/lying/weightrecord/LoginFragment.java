package com.example.lying.weightrecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lying.weightrecord.util.Util;

public class LoginFragment extends Fragment implements TextWatcher {

    EditText loginUserId,loginUserPW;
    Button loginLoginButton;
    TextView loginError,loginToRegister;

    LoginClickListener loginClickListener;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase mydatabase;
    private Context context;

    public interface LoginClickListener{
        public void loginClick(String UserID);
        public void loginToRegisterClick();
    }

    public void setLoginClickListener(LoginClickListener loginClickListener){
        this.loginClickListener = loginClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView =  inflater.inflate(R.layout.fragment_login, container, false);
        //绑定控件
        loginUserId = (EditText)LoginView.findViewById(R.id.loginUserId);
        loginUserPW = (EditText)LoginView.findViewById(R.id.loginUserPW);
        loginLoginButton = (Button)LoginView.findViewById(R.id.loginLoginButton);
        loginError = (TextView)LoginView.findViewById(R.id.loginError);
        loginToRegister = (TextView)LoginView.findViewById(R.id.loginToRegister);
        //监听事件
        loginUserId.addTextChangedListener(this);
        loginUserPW.addTextChangedListener(this);
        loginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserIDText = loginUserId.getText().toString().trim();
                String UserPWText = loginUserPW.getText().toString().trim();
                boolean isHaveTheUser = Util.haveTheUser(UserIDText,UserPWText,mydatabase);
                if(isHaveTheUser){
                    loginClickListener.loginClick(UserIDText);
                    Toast.makeText(context,"登陆成功!",Toast.LENGTH_SHORT).show();
                }else {
                    loginError.setVisibility(View.VISIBLE);
                }
            }
        });
        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClickListener.loginToRegisterClick();
            }
        });
        return LoginView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        myDBHelper=new MyDBHelper(context);
        mydatabase=myDBHelper.getWritableDatabase();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mydatabase.close();
        myDBHelper.close();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        loginError.setVisibility(View.GONE);
    }
}
