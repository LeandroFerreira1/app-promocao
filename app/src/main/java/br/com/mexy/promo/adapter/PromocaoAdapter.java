package br.com.mexy.promo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;

public class PromocaoAdapter  extends RecyclerView.Adapter<PromocaoAdapter.MyViewHolder> {

    private List<Promocao> promocoes;
    private Estabelecimento estabelecimento;

    public PromocaoAdapter(List<Promocao> promocoes, Estabelecimento estabelecimento) {
        this.promocoes = promocoes;
        this.estabelecimento = estabelecimento;
    }

    @Override
    public PromocaoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_estabelecimento_promocao, parent, false);
        return new PromocaoAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(PromocaoAdapter.MyViewHolder holder, int position) {
        Promocao promocao = promocoes.get(position);
        holder.textViewMarca.setText(promocao.getProduto().getNome());
        holder.textViewPreco.setText("R$ "+promocao.getValorPromocional());
        Picasso.get()
                .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewProduto);
        Picasso.get()
                .load(DataService.BASE_URL + estabelecimento.getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.imageViewEstab);

    }

    @Override
    public int getItemCount() {
        return promocoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMarca;
        private TextView textViewPreco;
        private ImageView imageViewProduto;
        private ImageView imageViewEstab;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewMarca = itemView.findViewById(R.id.textViewMarca);
            textViewPreco = itemView.findViewById(R.id.textViewPreco);
            imageViewProduto = itemView.findViewById(R.id.imageViewProduto);
            imageViewEstab = itemView.findViewById(R.id.imageViewEstab);

        }
    }

}