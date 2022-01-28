package com.example.gestisimpledarticles;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class articleSimpleActivity extends ListActivity {


    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;

    private articleDatasource bd;
    private long idActual;
    private int positionActual;
    private SimpleCursorAdapter scTasks;


    private static String[] from = new String[]{
            articleDatasource.TODOLIST_CODIARTICLE,
            articleDatasource.TODOLIST_DESCRIPCION,
            articleDatasource.TODOLIST_PVP,
            articleDatasource.TODOLIST_ESTOC};

    private static int[] to = new int[]{
            R.id.lblcodiArticle,
            R.id.lbldescription,
            R.id.lblpvp,
            R.id.lblestoc};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_simple);
        setTitle("Articles List");

        // Butó d'afegir
        Button btn = (Button) findViewById(R.id.btnAdd);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addArticle();
            }
        });

        bd = new articleDatasource(this);
        loadArticle();
    }

    private void loadArticle() {

        // Demanem totes les tasques
        Cursor cursorTasks = bd.articleList();

        // Now create a simple cursor adapter and set it to display
        scTasks = new SimpleCursorAdapter(this,
                R.layout.row_todolistsimple,
                cursorTasks,
                from,
                to,
                1);

        setListAdapter(scTasks);
    }

    private void refreshArticle() {

        // Demanem totes les tasques
        Cursor cursorTasks = bd.articleList();

        // Now create a simple cursor adapter and set it to display
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }


    private void addArticle() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;
///////Add task
        Intent i = new Intent(this, articleSimpleActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }

    private void updateArticle(long id) {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, articleSimpleActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_TASK_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refreshArticle();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE) {
            if (resultCode == RESULT_OK) {
                refreshArticle();
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // modifiquem el id
        updateArticle(id);
    }
}