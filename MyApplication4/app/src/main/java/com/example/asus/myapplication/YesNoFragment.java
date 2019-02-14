package com.example.asus.myapplication;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.example.asus.myapplication.GameActivity.viewMode;
public class YesNoFragment extends DialogFragment implements DialogInterface.OnDismissListener {

    private String question;
    private YesNoFragmentListener yesNoFragmentListener;
    private View view;

    public void setQuestion(String question){
        this.question = question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_yes_no_fragment_listener, container, false);
        TextView lblQuestion = view.findViewById(R.id.lblQuestion);
        if(question != null)
            lblQuestion.setText(question);
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnNo = view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isYes = false;
            switch (view.getId()) {
                case R.id.btnYes: {
                    isYes = true;
                    break;
                }
                case R.id.btnNo: {
                    isYes = false;
                    break;
                }
            }
            if (yesNoFragmentListener != null)
                yesNoFragmentListener.onChoose(isYes);
            dismiss();
        }
    };
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        viewMode=true;

    }
    public void setYesNoFragmentListener(YesNoFragmentListener yesNoFragmentListener) {
        this.yesNoFragmentListener = yesNoFragmentListener;
    }

    public interface YesNoFragmentListener{
        void onChoose(boolean isYes);
    }
}