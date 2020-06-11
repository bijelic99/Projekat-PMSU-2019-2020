package com.ftn.mailClient.model;

import androidx.annotation.Nullable;
import androidx.room.PrimaryKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

public abstract class Identifiable implements Serializable {
    @PrimaryKey
    protected Long id;

    public Identifiable() {
        this(null);
    }

    public Identifiable(Long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        else if(this.getClass() == obj.getClass()) {
            Identifiable identObj = (Identifiable) obj;
            if(this.getId() == identObj.getId()) return true;
        }
        return false;
    }
}
