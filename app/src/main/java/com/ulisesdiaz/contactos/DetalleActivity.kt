package com.ulisesdiaz.contactos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class DetalleActivity : AppCompatActivity() {

    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        index = intent.getStringExtra("ID")?.toInt()!!

        //mapearDatos()

    }

    fun  mapearDatos(){
        Log.d("VALORINDEX", index.toString())
        val contacto = MainActivity.obtenerContacto(index)

        val txtNombre = findViewById<TextView>(R.id.txtNombre)
        val txtEmpresa = findViewById<TextView>(R.id.txtEmpresa)
        val txtEdad = findViewById<TextView>(R.id.txtEdad)
        val txtPeso = findViewById<TextView>(R.id.txtPeso)
        val txtTelefono = findViewById<TextView>(R.id.txtTelefono)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtDireccion = findViewById<TextView>(R.id.txtDireccion)
        val imgfoto = findViewById<ImageView>(R.id.imgFoto)

        txtNombre.text = contacto.nombre + " " + contacto.apellidos
        txtEmpresa.text = contacto.empresa
        txtEdad.text = contacto.edad.toString() + " años"
        txtPeso.text = contacto.peso.toString() + " Kg"
        txtTelefono.text = contacto.telefono
        txtEmail.text = contacto.email
        txtDireccion.text = contacto.direccion
        imgfoto.setImageResource(contacto.foto)
    }

    /**
     * Inicializa el contenido del meni de opciones de la Actividad, es decir  que infla los elementos
     * que se creron en el archivo menu_detalle.xml para manipularlos mediante codigo Kotli
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Se activa siempre que se selecciona un elemento del menu de opciones
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
               finish()
                return true
            }
            R.id.menu_eliminar_contacto -> {
                MainActivity.eliminarContacto(index)
                finish()
                return true

            }
            R.id.menu_editar_contacto -> {
                val intent = Intent(this, NuevoActivity::class.java)
                intent.putExtra("ID", index.toString())
                startActivity(intent)
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapearDatos()
    }
}