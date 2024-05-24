package cl.telecom.patriciococobowl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import cl.telecom.patriciococobowl.Clases.Pedido;
import cl.telecom.patriciococobowl.R;

public class PedidoAdapter extends FirestoreRecyclerAdapter<Pedido, PedidoAdapter.ViewHolder> {
    public PedidoAdapter(@NonNull FirestoreRecyclerOptions<Pedido> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Pedido pedido) {
        viewHolder.direccion.setText(pedido.getDireccion());
        viewHolder.nombre.setText(pedido.getNombre());
        viewHolder.producto1.setText(pedido.getProducto1());
        viewHolder.producto2.setText(pedido.getProducto2());
        viewHolder.producto3.setText(pedido.getProducto3());
        viewHolder.producto4.setText(pedido.getProducto4());
        viewHolder.producto5.setText(pedido.getProducto5());
        viewHolder.telefono.setText(pedido.getTelefono());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_pedidos, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView direccion, nombre, producto1, producto2, producto3, producto4, producto5, telefono;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            direccion = itemView.findViewById(R.id.vdireccion);
            nombre = itemView.findViewById(R.id.vnombre);
            producto1 = itemView.findViewById(R.id.vproducto1);
            producto2 = itemView.findViewById(R.id.vproducto2);
            producto3 = itemView.findViewById(R.id.vproducto3);
            producto4 = itemView.findViewById(R.id.vproducto4);
            producto5 = itemView.findViewById(R.id.vproducto5);
            telefono = itemView.findViewById(R.id.vtelefono);
        }
    }
}
