package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

//        Authentication
        Authentication.authenticate(Credentials.CLIENT_ID, Credentials.CLIENT_SECRET, Credentials.USERNAME, Credentials.PASSWORD);

//        Getting Custom Object
        String objectName = "Book__c";
        String recordId = "a02WU000004mFLq";
//        System.out.println(GetObjectData.getCustomObject(objectName, recordId));
//        GetObjectData.getAllRecords();

//        Insert A Record
//        InsertRecord.insertRecord(objectName);

//        Update Data
//        UpdateRecord.updateRecord(objectName, recordId);

//        Delete Record
//        DeleteRecord.deleteRecord(objectName, recordId);

//        Bulk Delete
//        BulkDelete.bulkDelete(objectName);

//        Bulk Insert
//        BulkInsert.bulkInsert(objectName);
    }


}