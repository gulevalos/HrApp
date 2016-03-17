package com.example.mardiak.marek.hrapp;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.example.mardiak.marek.hrapp.database.DepartmentTable;
import com.example.mardiak.marek.hrapp.database.EmployeeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mm on 3/13/2016.
 */
public class JsonImporter extends AsyncTask<ContentResolver, Void, Void> {

    @Override
    protected Void doInBackground(ContentResolver... contentResolver) { //TODO thread safety of contentResolver??
        try {
            JSONObject jsonRootObject = new JSONObject(OrgStructureContentProvider.strJson);
            JSONArray jsonDepartmentsArray = jsonRootObject.optJSONArray("Departments");

            ArrayList<ContentProviderOperation> ops =
                    new ArrayList<ContentProviderOperation>();
            int departmenetScheduledOperationIndex = 0;
            for (int i = 0; i < jsonDepartmentsArray.length(); i++) {
                JSONObject jsonDepartment = jsonDepartmentsArray.getJSONObject(i);
                ops.add(
                        ContentProviderOperation.newInsert(OrgStructureContentProvider.INSERT_DEPARTMENT_URI)
                                .withValue(DepartmentTable.NAME_COLUMN, jsonDepartment.optString("Name"))
                                .build());
                JSONArray jsonEmployeesArray = jsonDepartment.optJSONArray("employees");
                for (int j = 0; j < jsonEmployeesArray.length(); j++) {
                    JSONObject jsonEmployee = jsonEmployeesArray.getJSONObject(j);
                    ops.add(
                            ContentProviderOperation.newInsert(OrgStructureContentProvider.INSERT_EMPLOYEE_URI)
                                    .withValueBackReference(EmployeeTable.FK_DEPARTMENTS_COLUMN, departmenetScheduledOperationIndex)
                                    .withValue(EmployeeTable.AVATAR__COLUMN, jsonEmployee.optString("avatar"))
                                    .withValue(EmployeeTable.FIRST_NAME__COLUMN, jsonEmployee.optString("firstName"))
                                    .withValue(EmployeeTable.LAST_NAME__COLUMN, jsonEmployee.optString("lastName"))
                                    .withYieldAllowed(true)
                                    .build());
                }
                departmenetScheduledOperationIndex = departmenetScheduledOperationIndex + jsonEmployeesArray.length() + 1;

            }

            try {
                contentResolver[0].applyBatch(OrgStructureContentProvider.AUTHORITY, ops);
            } catch (RemoteException e) {
                Log.e(JsonImporter.class.getName(), e.getMessage(),e);
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                Log.e(JsonImporter.class.getName(), e.getMessage(),e);
                e.printStackTrace();
            }
        } catch (JSONException e) {
            Log.e(JsonImporter.class.getName(), e.getMessage(),e);
            e.printStackTrace();
        }
        contentResolver[0].notifyChange(OrgStructureContentProvider.DEPARTMENTS_ALL_URI, null);
        contentResolver[0].notifyChange(OrgStructureContentProvider.EMPLOYEES_BY_DEPARTMENT_ID_URI, null);
        contentResolver[0].notifyChange(OrgStructureContentProvider.EMPLOYEES_ALL_URI, null);
        return null; //TODO inform main list activity about result
    }
}
