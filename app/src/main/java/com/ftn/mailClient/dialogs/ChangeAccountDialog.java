package com.ftn.mailClient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Operation;
import com.ftn.mailClient.viewModel.AddProfileViewModel;
import com.ftn.mailClient.viewModel.CreateRuleViewModel;

import java.util.ArrayList;

public class ChangeAccountDialog extends DialogFragment {

    private AddProfileViewModel addProfileViewModel;

    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_account, null);

        addProfileViewModel = new ViewModelProvider(this).get(AddProfileViewModel.class);

        builder.setView(view).setTitle(getString(R.string.add_profile)).setNegativeButton(R.string.cancel, (dialog, which) -> {
            addProfileViewModel.resetUser();
            dismiss();
        }).setPositiveButton(R.string.add, (dialog, which) -> {

            addProfileViewModel.insertNewUser();
            dismiss();
        });


        userNameEditText = view.findViewById(R.id.username_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        lastNameEditText = view.findViewById(R.id.last_name_edit_text);

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addProfileViewModel.setUserUsername(s.toString());
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addProfileViewModel.setUserPassword(s.toString());
            }
        });

        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addProfileViewModel.setUserFirstName(s.toString());
            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addProfileViewModel.setUserLastName(s.toString());
            }
        });

        return builder.create();
    }
}
