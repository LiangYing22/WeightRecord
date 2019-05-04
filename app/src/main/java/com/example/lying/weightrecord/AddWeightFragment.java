package com.example.lying.weightrecord;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lying.weightrecord.util.Util;

public class AddWeightFragment extends Fragment {

    TextView addTips;
    EditText addInput;
    Button addButton;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase mydatabase;
    private Context context;

    private boolean isAdd = false;
    String TableName = MyDBHelper.Table_Weight_NANE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addView = inflater.inflate(R.layout.fragment_add_weight, container, false);
        addTips = (TextView)addView.findViewById(R.id.addTips);
        addInput = (EditText)addView.findViewById(R.id.addInput);
        addInput.addTextChangedListener(new myTextChangedListener());
        addButton = (Button)addView.findViewById(R.id.addButton);
        if(isAdd){
            addTips.setText("今天已经添加了！");
            addInput.setEnabled(false);
            addButton.setEnabled(false);
        }else{
            addTips.setText("快添加体重记录吧！");
            addInput.setEnabled(true);
            addButton.setEnabled(false);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加入数据库
                String input = addInput.getText().toString().trim();
                double inputDouble = 0.0;
                try{
                    inputDouble = Double.parseDouble(input);
                }catch (Exception e){
                }
                String SpecificDate = Util.getCurrentData();
                String SpecificTime = Util.getCurrentTime();
                String Date = Util.getDate(SpecificDate);
                SharedPreferences sharedPreferences= context.getSharedPreferences("User", Context .MODE_PRIVATE);
                String userId=sharedPreferences.getString("UserID","游客");
                String Name = Util.getName(userId,mydatabase);
                Cursor cursor_for_weight = mydatabase.rawQuery("select * from " + TableName, null);
                int count = cursor_for_weight.getCount();
                ContentValues contentValues = new ContentValues();
                contentValues.put("WeightID",count+1);
                contentValues.put("WeightDate",SpecificDate);
                contentValues.put("WeightSpecificDate",SpecificDate);
                contentValues.put("WeightSpecificTime",SpecificTime);
                contentValues.put("WeightName",Name);
                contentValues.put("WeightText",input+" kg("+inputDouble*2+" 斤)");
                mydatabase.insert(TableName, null, contentValues);
                cursor_for_weight.close();
                addTips.setText("今天已经添加了！");
                addInput.setEnabled(false);
                addButton.setEnabled(false);
            }
        });
        return addView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        myDBHelper=new MyDBHelper(context);
        mydatabase=myDBHelper.getWritableDatabase();
        //获取表名
        TableName = MyDBHelper.Table_Weight_NANE;
        SharedPreferences sharedPreferences= context.getSharedPreferences("User", Context .MODE_PRIVATE);
        String userId=sharedPreferences.getString("UserID","游客");
        if(userId.equals("游客")){
            TableName = MyDBHelper.Table_Weight_NANE;
        }else{
            TableName = userId;
        }
        Cursor cursor = mydatabase.rawQuery("select * from " + TableName
                + " where WeightSpecificDate = ? " ,new String[]{Util.getCurrentData()},null);
        if(cursor.getCount() > 0){
            isAdd = true;
        }
        cursor.close();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mydatabase.close();
        myDBHelper.close();
    }

    private class myTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //主要在这里写
            String input = addInput.getText().toString().trim();
            try{
                Double.valueOf(input).intValue();
                if(input != null && !input.equals("")){
                    addButton.setEnabled(true);
                    addButton.setText("确定");
                }
            }catch (Exception e){
                addButton.setEnabled(false);
                addButton.setText("请输入数字");
            }
        }
    }
}
