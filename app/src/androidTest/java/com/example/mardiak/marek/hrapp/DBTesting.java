package com.example.mardiak.marek.hrapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.mardiak.marek.hrapp.database.OrgStructureDatabaseHelper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by mm on 3/23/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class DBTesting{

    private OrgStructureDatabaseHelper dbHelper;

    Context mMockContext;



    @Before
    public void setUp() throws Exception {
        mMockContext = new RenamingDelegatingContext(InstrumentationRegistry.getInstrumentation().getTargetContext(), "test_");

        mMockContext.deleteDatabase(OrgStructureDatabaseHelper.DATABASE_NAME);
        dbHelper = new OrgStructureDatabaseHelper(mMockContext);
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.close();
    }

    @Test
    public void shouldAddExpenseType() throws Exception {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Assert.assertTrue(db.isOpen());
    }
}
