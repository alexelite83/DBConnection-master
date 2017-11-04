package com.david.dbconnection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class DBHolydays extends SQLiteOpenHelper {

    private Context context;

    private static final String DBNAME = "HolydaysDB";
    private static final int DAYS_COLUMN_INDEX = 1;
    private final String HOLYDAYS_TABLE_NAME = "Holydays";


    /**
     * Constructor de la clase
     *
     * @param context
     */
    public DBHolydays(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    /**
     * Creará las tablas de la BDD al crear la BDD
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String inst = "CREATE TABLE " + HOLYDAYS_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date TEXT, User TEXT)";
        db.execSQL(inst);

    }

    /**
     * Acciones a realizar al actualizar una BDD
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Método para insertar un día de vacaciones a nombre de un usuario
     *
     * @param date
     * @param user
     */
    public void insertIntoHolydays(String date, String user) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String inst = "INSERT INTO " + HOLYDAYS_TABLE_NAME +
                    " VALUES ( null, '" + date + "', '" + user + "')";
            db.execSQL(inst);
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para recuperar los días de vacaciones guardados para un usuario
     *
     * @param user
     * @return
     */
    public Vector<String> getFromHolydays(String user) {
        Vector<String> result = new Vector<>();
        Cursor cursor = null;

        try {
            // Se selecciona la BDD y se realiza la consulta
            SQLiteDatabase db = getReadableDatabase();
            String inst = "SELECT * FROM " + HOLYDAYS_TABLE_NAME + " WHERE User = '" + user + "' ORDER BY _id";
            cursor = db.rawQuery(inst, null); // Matriz que recibirá el resultado de la consulta

            result.clear();

            // Se copia la columna de los días al vector de Strings
            if (cursor.moveToFirst()) {
                do {
                    String day = cursor.getString(DAYS_COLUMN_INDEX);
                    result.add(day);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            return result;
        }


    }

    /**
     * Método para borrar los días guardados para un usuario
     *
     * @param user
     * @return
     */
    public boolean deleteFromHolydays(String user) {
        boolean deleted = false;

        try {
            // Se selecciona la BDD y se borran los datos del usuario pasado
            SQLiteDatabase db = getWritableDatabase();
            String inst = "DELETE FROM " + HOLYDAYS_TABLE_NAME + " WHERE User = '" + user + "'";

            db.execSQL(inst);
            deleted = true;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            return deleted;
        }
    }

    public boolean deleteDayFromHolydays(String user, String date) {
        boolean deleted = false;

        try {
            // Se selecciona la BDD y se borran los datos del usuario pasado
            SQLiteDatabase db = getWritableDatabase();
            String inst = "DELETE FROM " + HOLYDAYS_TABLE_NAME + " WHERE User = '" + user + "' AND Date = '" + date + "'";

            db.execSQL(inst);
            deleted = true;
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            return deleted;
        }
    }

    public boolean isSelectedDate(String user, String date) {
        boolean deleted = false;

        try {
            // Se selecciona la BDD y se borran los datos del usuario pasado
            SQLiteDatabase db = getWritableDatabase();
            String inst = "SELECT * FROM " + HOLYDAYS_TABLE_NAME + " WHERE User = '" + user + "' AND Date = '" + date + "'";

            Cursor cursor = db.rawQuery(inst, null);
            if(cursor.getCount() > 0) {
                deleted = true;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        } finally {
            return deleted;
        }

    }

}
