package com.ftn.mailClient.model.linkingClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Folder;

@Entity(foreignKeys = {
        @ForeignKey(entity = Account.class, parentColumns = {"id"}, childColumns = {"accountId"}),
        @ForeignKey(entity = Folder.class, parentColumns = {"id"}, childColumns = {"folderId"})
})
public class AccountFolder {
    @NonNull
    @ColumnInfo(index = true)
    private Long accountId;
    @NonNull
    @PrimaryKey
    @ColumnInfo(index = true)
    private Long folderId;

    public AccountFolder(@NonNull Long accountId, Long folderId) {
        this.accountId = accountId;
        this.folderId = folderId;
    }

    @NonNull
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(@NonNull Long accountId) {
        this.accountId = accountId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
}
