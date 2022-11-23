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
import br.com.mexy.promo.model.Avaliacao;
import br.com.mexy.promo.model.Usuario;

public class AvaliacaoAdapter extends RecyclerView.Adapter<AvaliacaoAdapter.MyViewHolder> {

    private List<Avaliacao> avaliacaos;
    private Usuario usuario;

    public AvaliacaoAdapter(List<Avaliacao> avaliacaos, Usuario usuario) {
        this.avaliacaos = avaliacaos;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public AvaliacaoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_avaliado, parent, false);
        return new AvaliacaoAdapter.MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull AvaliacaoAdapter.MyViewHolder holder, int position) {
        final Avaliacao avaliacao = avaliacaos.get(position);
        Picasso.get()
                .load(DataService.BASE_URL + avaliacao.getUsuarioComp().getUrlImagem())
                .error(R.mipmap.ic_logo)
                .into(holder.imageViewUsuario);
        holder.textDescricao.setText(avaliacao.getDescricao());
        holder.textViewNota.setText(avaliacao.getNota().toString());
    }

    @Override
    public int getItemCount() {
        return avaliacaos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewUsuario;
        private TextView textDescricao;
        private TextView textViewNota;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUsuario = itemView.findViewById(R.id.imageViewUsuario);
            textViewNota = itemView.findViewById(R.id.textViewNota);
            textDescricao = itemView.findViewById(R.id.textDescricao);
        }
    }
}
