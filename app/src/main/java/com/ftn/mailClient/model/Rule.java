package com.ftn.mailClient.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.TypeConverters;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.utill.converters.ConditionTypeConverter;
import com.ftn.mailClient.utill.converters.FolderTypeConverter;
import com.ftn.mailClient.utill.converters.OperationTypeConverter;

@Entity
public class Rule extends Identifiable {
    @TypeConverters(ConditionTypeConverter.class)
    private Condition condition;
    @TypeConverters(OperationTypeConverter.class)
    private Operation operation;
    @TypeConverters(FolderTypeConverter.class)

    private FolderMetadata destinationFolder;
    private String value;

    @Ignore
    public Rule(){

    }
    public Rule(Long id, Condition condition, Operation operation, FolderMetadata destinationFolder, String value) {
        super(id);
        this.condition = condition;
        this.operation = operation;
        this.destinationFolder = destinationFolder;
        this.value = value;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @JsonGetter("destination")
    public FolderMetadata getDestinationFolder() {
        return destinationFolder;
    }

    @JsonSetter("destination")
    public void setDestinationFolder(FolderMetadata destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
