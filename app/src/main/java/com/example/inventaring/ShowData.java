package com.example.inventaring;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaring.adaptador.ListaAdapter;
import com.example.inventaring.db.DbInventario;
import com.example.inventaring.entidades.Productos;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowData extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_STORAGE = 1;
    RecyclerView listaProductos;
    ArrayList<Productos> listaArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        listaProductos=findViewById(R.id.listaDatos);

        listaProductos.setLayoutManager(new LinearLayoutManager(this));


        DbInventario dbInventario = new DbInventario(ShowData.this);



        listaArray = dbInventario.mostrarDatos();

        ListaAdapter adapter = new ListaAdapter(listaArray);

        listaProductos.setAdapter(adapter);


        setSupportActionBar(findViewById(R.id.toolbar)); // Establecer la barra de herramientas como barra de acción

    }



    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.descargajson, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){

        int itemId = item.getItemId();
        if (itemId == R.id.DescargaJson) {
            descargarJSON();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Descarga el JSON de la listaArray y lo guarda en el almacenamiento.
     * Si no se tiene el permiso WRITE_EXTERNAL_STORAGE, se solicita al usuario.
     * Una vez que se obtiene el permiso, se guarda el JSON en el almacenamiento.
     */
    private void descargarJSON() {
        // Convertir listaArray a formato JSON
        String json = new Gson().toJson(listaArray);

        // Verificar si se tiene permiso WRITE_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permiso WRITE_EXTERNAL_STORAGE si no se tiene
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_STORAGE);
        } else {
            // Si se tiene permiso WRITE_EXTERNAL_STORAGE, guardar el JSON en el almacenamiento
            guardarJsonEnAlmacenamiento(json);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso WRITE_EXTERNAL_STORAGE concedido, se puede realizar la acción deseada
                // Por ejemplo, guardar el JSON en el almacenamiento
                String json = new Gson().toJson(listaArray);
                guardarJsonEnAlmacenamiento(json);
            } else {
                // Permiso WRITE_EXTERNAL_STORAGE denegado, se puede mostrar un mensaje de error o realizar alguna otra acción alternativa
                Toast.makeText(this, "Permiso denegado para escribir en el almacenamiento", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Guarda el JSON en el almacenamiento externo.
     *
     * @param json El JSON a guardar.
     */
    private void guardarJsonEnAlmacenamiento(String json) {
        try {
            // Crear un nombre de archivo único usando la fecha y hora actual
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "data_" + timeStamp + ".json";

            // Verificar si el almacenamiento externo está disponible y se puede escribir
            if (isExternalStorageWritable()) {
                // Obtener la ruta del directorio de descargas
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                // Crear el archivo en el directorio de descargas
                File file = new File(downloadsDir, fileName);

                // Escribir el contenido JSON en el archivo
                FileWriter writer = new FileWriter(file);
                writer.write(json);
                writer.flush();
                writer.close();

                Toast.makeText(this, "JSON guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "El almacenamiento externo no está disponible para escritura", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el JSON", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Verifica si el almacenamiento externo está disponible y se puede escribir.
     *
     * @return true si el almacenamiento externo es escribible, false de lo contrario.
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}