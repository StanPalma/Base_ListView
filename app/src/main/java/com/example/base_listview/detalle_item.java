package com.example.base_listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class detalle_item extends AppCompatActivity {

    EditText edtNombre;
    int id;
    // Intent a MainActivity
    Intent regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_item);

        edtNombre = (EditText) findViewById(R.id.edtNombre);
        // Recibo ID de MainActivity
        Bundle datosRecibidos = getIntent().getExtras();
        int idItem = idItem = datosRecibidos.getInt("id");
        // Lo paso por metodo setID
        setID(idItem);

        // BASE DE DATOS
        Base obj = new Base(this,"Productos",null,1);
        SQLiteDatabase objDB = obj.getWritableDatabase();
        String consulta = "SELECT id, nombre FROM Compras WHERE id = " + String.valueOf(idItem);
        Cursor cursor = objDB.rawQuery(consulta,null);


        if (cursor.moveToFirst()) {
            String nombre = cursor.getString(1).toString();
            edtNombre.setText(nombre);
        } else {
            Toast.makeText(this, "No existe registro", Toast.LENGTH_SHORT).show();
        }
    }

    private void setID(int id) {
        this.id = id;
    }

    public void modificar(View view) {
        // BASE DE DATOS
        Base obj = new Base(this,"Productos",null,1);
        SQLiteDatabase objDB = obj.getWritableDatabase();
        String consulta = "UPDATE Compras set  nombre = '" + edtNombre.getText().toString() + "' WHERE id = " + String.valueOf(id);
        objDB.execSQL(consulta);
        objDB.close();
        regresar = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(regresar);
        finish();
    }

    public void eliminar(View view) {
        // BASE DE DATOS
        Base obj = new Base(this,"Productos",null,1);
        SQLiteDatabase objDB = obj.getWritableDatabase();
        String consultaEliminar = "Delete FROM Compras WHERE id = " + String.valueOf(id);
        objDB.execSQL(consultaEliminar);
        regresar = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(regresar);
        finish();
    }
}