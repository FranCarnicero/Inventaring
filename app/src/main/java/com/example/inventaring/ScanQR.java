package com.example.inventaring;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaring.db.DbInventario;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ScanQR extends AppCompatActivity {

    private TextView resultclave, resultnombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        resultclave = findViewById(R.id.ResultScanClave);
        resultnombre = findViewById(R.id.resultScanNombre);

        Button NuevoRegistro = (Button)findViewById(R.id.NewSCAN);

        NuevoRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanQR.this, ScanQR.class);
                startActivity(i);
            }
        });

        Button Inicio = (Button)findViewById(R.id.Inicio);

        Inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScanQR.this, MainActivity.class);
                startActivity(i);
            }
        });




        Escanear();
    }

    public void Escanear() {
        IntentIntegrator integrador = new IntentIntegrator(ScanQR.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrador.setPrompt("Leyendo");
        integrador.setCameraId(0);
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();
    }

    /**
     *Una vez escaneado el QR se separan sus datos para poder almacenarlos en la base de datos
     */
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestcode, resultcode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Lectura cancelada", Toast.LENGTH_LONG).show();
            } else {
                String qrData = result.getContents();

                // Separar los datos utilizando el carácter |
                String[] dataParts = qrData.split("\\|");

                // Verificar si se obtuvieron los tres datos esperados
                if (dataParts.length == 3) {
                    String Clave = dataParts[0];
                    String Producto = dataParts[1];
                    String Cantidad = dataParts[2];

                    // Mostrar los datos en los TextView correspondientes
                    resultclave.setText(Clave);
                    resultnombre.setText(Producto);

                    // Llamar al método manipulacion()
                    insertar(Clave, Producto, Cantidad);
                } else {
                    Toast.makeText(this, "Formato de código QR incorrecto", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestcode, resultcode, data);
        }
    }



    public void insertar(String Clave, String Producto, String Cantidad) {

        DbInventario dbInventario = new DbInventario(ScanQR.this);

        long id = dbInventario.InsertarDatos(Clave, Producto, Integer.parseInt(Cantidad));

        if(id> 0){
            Toast.makeText(ScanQR.this, "REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(ScanQR.this, "ERROR AL GUARDAR EL REGISTRO", Toast.LENGTH_LONG).show();

        }

    }


}
