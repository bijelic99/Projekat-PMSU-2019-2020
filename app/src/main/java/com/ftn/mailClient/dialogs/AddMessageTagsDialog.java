package com.ftn.mailClient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Tag;
import com.ftn.mailClient.viewModel.AddMessageTagsViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddMessageTagsDialog extends DialogFragment {
    private Spinner incomingEditText;
    private ChipGroup tagsChipGroup;
    private AddMessageTagsViewModel addMessageTagsViewModel;

    public void setMessageId(Long messageId){
        if(messageId != null)
        addMessageTagsViewModel.setMessageId(messageId);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_tag, null);

        incomingEditText = view.findViewById(R.id.add_tag_dialog_edit_text);
        tagsChipGroup = view.findViewById(R.id.add_tag_dialog_chip_edit_text);

        addMessageTagsViewModel = new ViewModelProvider(this).get(AddMessageTagsViewModel.class);

        addMessageTagsViewModel.getTags().observe(this, tags -> {
            if(tags != null){
                tagsChipGroup.removeAllViews();
                for(Tag tag : tags){
                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.chip_tag, tagsChipGroup, false);
                    Chip chip = view.findViewById(R.id.tag_chip);
                    chip.setText(tag.getName());
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(v -> {
                        addMessageTagsViewModel.removeTag(tag.getId());
                    });
                }
            }
        });

        ArrayAdapter<Tag> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        addMessageTagsViewModel.getAllTags().observe(this, tags -> {
            if(tags != null) {
                arrayAdapter.clear();
                arrayAdapter.addAll(tags);
            }
        });
        incomingEditText.setAdapter(arrayAdapter);
        incomingEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                addMessageTagsViewModel.addTag(arrayAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view).setTitle("Add tags").setNegativeButton(R.string.cancel, (dialog, which) -> {
            dismiss();
        }).setPositiveButton(R.string.add, (dialog, which) -> {
            addMessageTagsViewModel.setTags();
            dismiss();
        });

        return builder.create();
    }
}
