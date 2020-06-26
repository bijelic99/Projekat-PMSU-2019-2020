package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.*;
import com.ftn.mailClient.repository.FolderRepository;
import com.ftn.mailClient.repository.RuleRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.List;

public class CreateRuleViewModel extends AndroidViewModel {
    private Rule rule;
    private LiveData<List<FolderMetadata>> foldersLiveData;
    private FolderRepository folderRepository;
    private RuleRepository ruleRepository;
    private Long accountId;

    public CreateRuleViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        folderRepository = new FolderRepository(application);
        ruleRepository = new RuleRepository(application);
        if(sharedPreferences.contains(application.getString(R.string.user_account_id))){
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_choose_an_account, Toast.LENGTH_LONG).show();
        rule = new Rule();
        rule.setValue("");
    }

    public LiveData<List<FolderMetadata>> getFoldersLiveData() {
        if(foldersLiveData == null) foldersLiveData = folderRepository.getAllFoldersAsMetadata();
        return foldersLiveData;
    }

    public void setRuleOperation(Operation operation){
        rule.setOperation(operation);
    }

    public void setRuleCondition(Condition condition){
        rule.setCondition(condition);
    }

    public void setRuleValue(String value){
        rule.setValue(value);
    }

    public void setRuleDestinationFolder(FolderMetadata destinationFolder){
        rule.setDestinationFolder(destinationFolder);
    }

    public Boolean isRuleValid(){
        if(rule.getCondition() != null && rule.getOperation() != null && rule.getValue() != null){
            if(rule.getOperation().equals(Operation.MOVE) || rule.getOperation().equals(Operation.COPY)) {
                if(rule.getDestinationFolder() != null) return true;
                else return false;
            }
            else return true;
        }
        else return false;
    }

    public void resetRule(){
        rule = new Rule();
    }

    public void insertNewRule(){
        ruleRepository.insert(accountId, rule);
    }
}
