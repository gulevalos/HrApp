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
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mardiak.marek.hrapp.JsonImporter;
import com.example.mardiak.marek.hrapp.OrgStructureContentProvider;
import com.example.mardiak.marek.hrapp.R;
import com.example.mardiak.marek.hrapp.database.DepartmentTable;
import com.example.mardiak.marek.hrapp.database.EmployeeTable;
import com.squareup.picasso.Picasso;

/**
 * Created by mm on 3/9/2016.
 */
public class EmployeeListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private SimpleCursorAdapter employeesListAdapter;
    private SimpleCursorAdapter departmentSpinnerAdapter;
    private CursorLoader employeesListCursorLoader;

    private LoaderManager.LoaderCallbacks<Cursor> employeeListLoaderListener
            = createEmployeeListLoaderListener(null);
    private ListView listView;

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
                employeesListCursorLoader = new CursorLoader(EmployeeListActivity.this,
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list);
        listView = (ListView) findViewById(R.id.empList);
        fillEmployeeData();
        fillDepartmentData();
        createDrawerMenu();
    }

    private void createDrawerMenu() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.importJson:
                        Toast.makeText(getApplicationContext(), "importJson", Toast.LENGTH_SHORT).show();
                        JsonImporter jsonImporter = new JsonImporter();
                        jsonImporter.execute(getContentResolver());
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void fillDepartmentData() {

        String[] from = new String[]{DepartmentTable.NAME_COLUMN};
        int[] to = new int[]{android.R.id.text1};
        departmentSpinnerAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_spinner_item,
                null,
                from,
                to,
                0);
        departmentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Spinner spinner = (Spinner) this.findViewById(R.id.depSpinner);
        spinner.setAdapter(departmentSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Object item = parent.getItemAtPosition(position);
                getLoaderManager().restartLoader(0, null, createEmployeeListLoaderListener(id));
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

        getLoaderManager().initLoader(0, null, employeeListLoaderListener);
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

        listView.setAdapter(employeesListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
