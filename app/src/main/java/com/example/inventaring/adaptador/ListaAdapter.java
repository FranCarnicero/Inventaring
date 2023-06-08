package com.example.inventaring.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaring.R;
import com.example.inventaring.entidades.Productos;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ProductoViewHolder> {


    ArrayList<Productos> listaProductos;


    /**
     * Constructor de la clase ListaAdapter.
     *
     * @param listaProductos La lista de productos a mostrar en el RecyclerView.
     */
    public ListaAdapter(ArrayList<Productos> listaProductos){
        this.listaProductos= listaProductos;
    }


    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflar el diseño de un elemento de lista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item,null, false);

        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {

        // Configurar los datos del producto en el elemento de lista
        holder.ShowName.setText(listaProductos.get(position).getPRODUCTO());
        holder.ShowClave.setText(listaProductos.get(position).getClave());
        holder.ShowCantidad.setText(listaProductos.get(position).getCANTIDAD());


    }

    @Override
    public int getItemCount() {return listaProductos.size();}

    public class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView ShowName, ShowClave, ShowCantidad;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);


            ShowName=itemView.findViewById(R.id.ShowName);

            ShowCantidad = itemView.findViewById(R.id.ShowCantidad);

            ShowClave = itemView.findViewById(R.id.ShowClave);
        }
    }
}
