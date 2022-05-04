package com.example.todo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todo.Model.TodoGroupModel;
import com.example.todo.Utils.DBHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewGroup extends BottomSheetDialogFragment {
    public static final String TAG = "Add new group.";

    //widgets
    private EditText editText;
    private Button saveButton;

    private DBHelper db;

    public static AddNewGroup newInstance() {
        return new AddNewGroup();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_group, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.groupEditText);
        saveButton = view.findViewById(R.id.groupSaveButton);

        db = new DBHelper(getActivity());

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            Log.d(TAG, "onViewCreated: ----------------------------------------------57");
            String group = bundle.getString("group");
            editText.setText(group);

            if (group.length() > 0) {
                Log.d(TAG, "onViewCreated: ----------------------------------------------62");

                saveButton.setEnabled(false);
            }
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                if(text.length()==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please Enter Group Name.");
                    builder.setMessage("Group name cannot be empty.");
                    builder.setPositiveButton(R.string.validation_ok,null);
                    builder.show();

                }else {
                    if (finalIsUpdate) {
                        db.updateGroupName(bundle.getInt("id"), text);

                    } else {
                        TodoGroupModel group = new TodoGroupModel();
                        group.setName(text);
                        group.setStatus(0);
                        db.insertGroup(group);

                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
