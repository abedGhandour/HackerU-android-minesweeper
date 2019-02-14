package com.example.asus.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.asus.myapplication.GameActivity.SizeOfBoard;
import static com.example.asus.myapplication.GameActivity.numOfBombs;
public class SettingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void SubmitChanges(View view) {
        
        int A,B;
        EditText currentA= findViewById(R.id.editTextNumOfBombs);
        EditText currentB= findViewById(R.id.editTextSizeOfBoard);

        if(!isEmpty(currentA)) {
            A = Integer.parseInt(currentA.getText().toString());
        }
        else{
            Toast.makeText(this, "Missing Number Of Bombs", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isEmpty(currentB)){
        B= Integer.parseInt(currentB.getText().toString());
        }
        else {
            Toast.makeText(this, "Missing Size Of Board", Toast.LENGTH_SHORT).show();
            return;
        }

        if(A<=0){
            Toast.makeText(this, "Number Of bombs must be at least 1", Toast.LENGTH_SHORT).show();
        }else if(B<=0){
            Toast.makeText(this, "Size Of Board must be at least 1", Toast.LENGTH_SHORT).show();
        }else if(B*B<=A) {
            Toast.makeText(this, "Number Of bombs must be at least 1 less than Size of Board: "+B*B, Toast.LENGTH_SHORT).show();
        }
        else if(B>50){
            Toast.makeText(this, "Size Of Board is too Big", Toast.LENGTH_SHORT).show();
        }
        else {
            numOfBombs = A;
            SizeOfBoard = B;
            goToGameActivity(view);
        }
    }
    public void ChangeEditTextContent(View view) {
        Button buttonId=findViewById(view.getId());
        int buttonTag=Integer.parseInt(buttonId.getTag().toString());
        EditText current;
        int a;

        if(buttonTag==1){//editTextNumOfBombs
            current=  findViewById(R.id.editTextNumOfBombs);
            if(isEmpty(current)){
                current.setInputType(InputType.TYPE_CLASS_NUMBER);
                current.setText(String.valueOf(0), TextView.BufferType.EDITABLE);
                a=0;
            }else{
                a=Integer.parseInt(current.getText().toString());
            }
            if (buttonId==findViewById(R.id.btnAddNumOfBombs)) {
                a++;
            }
            else {
                a--;
            }
        }
        else {//buttonTag==2   editTextSizeOfBoard
             current= findViewById(R.id.editTextSizeOfBoard);
            if(isEmpty(current)){
                current.setInputType(InputType.TYPE_CLASS_NUMBER);
                current.setText(String.valueOf(0), TextView.BufferType.EDITABLE);
                a=0;
            }else {
                 a = Integer.parseInt(current.getText().toString());
            }
            if(buttonId==findViewById(R.id.btnAddSizeOfBoard)){
                a++;
            }
            else {
                a--;
            }

        }
        current.setInputType(InputType.TYPE_CLASS_NUMBER);
        current.setText(String.valueOf(a), TextView.BufferType.EDITABLE);
    }
    public void goToGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    private boolean isEmpty(EditText tempText) {
        return tempText.getText().toString().trim().length() <= 0;
    }

}