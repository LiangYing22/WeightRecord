package com.example.lying.weightrecord;

import android.content.ContentValues;
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

import com.example.lying.weightrecord.util.Util;

public class RegisterFragment extends Fragment implements TextWatcher {

    EditText registerUserId,registerUserName,registerUserPW;
    Button registerRegisterButton;
    TextView registerErrorTip,registerToLogin;

    RegisterClickListener registerClickListener;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase mydatabase;
    private Context context;

    public interface RegisterClickListener{
        public void registerClick();
        public void registerToLogin();
    }

    public void setRegisterClickListener(RegisterClickListener registerClickListener){
        this.registerClickListener = registerClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View RegisterView = inflater.inflate(R.layout.fragment_register, container, false);
        //绑定控件
        registerUserId = (EditText)RegisterView.findViewById(R.id.registerUserId);
        registerUserName = (EditText)RegisterView.findViewById(R.id.registerUserName);
        registerUserPW = (EditText)RegisterView.findViewById(R.id.registerUserPW);
        registerRegisterButton = (Button)RegisterView.findViewById(R.id.registerRegisterButton);
        registerErrorTip = (TextView)RegisterView.findViewById(R.id.registerErrorTip);
        registerToLogin = (TextView)RegisterView.findViewById(R.id.registerToLogin);
        //监听事件
        registerUserId.addTextChangedListener(this);
        registerUserName.addTextChangedListener(this);
        registerUserPW.addTextChangedListener(this);
        registerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserId = registerUserId.getText().toString().trim();
                String UserName = registerUserName.getText().toString().trim();
                String UserPW = registerUserPW.getText().toString().trim();
                boolean isHaveTheUser = Util.haveTheUser(UserId,mydatabase);
                if(!UserId.equals("") && !UserName.equals("") && !UserPW.equals("")){
                    if(!isHaveTheUser){
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("UserID",UserId);
                        contentValues.put("UserRealName",UserName);
                        contentValues.put("UserPassWord",UserPW);
                        mydatabase.insert(MyDBHelper.Table_User_NANE, null, contentValues);
                        registerClickListener.registerClick();
                        //创建相应的体重表
                        MyDBHelper.createWeightTable(mydatabase,UserId);
                    }else{
                        registerErrorTip.setVisibility(View.VISIBLE);
                    }
                }else {
                    registerErrorTip.setVisibility(View.VISIBLE);
                }
            }
        });
        registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClickListener.registerToLogin();
            }
        });
        return RegisterView;
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
        registerErrorTip.setVisibility(View.GONE);
    }

}
