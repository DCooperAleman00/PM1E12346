package com.example.pm1e12346;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1e12346.Conexion.SQLiteConexion;
import com.example.pm1e12346.Conexion.Transacciones;

public class ActivityAgregarPais extends AppCompatActivity {


    EditText txtPaisAP,txtCodPaisAC;
    Button btnGuardarPaisAP, btnSalirAP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pais);

        txtPaisAP = (EditText) findViewById(R.id.txtPaisAP);
        txtCodPaisAC = (EditText) findViewById(R.id.txtCodPaisAP);
        btnGuardarPaisAP = (Button) findViewById(R.id.btnGuardarPaisAP);
        btnSalirAP = (Button) findViewById(R.id.btnSalirAP);

        btnGuardarPaisAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardarPais();
            }
        });

        btnSalirAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GuardarPais() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.codigo,txtCodPaisAC.getText().toString());
        valores.put(Transacciones.p_pais,txtPaisAP.getText().toString());

        Long resultado = db.insert(Transacciones.tblPaises,Transacciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registro Exitoso!!!"+resultado.toString(),Toast.LENGTH_LONG).show();
        db.close();

        limpiarCampos();

    }

    private void limpiarCampos() {
        txtPaisAP.setText("");
        txtCodPaisAC.setText("");
    }
}