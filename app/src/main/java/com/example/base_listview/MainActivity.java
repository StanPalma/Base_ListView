package com.example.base_listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText agregaAlista;
    ListView lvLista;
    ArrayList<String> array_list;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        lvLista = (ListView) findViewById(R.id.lvLista);
        agregaAlista = (EditText) findViewById(R.id.edtAddProd);
        
        mostrar_lista();
    }

    // Despues de crear el .xml mi_menu
    // Creamos el metodo override onCreateOptionsMenu y onOptionsItemSelected
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Mostrar MENU en activity_main.xml
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId(); // Obtiene id del item seleccionado el List_View
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog;

        switch (id) {
            case R.id.borrarLista:
                builder.setTitle("Borrar lista");
                builder.setMessage("¿Esta seguro que desea eiminar definitivamente la lista?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrar();
                    }
                });
                builder.setNegativeButton("NO", null);
                dialog = builder.create();
                dialog.show();
                break;
            case R.id.about:
                builder.setTitle("Acerca de");
                builder.setMessage("Hecho por: Bladimir Stanley Palma Portillo");
                builder.setPositiveButton("Aceptar", null);
                dialog = builder.create();
                dialog.show();
                break;

            default:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    // Variable para guardar ID
    ArrayList<String> listaID = new ArrayList<>();
    private void mostrar_lista() {
        listaID.clear(); // Limpia los ID del Array
        Base obj = new Base(this,"Productos",null,1);
        SQLiteDatabase objDB = obj.getWritableDatabase();
        array_list = new ArrayList<String>();
        Cursor cursor = objDB.rawQuery("SELECT * FROM Compras",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            array_list.add(cursor.getString(cursor.getColumnIndex("id"))
                    + " -" + (cursor.getString(cursor.getColumnIndex("nombre"))));
            listaID.add(cursor.getString(cursor.getColumnIndex("id")));
            cursor.moveToNext();
        }
        adaptador = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, array_list);
        lvLista.setAdapter(adaptador);

        // CODIGO PARA CUANDO HACEMOS CLICK EN ITEM, PODER MODIFICARLO O ELIMINARLO

        lvLista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(getApplicationContext())
            }
        });

        objDB.close();
    }

    // Metodo para agregar valores al listview
    public void agregar(View view) {
        // Creamos la conexion a la BD
        Base obj = new Base(this,"Productos", null, 1 );
        SQLiteDatabase objDB = obj.getWritableDatabase();

        String nuevoProducto = agregaAlista.getText().toString();
        String sintaxisSql = "INSERT INTO Compras(id, nombre) VALUES(" + null +", '" + nuevoProducto + "')";

        if (nuevoProducto.equals("")) {
            agregaAlista.setError("Debe agregar un producto");
        }
        else {
            objDB.execSQL(sintaxisSql);
            Toast.makeText(this, "Se ha agregado un producto a la lista de compras", Toast.LENGTH_SHORT).show();

            agregaAlista.setText("");
            agregaAlista.requestFocus();
            mostrar_lista();
        }
        objDB.close();
    }

    // Despues de crear metodo agregar
//    Crear metodo borrar para quitar registros de la BD
    public void borrar() {
        // Creamos la conexion a la BD
        Base obj = new Base(this,"Productos", null, 1 );
        SQLiteDatabase objDB = obj.getWritableDatabase();
        Cursor cursor = objDB.rawQuery("SELECT * FROM Compras", null);

        if (cursor.moveToNext()) {
            objDB.delete("Compras", null, null);
            array_list.clear();
            /*Notifica que los datos se han modificado, cualquier Vista que refleje el conjunto
            * de datos debe actualizarse.*/
            adaptador.notifyDataSetChanged();
            objDB.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'Compras'");
            /*Se usa SQLITE_SEQUENCE porque la columna id es autoincrementable y hace que el
            * id se reinicie a 0*/
            Toast.makeText(this, "Se han eliminado los registros", Toast.LENGTH_SHORT).show();
            objDB.close();
        }

    }
}