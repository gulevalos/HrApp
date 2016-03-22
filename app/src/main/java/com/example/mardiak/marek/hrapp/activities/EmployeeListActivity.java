package com.example.mardiak.marek.hrapp.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mardiak.marek.hrapp.JsonImporter;
import com.example.mardiak.marek.hrapp.OrgStructureContentProvider;
import com.example.mardiak.marek.hrapp.R;
import com.example.mardiak.marek.hrapp.database.DepartmentTable;
import com.example.mardiak.marek.hrapp.database.EmployeeTable;
import com.squareup.picasso.Picasso;

/**
 * Created by mm on 3/9/2016.
 */
public class EmployeeListActivity extends ListActivity {

    private SimpleCursorAdapter employeesListAdapter;
    private SimpleCursorAdapter departmentSpinnerAdapter;

    private LoaderManager.LoaderCallbacks<Cursor> departmentSpinnerLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = {DepartmentTable.ID_COLUMN, DepartmentTable.NAME_COLUMN};
            Uri uri = OrgStructureContentProvider.DEPARTMENTS_ALL_URI;
            CursorLoader cursorLoader = new CursorLoader(EmployeeListActivity.this,
                    uri, projection, null, null, null);
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            departmentSpinnerAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            departmentSpinnerAdapter.swapCursor(null);
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> employeeListLoaderListener
            = createEmployeeListLoaderListener(null);

    @NonNull
    private LoaderManager.LoaderCallbacks<Cursor> createEmployeeListLoaderListener(final Long departmentId) {
        return new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] projection = {EmployeeTable.ID_COLUMN, EmployeeTable.LAST_NAME__COLUMN, EmployeeTable.FIRST_NAME__COLUMN, EmployeeTable.AVATAR__COLUMN};
                Uri uri = OrgStructureContentProvider.EMPLOYEES_ALL_URI;
                if (departmentId != null) {
                    uri = ContentUris.withAppendedId(OrgStructureContentProvider.EMPLOYEES_BY_DEPARTMENT_ID_URI, departmentId);
                }
                CursorLoader employeesListCursorLoader = new CursorLoader(EmployeeListActivity.this,
                        uri, projection, null, null, null);
                return employeesListCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                employeesListAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                employeesListAdapter.swapCursor(null);
            }
        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        this.getListView().setDividerHeight(2);
        fillEmployeeData();
        fillDepartmentData();
        registerForContextMenu(getListView());
    }

    private void fillDepartmentData() {
        String[] from = new String[]{DepartmentTable.NAME_COLUMN};
        departmentSpinnerAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                null,
                from,
                new int[]{android.R.id.text1},
                0);
        departmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) this.findViewById(R.id.depSpinner);
        spinner.setAdapter(departmentSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLoaderManager().restartLoader(0, null, createEmployeeListLoaderListener(id)); //TODO better about of informing URI parameter change?
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        getLoaderManager().initLoader(1, null, departmentSpinnerLoaderListener);
    }

    private void fillEmployeeData() {
        String[] from = new String[]{EmployeeTable.ID_COLUMN, EmployeeTable.LAST_NAME__COLUMN, EmployeeTable.FIRST_NAME__COLUMN, EmployeeTable.AVATAR__COLUMN};
        int[] to = new int[]{R.id.person_avatar, R.id.person_label, R.id.person_content};

        getLoaderManager().initLoader(0, null, employeeListLoaderListener); //TODO 2 listeners one for empDataList second for Department spinner
        employeesListAdapter = new SimpleCursorAdapter(this, R.layout.todo_row, null, from,
                to, 0);
        employeesListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (view.getId()) {
                    case R.id.person_label:
                        ((TextView) view).setText(cursor.getString(1));
                        break;
                    case R.id.person_content:
                        ((TextView) view).setText(cursor.getString(2));
                        break;
                    case R.id.person_avatar:
                        Picasso.with(view.getContext()).load(cursor.getString(3)).into((ImageView) view);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        setListAdapter(employeesListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importJson:
                JsonImporter jsonImporter = new JsonImporter();
                jsonImporter.execute(getContentResolver());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
