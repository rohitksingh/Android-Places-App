package edu.asu.msse.rsingh92.assignment1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmationDialog extends AppCompatDialogFragment {

    private AlertDialog.Builder builder;
    private TextView messageTextView;
    private Button ok, cancel;
    private DialogCallBack dialogcallback;
    private String message;

    public ConfirmationDialog(String message){
        this.message = message;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_confirmation,null);
        messageTextView = view.findViewById(R.id.editText);
        ok = view.findViewById(R.id.ok);
        cancel = view.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        messageTextView.setText(message);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        builder.setView(view);
        return builder.create();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
