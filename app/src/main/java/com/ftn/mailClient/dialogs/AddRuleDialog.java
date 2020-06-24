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
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.FolderMetadata;
import com.ftn.mailClient.model.Operation;
import com.ftn.mailClient.viewModel.CreateEmailViewModel;
import com.ftn.mailClient.viewModel.CreateRuleViewModel;

import java.util.ArrayList;

public class AddRuleDialog extends DialogFragment {
    private CreateRuleViewModel createRuleViewModel;

    private Spinner conditionSpinner;
    private EditText valueEditText;
    private Spinner operationSpinner;
    private Spinner destinationSpinner;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_rule, null);

        createRuleViewModel = new ViewModelProvider(this).get(CreateRuleViewModel.class);

        builder.setView(view).setTitle(getString(R.string.add_rule)).setNegativeButton(R.string.cancel, (dialog, which) -> {
            createRuleViewModel.resetRule();
            dismiss();
        }).setPositiveButton(R.string.add, (dialog, which) -> {
            if(createRuleViewModel.isRuleValid()){
                createRuleViewModel.insertNewRule();
                dismiss();
            }
            else Toast.makeText(getActivity(), R.string.rule_not_valid, Toast.LENGTH_SHORT).show();
        });


        conditionSpinner = view.findViewById(R.id.condition_spinner);
        valueEditText = view.findViewById(R.id.value_edit_text);
        operationSpinner = view.findViewById(R.id.operation_spinner);
        destinationSpinner = view.findViewById(R.id.destination_spinner);

        ArrayAdapter<Condition> conditionArrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Condition.values());
        conditionSpinner.setAdapter(conditionArrayAdapter);
        conditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createRuleViewModel.setRuleCondition(conditionArrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Operation> operationArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Operation.values());
        operationSpinner.setAdapter(operationArrayAdapter);
        operationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Operation operation = operationArrayAdapter.getItem(position);
                createRuleViewModel.setRuleOperation(operation);
                if(operation.equals(Operation.DELETE)){
                    destinationSpinner.setEnabled(false);
                    createRuleViewModel.setRuleDestinationFolder(null);
                }
                else destinationSpinner.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                destinationSpinner.setEnabled(false);
            }
        });


        ArrayAdapter<FolderMetadata> folderArrayAdapter = new ArrayAdapter<>(getActivity(),  android.R.layout.simple_list_item_1, new ArrayList<>());
        destinationSpinner.setAdapter(folderArrayAdapter);
        createRuleViewModel.getFoldersLiveData().observe(getActivity(), folderMetadata -> {
            if(folderMetadata != null){
                folderArrayAdapter.clear();
                folderArrayAdapter.addAll(folderMetadata);
            }
        });
        destinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createRuleViewModel.setRuleDestinationFolder(folderArrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        valueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                createRuleViewModel.setRuleValue(s.toString());
            }
        });

        return builder.create();
    }
}
