package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Identifiable;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.repository.MessageRepository;
import com.ftn.mailClient.utill.enums.FetchStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateEmailViewModel extends AndroidViewModel {
    private Long accountId;
    private Long userId;

    private ContactRepository contactRepository;
    private MessageRepository messageRepository;

    private MutableLiveData<List<Contact>> toList;
    private MutableLiveData<List<Contact>> ccList;
    private MutableLiveData<List<Contact>> bccList;
    private MutableLiveData<List<Uri>> attachmentList;
    private String subject;
    private String content;

    private MutableLiveData<String> toListFilter;
    private MutableLiveData<String> ccListFilter;
    private MutableLiveData<String> bccListFilter;
    public LiveData<List<Contact>> toListFiltered;
    public LiveData<List<Contact>> ccListFiltered;
    public LiveData<List<Contact>> bccListFiltered;


    public CreateEmailViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(application.getString(R.string.user_id)) && sharedPreferences.contains(application.getString(R.string.user_account_id))){
            userId = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        }
        else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
        contactRepository = new ContactRepository(application);
        messageRepository = new MessageRepository(application);
        this.toList = new MutableLiveData<>(new ArrayList<>());
        this.ccList = new MutableLiveData<>(new ArrayList<>());
        this.bccList = new MutableLiveData<>(new ArrayList<>());
        this.attachmentList = new MutableLiveData<>(new ArrayList<>());
        this.subject = "";
        this.content = "";
        contactRepository.fetchContacts(userId);
        toListFilter = new MutableLiveData<>("");
        ccListFilter = new MutableLiveData<>("");
        bccListFilter = new MutableLiveData<>("");
        this.toListFiltered = Transformations.switchMap(toListFilter, input -> contactRepository.getContacts(input));
        this.ccListFiltered = Transformations.switchMap(ccListFilter, input -> contactRepository.getContacts(input));
        this.bccListFiltered = Transformations.switchMap(bccListFilter, input -> contactRepository.getContacts(input));
    }

    public void setToSearchTerm(String term){
        this.toListFilter.setValue(term);
    }
    public void setCcSearchTerm(String term){
        this.ccListFilter.setValue(term);
    }
    public void setBccSearchTerm(String term){
        this.bccListFilter.setValue(term);
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LiveData<List<Contact>> getToList() {
        return toList;
    }

    public LiveData<List<Contact>> getCcList() {
        return ccList;
    }

    public LiveData<List<Contact>> getBccList() {
        return bccList;
    }

    public LiveData<List<Uri>> getAttachmentList() {
        return attachmentList;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Boolean addToToList(Contact... contacts){
        return addToMutableLiveDataList(toList, contacts);
    }

    public Boolean removeFromToList(int index){
        return removeFromMutableLiveDataList(toList, index);
    }

    public Boolean addToCCList(Contact... contacts){
        return addToMutableLiveDataList(ccList, contacts);
    }

    public Boolean removeFromCCList(int index){
        return removeFromMutableLiveDataList(ccList, index);
    }

    public Boolean addToBccList(Contact... contacts){
        return addToMutableLiveDataList(bccList, contacts);
    }

    public Boolean removeFromBccList(int index){
        return removeFromMutableLiveDataList(bccList, index);
    }

    public Boolean addToAttachmentList(Uri... attachmentUris){
        return addToMutableLiveDataList(attachmentList, attachmentUris);
    }

    public Boolean removeFromAttachmentList(int index){
        return removeFromMutableLiveDataList(attachmentList, index);
    }

    private static <K extends Object> Boolean removeFromMutableLiveDataList(MutableLiveData<List<K>> mutableLiveDataToRemoveFrom, int index){
        List<K> list = mutableLiveDataToRemoveFrom.getValue();
        if(list != null && index < list.size()){
            list.remove(index);
            mutableLiveDataToRemoveFrom.setValue(list);
            return true;
        }
        else return false;
    }

    private static <K extends Object> Boolean addToMutableLiveDataList(MutableLiveData<List<K>> mutableLiveDataToRemoveFrom, K... newItems){
        List<K> list = mutableLiveDataToRemoveFrom.getValue();
        if(list != null){
            list.addAll(Arrays.asList(newItems));
            mutableLiveDataToRemoveFrom.setValue(list);
            return true;
        }
        else {
            list = new ArrayList<>();
            mutableLiveDataToRemoveFrom.setValue(list);
            return false;
        }
    }

    public LiveData<FetchStatus> sendMessage(){
        return messageRepository.sendMessage(toList.getValue(), ccList.getValue(), bccList.getValue(), subject, content, attachmentList.getValue(), accountId);
    }

    public LiveData<FetchStatus> saveToDrafts(){
        return null;
    }
}
