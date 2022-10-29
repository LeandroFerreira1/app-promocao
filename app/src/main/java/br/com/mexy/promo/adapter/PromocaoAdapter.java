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

    public PromocaoAdapter(List<Promocao> promocoes) {
        this.promocoes = promocoes;
    }

    @Override
    public PromocaoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.produto_bottom_sheet_layout, parent, false);
        return new PromocaoAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(PromocaoAdapter.MyViewHolder holder, int position) {
        Promocao promocao = promocoes.get(position);
        holder.textMarca.setText(promocao.getProduto().getMarca());
        holder.textValor.setText("R$ "+promocao.getValorPromocional());
        Picasso.get()
                .load(DataService.BASE_URL + promocao.getProduto().getUrlImagem())
                .error(R.drawable.ic_error)
                .into(holder.ImgPromocao);
    }

    @Override
    public int getItemCount() {
        return promocoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textMarca;
        private TextView textValor;
        private ImageView ImgPromocao;

        public MyViewHolder(View itemView) {
            super(itemView);
            textMarca = itemView.findViewById(R.id.textViewMarca);
            textValor = itemView.findViewById(R.id.textValor);
            ImgPromocao = itemView.findViewById(R.id.ImgPromocao);

        }
    }

}