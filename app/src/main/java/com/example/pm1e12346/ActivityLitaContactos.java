package com.example.pm1e12346;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e12346.Clases.Contactos;
import com.example.pm1e12346.Conexion.SQLiteConexion;
import com.example.pm1e12346.Conexion.Transacciones;

import java.util.ArrayList;

public class ActivityLitaContactos extends AppCompatActivity {


    SQLiteConexion conexion;
    ListView lista;
    ArrayList<Contactos> listaContactos;
    ArrayList <String> ArregloContactos;
    EditText btnBuscarAL;
    Button btnAtras,btnActualizarAL, btnEliminarAL, btnCompartirAL, btnVerImagenAL;
    Intent intent;
    Contactos contacto;


    static final int PETICION_LLAMADA_TELEFONO = 102;


    int previousPosition = 1;
    int count=1;
    long previousMil=0;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lita_contactos);

        lista = (ListView) findViewById(R.id.listaContactosAL);
        intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        obtenerlistaContactos();

        //llenar grip con datos contactos
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,ArregloContactos);
        lista.setAdapter(adp);

        /*BUSCAR CONTACTOS EN LISTA*/
        btnBuscarAL = (EditText) findViewById(R.id.btnBuscarAL);

        btnBuscarAL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                adp.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //--------------------------------------LISTA------------------------------------------

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(previousPosition==i)
                {
                    count++;
                    if(count==2 && System.currentTimeMillis()-previousMil<=1000)
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Acción");
                        alertDialogBuilder
                                .setMessage("¿Desea llamar a "+contacto.getNombre()+"?")
                                .setCancelable(false)
                                .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try{
                                            permisoLlamada();
                                        }catch (Exception ex){
                                            ex.toString();
                                        }

                                        Toast.makeText(getApplicationContext(),"Realizando llamada",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        count=1;
                    }
                }
                else
                {
                    previousPosition=i;
                    count=1;
                    previousMil=System.currentTimeMillis();
                    contacto = listaContactos.get(i);
                    setContactoSeleccionado();
                }
            }


        });

        btnAtras = (Button) findViewById(R.id.btnAtras);
        btnActualizarAL = (Button) findViewById(R.id.btnActualizarAL);
        btnEliminarAL = (Button) findViewById(R.id.btnEliminarAL);
        btnCompartirAL = (Button) findViewById(R.id.btnCompartirAL);
        btnVerImagenAL = (Button) findViewById(R.id.btnVerImagenAL);

        //------------------------------------------BOTONES------------------------------------------

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        btnVerImagenAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(),ActivityVerFoto.class);
                    intent.putExtra("codigoParaFoto", contacto.getId()+"");
                    startActivity(intent);
                }catch (NullPointerException e){
                    Intent intent = new Intent(getApplicationContext(),ActivityVerFoto.class);
                    intent.putExtra("codigoParaFoto", "1");
                    startActivity(intent);
                }

            }
        });

        btnActualizarAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent);
            }
        });

        btnCompartirAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarContacto();
            }
        });

        btnEliminarAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Eliminar Contacto");

                alertDialogBuilder
                        .setMessage("¿Está seguro de eliminar el contacto?")
                        .setCancelable(false)
                        .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                 eliminarContacto();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();

            }
        });
    }

    //-------------------------------------------METODOS-----------------------------------------


    private void permisoLlamada() {
        // valido si el permiso para acceder a la telefono esta otorgado
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            // Otorgamos el permiso para acceder al telefono
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, PETICION_LLAMADA_TELEFONO);
        }else{
            LlamarContacto();
        }
    }

    private void LlamarContacto() {
        String numero = "+"+contacto.getCodPais()+contacto.getTelefono();
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numero));
        startActivity(intent);
    }

    private void eliminarContacto() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        int obtenerCodigo = contacto.getId();

        db.delete(Transacciones.tablaContactos,Transacciones.id +" = "+ obtenerCodigo, null);

        Toast.makeText(getApplicationContext(), "Registro eliminado con exito, Codigo " + obtenerCodigo
                ,Toast.LENGTH_LONG).show();
        db.close();

        //despues de eliminar vuelve a abrir la activida, limpiando los menu anteriores
        Intent intent = new Intent(this, ActivityLitaContactos.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


    }

    private void enviarContacto(){
        String contactoEnviado = "El numero de "+contacto.getNombre().toString()+
                " es +"+contacto.getCodPais()+contacto.getTelefono() ;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contactoEnviado);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }


    private void setContactoSeleccionado() {

        //contacto = listaContactos.get(id);
        //intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);
        intent.putExtra("id", contacto.getId()+"");
        intent.putExtra("nombre", contacto.getNombre());
        intent.putExtra("telefono", contacto.getTelefono()+"");
        intent.putExtra("codPais", contacto.getCodPais()+"");
        intent.putExtra("nota", contacto.getNota());
        //startActivity(intent);
    }



    private void obtenerlistaContactos() {
         SQLiteDatabase db = conexion.getReadableDatabase();

         Contactos list_contact = null;

         listaContactos = new ArrayList<Contactos>();

         Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.tablaContactos, null);

         while (cursor.moveToNext())
        {
            list_contact = new Contactos();
            list_contact.setId(cursor.getInt(0));
            list_contact.setNombre(cursor.getString(1));
            list_contact.setTelefono(cursor.getInt(2));
            list_contact.setNota(cursor.getString(3));
            list_contact.setCodPais(cursor.getInt(5));
            listaContactos.add(list_contact);
        }
        cursor.close();
         llenarlista();

    }

    private void llenarlista()
    {
        ArregloContactos = new ArrayList<String>();

        for (int i=0; i<listaContactos.size();i++)
        {
            ArregloContactos.add(listaContactos.get(i).getNombre()+" | "+
                    listaContactos.get(i).getCodPais()+
                    listaContactos.get(i).getTelefono());

        }
    }

}