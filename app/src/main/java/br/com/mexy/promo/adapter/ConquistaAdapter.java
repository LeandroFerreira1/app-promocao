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
import br.com.mexy.promo.model.Conquista;
import br.com.mexy.promo.model.Estabelecimento;

public class ConquistaAdapter  extends RecyclerView.Adapter<ConquistaAdapter.MyViewHolder> {

    private List<Conquista> conquistas;
    private Context context;

    public ConquistaAdapter(List<Conquista> conquistas) {
        this.conquistas = conquistas;
    }

    @NonNull
    @Override
    public ConquistaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_conquista, parent, false);
        return new ConquistaAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ConquistaAdapter.MyViewHolder holder, int position) {

        final Conquista conquista = conquistas.get(position);

        Picasso.get()
                .load(DataService.BASE_URL + conquista.getImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewConquista);

    }

    @Override
    public int getItemCount() {
        return conquistas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewConquista;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewConquista = itemView.findViewById(R.id.imageViewConquista);
        }
    }

}
