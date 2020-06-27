package com.ftn.mailClient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.IncomingMailProtocol;
import com.ftn.mailClient.viewModel.AddAccountViewModel;

//TODO implementirati
public class AddAccountDialog extends DialogFragment {
    private AddAccountViewModel addAccountViewModel;

    private EditText smtpEditText;
    private Spinner incomingProtocolSpinner;
    private EditText incomingEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_account, null);

        smtpEditText = view.findViewById(R.id.smtp_address_edit_text);
        incomingProtocolSpinner = view.findViewById(R.id.incomingMailProtocol);
        incomingEditText = view.findViewById(R.id.incomingMailAddressEditText);
        usernameEditText = view.findViewById(R.id.usernameAccountEditText);
        passwordEditText = view.findViewById(R.id.passwordAccountEditText);

        addAccountViewModel = new ViewModelProvider(this).get(AddAccountViewModel.class);

        builder.setView(view).setTitle("Add account").setNegativeButton(R.string.cancel, (dialog, which) -> {
            addAccountViewModel.reset();
            dismiss();
        }).setPositiveButton(R.string.add, (dialog, which) -> {
            if(addAccountViewModel.validate()){
                addAccountViewModel.addAccount();
                dismiss();
            }
            else Toast.makeText(getActivity(), R.string.account_not_valid, Toast.LENGTH_SHORT).show();
        });

        smtpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addAccountViewModel.setSmtpAddress(s.toString());
            }
        });
        ArrayAdapter<IncomingMailProtocol> incomingMailProtocolArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, IncomingMailProtocol.values());
        incomingProtocolSpinner.setAdapter(incomingMailProtocolArrayAdapter);
        incomingProtocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addAccountViewModel.setIncomingMailProtocol(incomingMailProtocolArrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        incomingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addAccountViewModel.setIncomingMailAddress(s.toString());
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                addAccountViewModel.setUsername(s.toString());
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
                addAccountViewModel.setPassword(s.toString());
            }
        });

        return builder.create();
    }
}
