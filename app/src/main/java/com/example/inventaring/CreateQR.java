package com.example.inventaring;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateQR extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;
    private ImageView qrImageView;
    private Button generarQR, descargarQR;
    private EditText cod, name;
    int cantidad = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);

        qrImageView = findViewById(R.id.imageView);
        cod = (EditText)findViewById(R.id.cod);
        name = (EditText)findViewById(R.id.name);
        generarQR=(Button)findViewById(R.id.btnGenera);

        generarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerarQR();
            }
        });

        descargarQR=(Button)findViewById(R.id.Descargar_QR);

        descargarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solicitarPermisoDescarga();
            }
        });
    }

    /**
     * Se crea el QR con las caracteristicas necesarias para poder manipularlo con la BBDD
     */
    public void GenerarQR(){
        if (cod.getText().toString().isEmpty() && name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Falta el código y el nombre del producto", Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Falta el nombre del producto", Toast.LENGTH_LONG).show();
        } else if (cod.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Falta el Código del producto", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
                Bitmap bitmap=barcodeEncoder.encodeBitmap(cod.getText().toString()+"|"+name.getText().toString()+"|"+cantidad, BarcodeFormat.QR_CODE, 750, 750);
                qrImageView.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * Se solicita un permiso de descarga para el QR
     */
    public void solicitarPermisoDescarga(){
    // Verificar si se ha concedido el permiso de escritura en tiempo de ejecución
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        // No se ha concedido el permiso, solicitarlo al usuario
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    } else {
        // El permiso ya se ha concedido, guardar la imagen
        saveImage();
    }
}

    /**
     * Se guarda en el almacenamiento del dispositivo el QR para luego poder utilizarlo en etiquetas en la vida real para escanearlos
     */
    public void saveImage() {
        // Obtener el bitmap del ImageView
        Bitmap bitmap = ((BitmapDrawable) qrImageView.getDrawable()).getBitmap();

        // Obtener la ruta de la carpeta pública de imágenes
        String folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();

        // Generar un nombre único para el archivo de imagen
        String fileName = "QRImage_" + System.currentTimeMillis() + ".png";

        // Crear el objeto ContentValues para almacenar los detalles de la imagen
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // Agregar la imagen al MediaStore y obtener la URI
        ContentResolver resolver = getContentResolver();
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            // Obtener un OutputStream para el URI de la imagen
            OutputStream outputStream = resolver.openOutputStream(imageUri);

            // Comprimir y guardar el bitmap en el OutputStream como formato PNG
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            // Cerrar el OutputStream
            outputStream.close();

            // Mostrar un mensaje de éxito
            Toast.makeText(getApplicationContext(), "Imagen guardada en la galería", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Mostrar un mensaje de error en caso de fallo
            Toast.makeText(getApplicationContext(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }

    }

}