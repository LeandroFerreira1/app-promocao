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
import br.com.mexy.promo.model.Promocao;

public class PromocaoCardAdapter extends RecyclerView.Adapter<PromocaoCardAdapter.MyViewHolder> {

    private List<Promocao> promocoes;

    public PromocaoCardAdapter(List<Promocao> promocoes) {
        this.promocoes = promocoes;
    }

    @Override
    public PromocaoCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_perfil_promocao, parent, false);
        return new PromocaoCardAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(PromocaoCardAdapter.MyViewHolder holder, int position) {
        Promocao promocao = promocoes.get(position);

            holder.textViewMarca.setText(promocao.getProduto().getNome());
            holder.textViewPreco.setText("R$ "+promocao.getValorPromocional());
            Picasso.get()
                    .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                    .error(R.drawable.ic_error)
                    .into(holder.imageViewProduto);
            Picasso.get()
                    .load(DataService.BASE_URL + promocao.getEstabelecimento().getUrlImagem())
                    .error(R.drawable.ic_error)
                    .into(holder.imageViewEstab);
            if(promocao.getCurtidas().isEmpty()){
                holder.textViewLike.setText(String.valueOf(0));
            }else{
                holder.textViewLike.setText(String.valueOf(promocao.getCurtidas().size()));
            }
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
        private  TextView textViewLike;

        public MyViewHolder(View itemView) {
            super(itemView);
            textViewMarca = itemView.findViewById(R.id.textViewMarca);
            textViewPreco = itemView.findViewById(R.id.textViewPreco);
            imageViewProduto = itemView.findViewById(R.id.imageViewProduto);
            imageViewEstab = itemView.findViewById(R.id.imageViewEstab);
            textViewLike = itemView.findViewById(R.id.textViewLike);
        }
    }

}