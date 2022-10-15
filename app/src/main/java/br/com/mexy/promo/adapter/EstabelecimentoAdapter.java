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

public class EstabelecimentoAdapter extends RecyclerView.Adapter<EstabelecimentoAdapter.MyViewHolder> {

    private List<Estabelecimento> estabelecimentos;
    private Context context;

    public EstabelecimentoAdapter(List<Estabelecimento> estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_estabelecimento, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Estabelecimento estabelecimento = estabelecimentos.get(position);

        Picasso.get()
                .load(DataService.BASE_URL + estabelecimento.getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewEstabelecimento);

        holder.textNome.setText(estabelecimento.getNome());
        holder.textTelefone.setText(estabelecimento.getTelefone());
        holder.textEndereco.setText(estabelecimento.getEndereco());

    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewEstabelecimento;
        private TextView textNome;
        private TextView textTelefone;
        private TextView textEndereco;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewEstabelecimento = itemView.findViewById(R.id.imageViewEstabelecimento);
            textNome = itemView.findViewById(R.id.textNome);
            textTelefone = itemView.findViewById(R.id.textTelefone);
            textEndereco = itemView.findViewById(R.id.textEndereco);


        }
    }

}