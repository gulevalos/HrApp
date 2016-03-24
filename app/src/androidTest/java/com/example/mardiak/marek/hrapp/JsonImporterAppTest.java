package com.example.mardiak.marek.hrapp;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mm on 3/23/2016.
 */
@RunWith(AndroidJUnit4.class)
public class JsonImporterAppTest {

    @Mock
    private ContentResolver cr;
    @Mock
    private Context context;
    @Captor
    private ArgumentCaptor<ArrayList<ContentProviderOperation>> captor;

    JsonImporter jsonImporter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(context.getContentResolver()).thenReturn(cr);
        jsonImporter = new JsonImporter(context);
    }

    @Test
    public void testJsonImport() throws RemoteException, OperationApplicationException {
        jsonImporter.doInBackground(OrgStructureContentProvider.strJson); //TODO not nice, testing protected method, but execute runs on different thread
        Mockito.verify(cr).applyBatch(OrgStructureContentProvider.AUTHORITY, captor.capture());
        List<ArrayList<ContentProviderOperation>> allValues = captor.getAllValues();
        Assert.assertFalse(allValues.isEmpty());

        /*contentResolver.applyBatch(OrgStructureContentProvider.AUTHORITY, ops); //TODO verify
        contentResolver.notifyChange(OrgStructureContentProvider.DEPARTMENTS_ALL_URI, null); //TODO verify
        contentResolver.notifyChange(OrgStructureContentProvider.EMPLOYEES_BY_DEPARTMENT_ID_URI, null);
        contentResolver.notifyChange(OrgStructureContentProvider.EMPLOYEES_ALL_URI, null);*/
    }

}

