package com.example.payphonedemoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context cntx;

    LayoutInflater inflater;
    List<Curso> cursos;
    String currentPhone;

    public Adapter(Context cntx, List<Curso> cursos, String currentPhone){
        this.inflater = LayoutInflater.from(cntx);
        this.cntx = cntx;
        this.cursos = cursos;
        this.currentPhone = currentPhone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_cursos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(cursos.get(position).getNombre());
        holder.descripcion.setText(cursos.get(position).getDescripcion());
        double total = cursos.get(position).getPrecio() + cursos.get(position).getImpuesto();
        holder.precio.setText(String.valueOf(total));
        //Picasso.get().load(cursos.get(position).getImgJPG1()).error(R.mipmap.ic_launcher).into(holder.profilePhoto)

        int id = cursos.get(position).getIdCurso();

        holder.btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cntx.getApplicationContext(), ConfirmationActivity.class);
                intent.putExtra("idCurso", String.valueOf(id));
                intent.putExtra("phone", currentPhone);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cntx.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Se declaran todos los tipos de elementos que se tiene en la interfaz
        TextView nombre, descripcion, precio;
        ImageView profilePhoto;
        Button btnCompra;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.lblCurso);
            descripcion = itemView.findViewById(R.id.lblDescripcion);
            precio = itemView.findViewById(R.id.lblPrecio);
            profilePhoto = itemView.findViewById(R.id.evalimg);
            btnCompra = itemView.findViewById(R.id.btnComprar);
        }
    }
}
