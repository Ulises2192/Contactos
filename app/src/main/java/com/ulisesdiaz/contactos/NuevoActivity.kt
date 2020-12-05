package com.ulisesdiaz.contactos

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.ulisesdiaz.contactos.models.Contacto

class NuevoActivity : AppCompatActivity() {

    var imgFoto: ImageView? = null
    var fotoIndex:Int = 0;
    val fotos =  arrayOf(R.drawable.foto_01, R.drawable.foto_02, R.drawable.foto_03, R.drawable.foto_04,
        R.drawable.foto_05, R.drawable.foto_06)
    var index:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo)
        imgFoto = findViewById(R.id.imgFoto)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        imgFoto?.setOnClickListener {
            seleccionarfoto()
        }

        /**
         * Reconcer accion de nuevo vs editar
         */
        if (intent.hasExtra("ID")){
            index = intent.getStringExtra("ID")?.toInt()!!
            rellenarDatos(index)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nuevo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_crear_nuevo ->{
                // Se creara un nuevo elemento de tipo contacto
                val nombre = findViewById<EditText>(R.id.txtNombre)
                val apellidos = findViewById<EditText>(R.id.txtApellido)
                val empresa = findViewById<EditText>(R.id.txtEmpresa)
                val edad = findViewById<EditText>(R.id.txtEdad)
                val peso = findViewById<EditText>(R.id.txtPeso)
                val direccion = findViewById<EditText>(R.id.txtDireccion)
                val telefono = findViewById<EditText>(R.id.txtTelefono)
                val email = findViewById<EditText>(R.id.txtEmail)

                // Validar Campos
                var campos = ArrayList<String>()
                campos.add(nombre.text.toString())
                campos.add(apellidos.text.toString())
                campos.add(empresa.text.toString())
                campos.add(edad.text.toString().replace("años", "").trim())
                campos.add(peso.text.toString().replace("Kg", "").trim())
                campos.add(direccion.text.toString())
                campos.add(telefono.text.toString())
                campos.add(email.text.toString())

                var flag = 0
                for (campo in campos){
                    if (campo.isNullOrEmpty()){
                        flag ++
                    }
                }

                if (flag > 0){
                    Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }else{
                    /**
                     * El sigueinte fragmento inserta la informacion que viene directamente del componente de laVista
                     */
                    /*MainActivity.agregarContacto(Contacto(nombre.text.toString(), apellidos.text.toString(),
                        empresa.text.toString(), edad.text.toString().toInt(), peso.text.toString().toFloat(),
                        direccion.text.toString(), telefono.text.toString(), email.text.toString(), R.drawable.foto_02))*/

                    /**
                     * El Siguiente fragmento inserta la informacion que viene del Arraylist campos
                     * que se utilizo para las validaciones
                     */
                    if (index > -1){
                        MainActivity.actualizarContacto(index, Contacto(campos.get(0), campos.get(1),
                            campos.get(2), campos.get(3).toInt(), campos.get(4).toFloat(),
                            campos.get(5), campos.get(6), campos.get(7), obtenerFoto(fotoIndex)) )
                    }else{
                        MainActivity.agregarContacto(Contacto(campos.get(0), campos.get(1),
                            campos.get(2), campos.get(3).toInt(), campos.get(4).toFloat(),
                            campos.get(5), campos.get(6), campos.get(7), obtenerFoto(fotoIndex)))

                    }
                    finish()
                    Log.d("NO ELEMENTOS", MainActivity.contactos?.count().toString())
                }
                return true
            }
            else->{
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun seleccionarfoto(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona imagen de perfil")

        val  adaptadorDialogo = ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item)
        adaptadorDialogo.add("Foto 01")
        adaptadorDialogo.add("Foto 02")
        adaptadorDialogo.add("Foto 03")
        adaptadorDialogo.add("Foto 04")
        adaptadorDialogo.add("Foto 05")
        adaptadorDialogo.add("Foto 06")

        builder.setAdapter(adaptadorDialogo){
            dialog, which ->
            fotoIndex = which
            imgFoto?.setImageResource(obtenerFoto(fotoIndex))
        }

        builder.setNegativeButton("Cancelar"){
            dialog, wich ->
            dialog.dismiss()
        }
        builder.show()
    }

    fun obtenerFoto(index:Int): Int{
        return fotos.get(index)
    }

    fun rellenarDatos(index:Int){
        val contacto = MainActivity.obtenerContacto(index)

        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtApellidos = findViewById<EditText>(R.id.txtApellido)
        val txtEmpresa = findViewById<EditText>(R.id.txtEmpresa)
        val txtEdad = findViewById<EditText>(R.id.txtEdad)
        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val txtTelefono = findViewById<EditText>(R.id.txtTelefono)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtDireccion = findViewById<EditText>(R.id.txtDireccion)
        val imgfoto = findViewById<ImageView>(R.id.imgFoto)

        txtNombre.setText(contacto.nombre, TextView.BufferType.EDITABLE)
        txtApellidos.setText(contacto.apellidos, TextView.BufferType.EDITABLE)
        txtEmpresa.setText(contacto.empresa, TextView.BufferType.EDITABLE)
        txtEdad.setText(contacto.edad.toString() + " años", TextView.BufferType.EDITABLE)
        txtPeso.setText(contacto.peso.toString() + " Kg", TextView.BufferType.EDITABLE)
        txtTelefono.setText(contacto.telefono, TextView.BufferType.EDITABLE)
        txtEmail.setText(contacto.email, TextView.BufferType.EDITABLE)
        txtDireccion.setText(contacto.direccion, TextView.BufferType.EDITABLE)
        imgfoto.setImageResource(contacto.foto)

        var posicion = 0

        for (foto in fotos){
            if (contacto.foto == foto){
                fotoIndex = posicion
            }
            posicion++
        }
    }
}