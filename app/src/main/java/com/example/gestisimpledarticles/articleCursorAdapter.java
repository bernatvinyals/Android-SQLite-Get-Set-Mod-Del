package com.example.gestisimpledarticles;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class articleCursorAdapter extends android.widget.SimpleCursorAdapter {
    private static final String colorTaskPending = "#d78290";
    private static final String colorTaskCompleted = "#d7d7d7";


    private  articleSimpleActivity oTodoListIcon;

    public articleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        oTodoListIcon = (articleSimpleActivity) context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        Cursor linia = (Cursor) getItem(position);
        int done = linia.getInt(
                linia.getColumnIndexOrThrow(articleDatasource.TODOLIST_ESTOC)
        );

        if (done==1) {
            view.setBackgroundColor(Color.parseColor(colorTaskCompleted));
        }
        else {
            view.setBackgroundColor(Color.parseColor(colorTaskPending));
        }
// Capturem botons
        ImageButton btnMensage = (ImageButton) view.findViewById(R.id.delButton);

        btnMensage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oTodoListIcon.deleteArticle(linia.getInt(linia.getColumnIndexOrThrow(articleDatasource.TODOLIST_ID)));
            }
        });
        LinearLayout ly =  view.findViewById(R.id.Info);
        ly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oTodoListIcon.updateArticle(linia.getInt(linia.getColumnIndexOrThrow(articleDatasource.TODOLIST_ID)));
            }
        });
        return view;
    }
}
