package com.ulisesdiaz.contactos

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.ulisesdiaz.contactos.adapters.AdaptadorCustom
import com.ulisesdiaz.contactos.adapters.AdaptadorCustomGrid
import com.ulisesdiaz.contactos.models.Contacto

class MainActivity : AppCompatActivity() {

    var listaContactos: ListView? = null
    var gridContactos: GridView? = null
    var viewSwitcher: ViewSwitcher? = null

    companion object{
        var contactos: ArrayList<Contacto>? = null
        var adaptador: AdaptadorCustom? = null
        var adaptadorGrid: AdaptadorCustomGrid? = null

        // Se encapsula los objetos para no acceder directamente desde otras clases
        fun agregarContacto(contacto:Contacto){
            adaptador?.addItem(contacto)
        }

        fun obtenerContacto(index:Int): Contacto{
            return adaptador?.getItem(index) as Contacto
        }

        fun eliminarContacto(index:Int){
            Log.d("Items", index.toString())
            adaptador?.removItem(index)
        }

        fun actualizarContacto(index:Int, nuevoContacto:Contacto){
            adaptador?.updateItem(index, nuevoContacto)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listaContactos = findViewById<ListView>(R.id.lista_contactos)
        gridContactos = findViewById<GridView>(R.id.grid_contactos)
        viewSwitcher = findViewById(R.id.viewSwitcher)
        val  toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        contactos = ArrayList()
        contactos?.add(Contacto("Ulises","Diaz","Empresa 1", 28,
            90.5F, "Xalapa Veracruz", "2288523933", "ivan_ulises_2@hotmail.com",
                    R.drawable.foto_01))
        contactos?.add(Contacto("Uriel","Diaz","Empresa 2", 24,
            80.0F, "Xalapa Veracruz", "1234567890", "uriel_2@hotmail.com",
            R.drawable.foto_02))
        contactos?.add(Contacto("Miguel","Platas","Empresa 3", 38,
            98.5F, "Xalapa Veracruz", "1111111111", "miguel@hotmail.com",
            R.drawable.foto_03))
        contactos?.add(Contacto("Cristina","Lpez","Empresa 4", 49,
            77.5F, "Xalapa Veracruz", "2223334456", "cristinahotmail.com",
            R.drawable.foto_04))
        contactos?.add(Contacto("Hannia","Diaz","Empresa 5", 19,
            70.0F, "Xalapa Veracruz", "0987654321", "hannia@hotmail.com",
            R.drawable.foto_05))
        contactos?.add(Contacto("Claudia","Platas","Empresa 6", 33,
            83.5F, "Xalapa Veracruz", "000000000", "claudia@hotmail.com",
            R.drawable.foto_06))

        adaptador = AdaptadorCustom(this, contactos!!)
        listaContactos?.adapter = adaptador

        // Se pasa a la actividad detalle para obtener la informacion por medio de la poscion
        listaContactos?.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, DetalleActivity::class.java)
            intent.putExtra("ID", position.toString())
            startActivity(intent)
        }

        // Se asigna la lista de contactos en el adaptador del Griview
        adaptadorGrid = AdaptadorCustomGrid(this, contactos!!)
        gridContactos?.adapter = adaptadorGrid


    }

    /**
     * Inicializa el contenido del meni de opciones de la Actividad, es decir  que infla los elementos
     * que se creron en el archivo menu_main.xml para manipularlos mediante codigo Kotli
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val itemSwitch = menu?.findItem(R.id.menu_switch)
        itemSwitch?.setActionView(R.layout.switch_item)
        val switchView = itemSwitch?.actionView?.findViewById<Switch>(R.id.sCambiaVista)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val itemBusqueda = menu?.findItem(R.id.menu_search)
        val searchView = itemBusqueda?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Buscar contacto..."

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            // Preparar los datos
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtrar
                adaptador?.filtrar(newText!!)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filtrar
                return true
            }

        })

        // Permite cambiar al GridView que contiene el ViewSwitcher
        switchView?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewSwitcher?.showNext()
        }

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * Se activa siempre que se selecciona un elemento del menu de opciones
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId){
            R.id.menu_nuevo -> {
                val intent = Intent(this, NuevoActivity::class.java)
                startActivity(intent)
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }

    /**
     * Al pasar al detalle y editar o agregar un nuevo contacto, se tiene que actualizaar el adaptador
     * con los nuevos cambios
     */
    override fun onResume() {
        super.onResume()
        adaptador?.notifyDataSetChanged()
    }


}