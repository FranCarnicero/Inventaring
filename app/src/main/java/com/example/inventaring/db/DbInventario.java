package com.example.inventaring.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.inventaring.MainActivity;
import com.example.inventaring.entidades.Productos;

import java.util.ArrayList;

public class DbInventario extends DBhelper{

    Context context;

    public DbInventario(@Nullable Context context) {
        super(context);
        this.context=context;
    }

    /**
     * Inserta un nuevo registro de producto en la base de datos o actualiza la cantidad si el producto ya existe.
     *
     * @param Clave     La clave del producto.
     * @param Producto  El nombre del producto.
     * @param Cantidad  La cantidad del producto a insertar o sumar.
     * @return El ID del nuevo registro insertado o el número de filas actualizadas.
     */

    public long InsertarDatos(String Clave, String Producto, int Cantidad) {
        long id = 0;

        try {
            DBhelper dBhelper = new DBhelper(context);
            SQLiteDatabase db = dBhelper.getWritableDatabase();


            if(db!=null){
                Toast.makeText(context,"BASE DE DATOS CREADA CON ÉXITO", Toast.LENGTH_LONG).show();


                // Verificar si el producto ya existe en la base de datos
                String[] whereArgs = {Clave};
                Cursor cursor = db.query("Inventario", null, "Clave=?", whereArgs, null, null, null);

                if (cursor.getCount() > 0) {
                    // El producto ya existe, actualizar la cantidad sumando 1
                    cursor.moveToFirst();
                    int cantidadActual = cursor.getInt(cursor.getColumnIndex("CANTIDAD"));
                    int nuevaCantidad = cantidadActual + 1;

                    ContentValues updateValues = new ContentValues();
                    updateValues.put("CANTIDAD", nuevaCantidad);

                    id = db.update("Inventario", updateValues, "Clave=?", whereArgs);
                    Toast.makeText(context, "Registro actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    // El producto no existe, realizar una inserción
                    ContentValues insertValues = new ContentValues();
                    insertValues.put("Clave", Clave);
                    insertValues.put("PRODUCTO", Producto);
                    insertValues.put("CANTIDAD", Cantidad);

                    id = db.insertWithOnConflict("Inventario", null, insertValues, SQLiteDatabase.CONFLICT_IGNORE);
                    Toast.makeText(context, "Registro añadido", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            }
            else{
                Toast.makeText(context,"ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }


    /**
     * Recupera y devuelve una lista de productos almacenados en la base de datos.
     *
     * @return La lista de productos almacenados en la base de datos.
     */
    public ArrayList<Productos> mostrarDatos(){


        // Crear una instancia de DBhelper para interactuar con la base de datos
        DBhelper dBhelper = new DBhelper(context);
        SQLiteDatabase db = dBhelper.getWritableDatabase();


        // Crear una lista para almacenar los productos
        ArrayList<Productos> listaProductos = new ArrayList<>();
        Productos producto = null;

        Cursor cursorProductos=null;

        // Consultar todos los registros de la tabla "Inventario"
        cursorProductos = db.rawQuery("SELECT * FROM Inventario", null);


        if(cursorProductos.moveToFirst()){

            // Recorrer el cursor para obtener los datos de cada producto

            do{

                // Crear un objeto Producto para almacenar los datos
                producto = new Productos();

                // Obtener los datos del cursor y asignarlos al objeto Producto
                producto.setId(cursorProductos.getInt(0));
                producto.setClave(cursorProductos.getString(1));
                producto.setPRODUCTO(cursorProductos.getString(2));
                producto.setCANTIDAD(cursorProductos.getString(3));


                // Agregar el producto a la lista de productos
                listaProductos.add(producto);

            }while (cursorProductos.moveToNext());
        }

        // Cerrar el cursor después de su uso
        cursorProductos.close();

        // Devolver la lista de productos
        return listaProductos;

    }


}
