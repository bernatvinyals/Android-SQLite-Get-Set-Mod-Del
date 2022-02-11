package com.example.gestisimpledarticles;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class articleAddActivity extends AppCompatActivity {

    articleDatasource db;
    private long idTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_add);
        db = new articleDatasource(this);

// Botones de aceptar y cancelar
        // Boton ok
        Button  btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptarCambios();
            }
        });

        // Boton eliminar
        Button  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });

        // Boton cancelar
        Button  btnCancel = (Button) findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });


        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idTask = this.getIntent().getExtras().getLong("id");

        if (idTask != -1) {
            // Si estem modificant carreguem les dades en pantalla
            cargarDatos();
        }
        else {
            // Si estem creant amaguem el checkbox de finalitzada i el botó d'eliminar
            CheckBox chk = (CheckBox) findViewById(R.id.chkCompleted);
            chk.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }


    private void cargarDatos() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = db.article(idTask);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;

        tv = (TextView) findViewById(R.id.edtTitulo);
        tv.setText(datos.getString(datos.getColumnIndex(articleDatasource.TODOLIST_CODIARTICLE)));
        tv.setEnabled(false);

        tv = (TextView) findViewById(R.id.edtDescripcion);
        tv.setText(datos.getString(datos.getColumnIndex(articleDatasource.TODOLIST_DESCRIPCION)));

        tv = (TextView) findViewById(R.id.edtPVP);
        tv.setText(datos.getString(datos.getColumnIndex(articleDatasource.TODOLIST_PVP)));

        // Finalment el checkbox
        int finalitzada = datos.getInt(datos.getColumnIndex(articleDatasource.TODOLIST_ESTOC));

        CheckBox chk;
        chk = (CheckBox) findViewById(R.id.chkCompleted);
        if (finalitzada == 1) {
            chk.setChecked(true);
        } else {
            chk.setChecked(false);
        }
    }

    private void aceptarCambios() {
        // Validem les dades
        TextView tv;

        // Títol ha d'estar informat
        tv = (TextView) findViewById(R.id.edtTitulo);
        String titol = tv.getText().toString();
        if (titol.trim().equals("")) {
            Toast.makeText(getApplicationContext(),"Ha d'informar el títol",Toast.LENGTH_LONG);
            return;
        }

        // La Prioritat entre 1 i 5
        tv = (TextView) findViewById(R.id.edtPVP);
        int iPrioritat;
        try {
            iPrioritat = Integer.valueOf(tv.getText().toString());
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"El PVP ha de ser un numero",Toast.LENGTH_LONG);
            return;
        }


        tv = (TextView) findViewById(R.id.edtDescripcion);
        String descripcion = tv.getText().toString();

        Cursor checkifExist = db.CheckifExists(titol);
        checkifExist.moveToFirst();

        if (checkifExist.getCount()!=0){
            Toast.makeText(getApplicationContext(),"El codi ja existeix",Toast.LENGTH_LONG);
            return;
        }

        // Mirem si estem creant o estem guardant
        if (idTask == -1) {
            idTask = db.articleAdd(titol, descripcion, iPrioritat);
        }
        else {
            db.articleUpdate(idTask,titol,descripcion,iPrioritat);

            // ara indiquem si la tasca esta finalitzada o no
            CheckBox chk = (CheckBox) findViewById(R.id.chkCompleted);
            if (chk.isChecked()) {
                db.articleCompleted(idTask);
            }
            else {
                db.articlePending(idTask);
            }
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_OK, mIntent);

        finish();
    }

    private void cancelarCambios() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }

    private void deleteTask() {

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Desitja eliminar la tasca?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.articleDelete(idTask);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }
}