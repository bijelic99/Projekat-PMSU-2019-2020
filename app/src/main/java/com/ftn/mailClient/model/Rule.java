package com.ftn.mailClient.model;

import androidx.room.Entity;
import androidx.room.TypeConverters;
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
    private Folder destinationFolder;
    private String value;

    public Rule(){

    }
    public Rule(Long id, Condition condition, Operation operation, Folder destinationFolder, String value) {
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

    public Folder getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(Folder destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
