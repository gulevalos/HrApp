package com.example.mardiak.marek.hrapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.example.mardiak.marek.hrapp.database.DepartmentTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mm on 3/23/2016.
 */
@RunWith(AndroidJUnit4.class)
public class OrgStructureContentProviderTest extends ProviderTestCase2<OrgStructureContentProvider> {

    public OrgStructureContentProviderTest() {
        super(OrgStructureContentProvider.class, OrgStructureContentProvider.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @Test  //TODO not woring sqlLiteHelper is null
    public void testInsertAndQuery() {
        ContentValues values = new ContentValues();
        values.clear();
        values.put(DepartmentTable.NAME_COLUMN, "dname1");
        MockContentResolver cp = getMockContentResolver();
        long id = ContentUris.parseId(cp.insert(OrgStructureContentProvider.INSERT_DEPARTMENT_URI, values));
        assertTrue("created OK", id > -1);
        long id2 = ContentUris.parseId(cp.insert(OrgStructureContentProvider.INSERT_DEPARTMENT_URI, values));
        assertTrue("id's increment", id2 > id);
        final Cursor queryResults = cp.query(OrgStructureContentProvider.DEPARTMENTS_ALL_URI, null, null, null, null);
        assertEquals(2, queryResults.getCount());
    }


}
