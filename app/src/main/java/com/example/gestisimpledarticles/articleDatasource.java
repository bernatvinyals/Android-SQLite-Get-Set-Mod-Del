package com.example.gestisimpledarticles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class articleDatasource {

    public static final String table_TODOLIST = "articlelist";
    public static final String TODOLIST_ID = "_id";
    public static final String TODOLIST_CODIARTICLE = "codiArticle";
    public static final String TODOLIST_DESCRIPCION = "description";
    public static final String TODOLIST_PVP = "pvp";
    public static final String TODOLIST_ESTOC = "estoc";

    private articleHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    // CONSTRUCTOR
    public articleDatasource(Context ctx) {
        // En el constructor directament obro la comunicació amb la base de dades
        dbHelper = new articleHelper(ctx);

        // amés també construeixo dos databases un per llegir i l'altre per alterar
        open();
    }

    // DESTRUCTOR
    protected void finalize () {
        // Cerramos los databases
        dbW.close();
        dbR.close();
    }

    private void open() {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    // ******************
    // Funcions que retornen cursors de todoList
    // ******************
    public Cursor articleList() {
        // Retorem totes les tasques
        return dbR.query(table_TODOLIST, new String[]{TODOLIST_ID,TODOLIST_CODIARTICLE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                null, null,
                null, null, TODOLIST_ID);
    }

    public Cursor GetWithoutDescriptoers() {
        // Retorem totes les tasques
        return dbR.query(table_TODOLIST, new String[]{TODOLIST_ID,TODOLIST_CODIARTICLE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_DESCRIPCION+"=?", new String[]{String.valueOf("")},
                null, null, TODOLIST_ID);
    }

    public Cursor article(long id) {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(table_TODOLIST, new String[]{TODOLIST_ID,TODOLIST_CODIARTICLE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_ID+ "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    public Cursor CheckifExists(String id) {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(table_TODOLIST, new String[]{TODOLIST_ID,TODOLIST_CODIARTICLE,TODOLIST_DESCRIPCION,TODOLIST_PVP,TODOLIST_ESTOC},
                TODOLIST_CODIARTICLE+ "=?", new String[]{String.valueOf(id)},
                null, null, null);
    }


    // ******************
    // Funciones de manipualación de datos
    // ******************

    public long articleAdd(String title, String description, int level) {
        // Creem una nova tasca i retornem el id crear per si el necessiten
        ContentValues values = new ContentValues();
        values.put(TODOLIST_CODIARTICLE, title);
        values.put(TODOLIST_DESCRIPCION, description);
        values.put(TODOLIST_PVP,level);
        values.put(TODOLIST_ESTOC,0);  // Forcem 0 pq si s'està creant la tasca no pot estar finalitzada

        return dbW.insert(table_TODOLIST,null,values);
    }

    public void articleUpdate(long id, String title, String description, int level) {
        // Modifiquem els valors de las tasca amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(TODOLIST_CODIARTICLE, title);
        values.put(TODOLIST_DESCRIPCION, description);
        values.put(TODOLIST_PVP,level);
        values.put(TODOLIST_ESTOC,0);  // Forcem 0 pq si s'està creant la tasca no pot estar finalitzada

        dbW.update(table_TODOLIST,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void articleDelete(long id) {
        // Eliminem la task amb clau primària "id"
        dbW.delete(table_TODOLIST, TODOLIST_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void articlePending(long id) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(TODOLIST_ESTOC,0);

        dbW.update(table_TODOLIST,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void articleCompleted(long id) {
        // Modifiquem al estat de pendent la task indicada
        ContentValues values = new ContentValues();
        values.put(TODOLIST_ESTOC,1);

        dbW.update(table_TODOLIST,values, TODOLIST_ID + " = ?", new String[] { String.valueOf(id) });
    }


}
