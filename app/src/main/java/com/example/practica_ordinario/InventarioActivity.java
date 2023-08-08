package com.example.practica_ordinario;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InventarioActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private EditText etNombre, etPrecio, etCantidad;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        // Inicializar la base de datos
        OrdinarioBDHelper dbHelper = new OrdinarioBDHelper(this);
        database = dbHelper.getWritableDatabase();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Button btnInsertar = findViewById(R.id.btnInsertarProducto);
        Button btnEliminar = findViewById(R.id.btnEliminarProducto);
        Button btnActualizar = findViewById(R.id.btnActualizarProducto);

        etNombre = new EditText(this);
        etPrecio = new EditText(this);
        etCantidad = new EditText(this);

        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoInsertar();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoEliminar();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoActualizar();
            }
        });
    }

    private void mostrarDialogoInsertar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Producto");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_insertar_producto, null);
        builder.setView(dialogView);

        final EditText etNombreDialog = dialogView.findViewById(R.id.etNombreDialog);
        final EditText etPrecioDialog = dialogView.findViewById(R.id.etPrecioDialog);
        final EditText etCantidadDialog = dialogView.findViewById(R.id.etCantidadDialog);

        builder.setPositiveButton("Insertar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = etNombreDialog.getText().toString();
                String precio = etPrecioDialog.getText().toString();
                String cantidad = etCantidadDialog.getText().toString();

                if (!nombre.isEmpty() && !precio.isEmpty() && !cantidad.isEmpty()) {
                    ContentValues values = new ContentValues();
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_NOMBRE, nombre);
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_PRECIO, Double.parseDouble(precio));
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_CANTIDAD, Integer.parseInt(cantidad));
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_FECHA, sdf.format(new Date()));

                    database.insert(OrdinarioBD.ProductoEntry.TABLE_NAME, null, values);

                    // Actualizar la interfaz o mostrar mensaje de éxito
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Producto");

        final EditText etIdEliminar = new EditText(this);
        etIdEliminar.setHint("ID del Producto");
        builder.setView(etIdEliminar);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idEliminar = etIdEliminar.getText().toString();

                if (!idEliminar.isEmpty()) {
                    String selection = OrdinarioBD.ProductoEntry._ID + " = ?";
                    String[] selectionArgs = { idEliminar };
                    database.delete(OrdinarioBD.ProductoEntry.TABLE_NAME, selection, selectionArgs);

                    // Actualizar la interfaz o mostrar mensaje de éxito
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoActualizar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Producto");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_actualizar_producto, null);
        builder.setView(dialogView);

        final EditText etIdActualizar = dialogView.findViewById(R.id.etIdActualizar);
        final EditText etPrecioActualizar = dialogView.findViewById(R.id.etPrecioActualizar);
        final EditText etCantidadActualizar = dialogView.findViewById(R.id.etCantidadActualizar);

        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idActualizar = etIdActualizar.getText().toString();
                String precio = etPrecioActualizar.getText().toString();
                String cantidad = etCantidadActualizar.getText().toString();

                if (!idActualizar.isEmpty() && !precio.isEmpty() && !cantidad.isEmpty()) {
                    ContentValues values = new ContentValues();
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_PRECIO, Double.parseDouble(precio));
                    values.put(OrdinarioBD.ProductoEntry.COLUMN_CANTIDAD, Integer.parseInt(cantidad));

                    String selection = OrdinarioBD.ProductoEntry._ID + " = ?";
                    String[] selectionArgs = { idActualizar };
                    database.update(OrdinarioBD.ProductoEntry.TABLE_NAME, values, selection, selectionArgs);

                    // Actualizar la interfaz o mostrar mensaje de éxito
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

