package com.ulisesdiaz.contactos.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ulisesdiaz.contactos.MainActivity
import com.ulisesdiaz.contactos.R
import com.ulisesdiaz.contactos.models.Contacto

class AdaptadorCustom(var contexto:Context, items:ArrayList<Contacto>):BaseAdapter() {

    var items:ArrayList<Contacto>? = null
    var copiaItems:ArrayList<Contacto>? = null

    init {
        this.items = ArrayList(items)
        this.copiaItems = items
    }
    override fun getCount(): Int {
        return items?.count()!!
    }

    override fun getItem(position: Int): Any {
        return this.items?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var viewHolder:ViewHolder? = null
        var view:View? = convertView

        if (view == null){
            view = LayoutInflater.from(contexto).inflate(R.layout.template_contacto, null)
            viewHolder = ViewHolder(view!!)
            view.tag = viewHolder
        }else{
            viewHolder = view.tag as? ViewHolder
        }

        val item = getItem(position) as Contacto
        viewHolder?.nombre?.text = item.nombre + " " + item?.apellidos
        viewHolder?.empresa?.text = item.empresa
        viewHolder?.foto?.setImageResource(item.foto)

        return  view!!
    }

    private class ViewHolder(view: View){
        var nombre:TextView? = null
        var empresa:TextView? = null
        var foto:ImageView? = null

        init {
            nombre = view.findViewById(R.id.txtNombre)
            empresa = view.findViewById(R.id.txtEmpresa)
            foto = view.findViewById(R.id.imgFoto)
        }
    }

    fun  filtrar(str:String){
        items?.clear()

        if (str.isEmpty()){
            items = ArrayList(copiaItems)
            notifyDataSetChanged()
            return
        }

        var busqueda = str
        busqueda = busqueda.toLowerCase()

        for (item in copiaItems!!){
            val nombre = item.nombre.toLowerCase()
            if (nombre.contains(busqueda)){
                items?.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun addItem(item:Contacto){
        copiaItems?.add(item)
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

    fun removItem(index:Int){
        copiaItems?.removeAt(index)
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

    fun updateItem(index: Int, newItem: Contacto){
        copiaItems?.set(index, newItem)
        items = ArrayList(copiaItems)
        notifyDataSetChanged()
    }

}