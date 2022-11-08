package br.com.mexy.promo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Usuario;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    private List<Usuario> usuarios;

    public RankingAdapter(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_usuario, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Usuario usuario = usuarios.get(position);

        Picasso.get()
                .load(DataService.BASE_URL + usuario.getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewUsuario);

        holder.textNome.setText(usuario.getNome());
        holder.textViewNota.setText(usuario.getPontuacao());

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewUsuario;
        private TextView textNome;
        private TextView textViewNota;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUsuario = itemView.findViewById(R.id.imageViewUsuario);
            textNome = itemView.findViewById(R.id.textNome);
            textViewNota = itemView.findViewById(R.id.textViewNota);
        }
    }

}
