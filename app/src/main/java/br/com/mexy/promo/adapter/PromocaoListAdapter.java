package br.com.mexy.promo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;

public class PromocaoListAdapter extends RecyclerView.Adapter<PromocaoListAdapter.MyViewHolder> {

    private List<Promocao> promocoes;

    public PromocaoListAdapter(List<Promocao> promocoes) {
        this.promocoes = promocoes;
    }

    @Override
    public PromocaoListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_promocao, parent, false);
        return new PromocaoListAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(PromocaoListAdapter.MyViewHolder holder, int position) {
        Promocao promocao = promocoes.get(position);
        holder.textViewMarca.setText(promocao.getProduto().getNome());
        holder.textViewPreco.setText("R$ "+promocao.getValorPromocional());
        holder.textViewEstabelecimento.setText(promocao.getEstabelecimento().getNome());
        Picasso.get()
                .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewProduto);
        Picasso.get()
                .load(DataService.BASE_URL + promocao.getUsuario().getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewUsuario);
    }

    @Override
    public int getItemCount() {
        return promocoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMarca;
        private TextView textViewPreco;
        private TextView textViewEstabelecimento;
        private ImageView imageViewProduto;
        private ImageView imageViewUsuario;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewMarca = itemView.findViewById(R.id.textViewMarca);
            textViewPreco = itemView.findViewById(R.id.textViewPreco);
            textViewEstabelecimento = itemView.findViewById(R.id.textViewEstabelecimento);
            imageViewProduto = itemView.findViewById(R.id.imageViewProduto);
            imageViewUsuario = itemView.findViewById(R.id.imageViewUsuariop);
        }
    }

}