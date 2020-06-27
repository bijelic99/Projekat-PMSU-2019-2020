package com.ftn.mailClient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.viewModel.ChangeAccountViewModel;

//TODO implementirati
public class ChangeAccountDialog extends DialogFragment {
    private ChangeAccountViewModel changeAccountViewModel;

    private Spinner accountSpinner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_account, null);

        changeAccountViewModel = new ViewModelProvider(this).get(ChangeAccountViewModel.class);

        accountSpinner = view.findViewById(R.id.change_account_spinner);

        builder.setView(view).setTitle("Choose an account").setNegativeButton(R.string.cancel, (dialog, which) -> {
            dismiss();
        }).setPositiveButton(R.string.choose, (dialog, which) -> {
            changeAccountViewModel.chooseNewAccount();
            dismiss();
        });

        ArrayAdapter<Account> accountArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        changeAccountViewModel.getAccountLiveData().observe(this, accounts -> {
            if (accounts != null) {
                accountArrayAdapter.clear();
                accountArrayAdapter.addAll(accounts);
                if (changeAccountViewModel.getCurrentAccountId() != null) {
                    Account account = new Account();
                    account.setId(changeAccountViewModel.getCurrentAccountId());
                    int index = accounts.indexOf(account);
                    accountSpinner.setSelection(index);
                }
            }
        });


        accountSpinner.setAdapter(accountArrayAdapter);
        accountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Account account = accountArrayAdapter.getItem(position);
                changeAccountViewModel.setCurrentAccountId(account.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return builder.create();
    }
}
