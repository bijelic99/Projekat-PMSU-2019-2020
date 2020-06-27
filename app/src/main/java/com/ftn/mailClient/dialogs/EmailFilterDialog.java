package com.ftn.mailClient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.ftn.mailClient.R;
import com.ftn.mailClient.utill.FilterEmail;
import com.ftn.mailClient.viewModel.AccountEmailsViewModel;

public class EmailFilterDialog extends DialogFragment {

    private AccountEmailsViewModel accountEmailsViewModel;

    public EmailFilterDialog(AccountEmailsViewModel accountEmailsViewModel) {
        super();
        this.accountEmailsViewModel = accountEmailsViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_email_filter, null);

        builder.setView(view).setNegativeButton(R.string.cancel, (dialog, which) -> {
            dismiss();
        }).setPositiveButton(R.string.filter, (dialog, which) -> {
            EditText searchCriteria = view.findViewById(R.id.filter_text);
            accountEmailsViewModel.setFilter(new FilterEmail(searchCriteria.getText().toString()));
        });

        return builder.create();
    }

}
