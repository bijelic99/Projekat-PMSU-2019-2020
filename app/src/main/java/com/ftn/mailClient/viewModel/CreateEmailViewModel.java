package com.ftn.mailClient.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Telephony;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.repository.ContactRepository;
import com.ftn.mailClient.repository.MessageRepository;
import com.ftn.mailClient.utill.LoadAttachmentsAsyncTask;
import com.ftn.mailClient.utill.SetSubjectAndContentFuncInterface;
import com.ftn.mailClient.utill.enums.FetchStatus;
import com.ftn.mailClient.utill.enums.MessageResponseType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateEmailViewModel extends AndroidViewModel {
    private Long accountId;
    private Long userId;

    private ContactRepository contactRepository;
    private MessageRepository messageRepository;

    private Long messageId;
    private MutableLiveData<List<Contact>> toList;
    private MutableLiveData<List<Contact>> ccList;
    private MutableLiveData<List<Contact>> bccList;
    private MutableLiveData<List<Uri>> attachmentUriList;
    private MutableLiveData<List<Attachment>> attachmentList;
    private String subject;
    private String content;

    private MutableLiveData<String> toListFilter;
    private MutableLiveData<String> ccListFilter;
    private MutableLiveData<String> bccListFilter;
    public LiveData<List<Contact>> toListFiltered;
    public LiveData<List<Contact>> ccListFiltered;
    public LiveData<List<Contact>> bccListFiltered;
    private MessageResponseType messageResponseType;


    public CreateEmailViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences sharedPreferences = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(application.getString(R.string.user_id)) && sharedPreferences.contains(application.getString(R.string.user_account_id))) {
            userId = sharedPreferences.getLong(application.getString(R.string.user_id), -55L);
            accountId = sharedPreferences.getLong(application.getString(R.string.user_account_id), -55L);
        } else Toast.makeText(application, R.string.need_to_be_logged_in, Toast.LENGTH_LONG).show();
        contactRepository = new ContactRepository(application);
        messageRepository = new MessageRepository(application);
        this.toList = new MutableLiveData<>(new ArrayList<>());
        this.ccList = new MutableLiveData<>(new ArrayList<>());
        this.bccList = new MutableLiveData<>(new ArrayList<>());
        this.attachmentUriList = new MutableLiveData<>(new ArrayList<>());
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
        messageResponseType = MessageResponseType.NONE;
    }

    public MessageResponseType getMessageResponseType() {
        return this.messageResponseType;
    }

    public MutableLiveData<List<Attachment>> getAttachmentList() {
        return attachmentList;
    }

    public void setMessageId(Long messageId, @NonNull MessageResponseType messageResponseType, SetSubjectAndContentFuncInterface ifc) {
        this.messageId = messageId;
        this.messageResponseType = messageResponseType;
        if (!messageResponseType.equals(MessageResponseType.NONE)) {
            LiveData<Message> messageLiveData = this.messageRepository.getById(messageId);
            switch (this.messageResponseType) {
                case DRAFT: {
                    messageLiveData.observeForever(new Observer<Message>() {
                        @Override
                        public void onChanged(Message message) {
                            if (message != null) {
                                toList.setValue(message.getTo());
                                ccList.setValue(message.getCc());
                                bccList.setValue(message.getBcc());
                                attachmentList.setValue(message.getAttachments());
                                ifc.setSubjectAndContent(message.getSubject(), message.getContent());
                                //subject = message.getSubject();
                                //content = message.getContent();

                                messageLiveData.removeObserver(this);
                            }
                        }
                    });
                    break;
                }
                case FORWARD: {
                    messageLiveData.observeForever(new Observer<Message>() {
                        @Override
                        public void onChanged(Message message) {
                            if (message != null) {

                                attachmentList.setValue(message.getAttachments());
                                ifc.setSubjectAndContent(message.getSubject(), "FWD: \n\n" + message.getContent());
                                //subject = message.getSubject();
                                //content = "FWD: \n\n"+ message.getContent();


                                messageLiveData.removeObserver(this);
                            }
                        }
                    });
                    break;
                }
                case REPLY: {
                    messageLiveData.observeForever(new Observer<Message>() {
                        @Override
                        public void onChanged(Message message) {
                            if (message != null) {

                                List<Contact> a = toList.getValue();
                                a.add(message.getFrom());
                                toList.setValue(a);
                                ccList.setValue(message.getCc());
                                attachmentList.setValue(message.getAttachments());
                                ifc.setSubjectAndContent(message.getSubject(), "\n\nRE: \n\n" + message.getContent());
                                //subject = message.getSubject();
                                //content = "RE: \n\n"+ message.getContent();

                                messageLiveData.removeObserver(this);
                            }
                        }
                    });
                    break;
                }
                case REPLY_ALL: {
                    messageLiveData.observeForever(new Observer<Message>() {
                        @Override
                        public void onChanged(Message message) {
                            if (message != null) {

                                List<Contact> a = toList.getValue();
                                a.add(message.getFrom());
                                a.addAll(message.getCc());
                                toList.setValue(a);
                                attachmentList.setValue(message.getAttachments());
                                ifc.setSubjectAndContent(message.getSubject(), "RE_ALL: \n\n" + message.getContent());
                                subject = message.getSubject();
                                content = "\n\nRE_ALL: \n\n" + message.getContent();

                                messageLiveData.removeObserver(this);
                            }
                        }
                    });
                }
            }
        }


    }


    public void setToSearchTerm(String term) {
        this.toListFilter.setValue(term);
    }

    public void setCcSearchTerm(String term) {
        this.ccListFilter.setValue(term);
    }

    public void setBccSearchTerm(String term) {
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

    public LiveData<List<Uri>> getAttachmentUriList() {
        return attachmentUriList;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public Boolean addToToList(Contact... contacts) {
        return addToMutableLiveDataList(toList, contacts);
    }

    public Boolean removeFromToList(int index) {
        return removeFromMutableLiveDataList(toList, index);
    }

    public Boolean addToCCList(Contact... contacts) {
        return addToMutableLiveDataList(ccList, contacts);
    }

    public Boolean removeFromCCList(int index) {
        return removeFromMutableLiveDataList(ccList, index);
    }

    public Boolean addToBccList(Contact... contacts) {
        return addToMutableLiveDataList(bccList, contacts);
    }

    public Boolean removeFromBccList(int index) {
        return removeFromMutableLiveDataList(bccList, index);
    }

    public Boolean addToAttachmentList(Uri... attachmentUris) {

        if (this.messageResponseType.equals(MessageResponseType.NONE))
            return addToMutableLiveDataList(attachmentUriList, attachmentUris);
        else {
            List<Attachment> attachments = attachmentList.getValue();
            if (attachments == null) attachments = new ArrayList<>();
            List<Attachment> finalAttachments = attachments;
            new LoadAttachmentsAsyncTask(value -> {
                if (value != null) {
                    finalAttachments.addAll(value);
                    attachmentList.setValue(finalAttachments);
                }
            }, getApplication().getContentResolver()).execute(attachmentUris);
            return true;
        }
    }

    public Boolean removeFromAttachmentList(int index) {
        if (this.messageResponseType.equals(MessageResponseType.NONE))
            return removeFromMutableLiveDataList(attachmentUriList, index);
        else return removeFromMutableLiveDataList(attachmentList, index);
    }

    private static <K extends Object> Boolean removeFromMutableLiveDataList(MutableLiveData<List<K>> mutableLiveDataToRemoveFrom, int index) {
        List<K> list = mutableLiveDataToRemoveFrom.getValue();
        if (list != null && index < list.size()) {
            list.remove(index);
            mutableLiveDataToRemoveFrom.setValue(list);
            return true;
        } else return false;
    }

    private static <K extends Object> Boolean addToMutableLiveDataList(MutableLiveData<List<K>> mutableLiveDataToRemoveFrom, K... newItems) {
        List<K> list = mutableLiveDataToRemoveFrom.getValue();
        if (list != null) {
            list.addAll(Arrays.asList(newItems));
            mutableLiveDataToRemoveFrom.setValue(list);
            return true;
        } else {
            list = new ArrayList<>();
            mutableLiveDataToRemoveFrom.setValue(list);
            return false;
        }
    }

    public LiveData<FetchStatus> sendMessage() {
        if (this.messageResponseType.equals(MessageResponseType.DRAFT)) messageRepository.deleteById(messageId);
        if (this.messageResponseType.equals(MessageResponseType.NONE))
            return messageRepository.sendMessage(toList.getValue(), ccList.getValue(), bccList.getValue(), subject, content, attachmentUriList.getValue(), accountId);
        else {
            return messageRepository.sendMessage2(toList.getValue(), ccList.getValue(), bccList.getValue(), subject, content, attachmentList.getValue(), accountId);
        }
    }

    public void saveToDrafts() {
        if (this.messageResponseType.equals(MessageResponseType.NONE))
            if (toList.getValue().size() > 0 || ccList.getValue().size() > 0 || bccList.getValue().size() > 0 || !subject.isEmpty() || !content.isEmpty() || attachmentUriList.getValue().size() > 0) {
                LiveData<FetchStatus> liveData = messageRepository.addToDrafts(toList.getValue(), ccList.getValue(), bccList.getValue(), subject, content, attachmentUriList.getValue(), accountId);
                liveData.observeForever(new Observer<FetchStatus>() {
                    @Override
                    public void onChanged(FetchStatus fetchStatus) {
                        if (fetchStatus.equals(FetchStatus.DONE) || fetchStatus.equals(FetchStatus.ERROR)) {
                            if (fetchStatus.equals(FetchStatus.DONE))
                                Toast.makeText(getApplication(), R.string.message_saved_to_drafts, Toast.LENGTH_SHORT).show();
                            else if (fetchStatus.equals(FetchStatus.ERROR))
                                Toast.makeText(getApplication(), R.string.failed_to_save_to_drafts, Toast.LENGTH_SHORT).show();
                            liveData.removeObserver(this);
                        }
                    }
                });
            }

    }
}
