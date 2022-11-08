package br.com.mexy.promo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.api.DataService;
import br.com.mexy.promo.model.Promocao;

public class PromocaoFilterAdapter extends RecyclerView.Adapter<PromocaoFilterAdapter.MyViewHolder> implements Filterable {

    private List<Promocao> promocoes;
    private List<Promocao> promocoesFiltro;


    public PromocaoFilterAdapter(List<Promocao> list) {

        this.promocoes = list;
        this.promocoesFiltro =  list;
    }

    @Override
    public PromocaoFilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_promocao, parent, false);
        return new PromocaoFilterAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(PromocaoFilterAdapter.MyViewHolder holder, int position) {
        Promocao promocao = promocoesFiltro.get(position);
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

    public void setListaPromocaoFiltro(List<Promocao> promocoesFiltro) {
        this.promocoesFiltro = promocoesFiltro;
    }

    @Override
    public int getItemCount() {
        return promocoesFiltro.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String query = constraint.toString();
                final String lowerCaseQuery = query.toLowerCase();

                if (query.isEmpty()) {
                    promocoesFiltro = promocoes;
                } else {
                    List<Promocao> filteredList = new ArrayList<>();
                    for (Promocao a : promocoes) {
                        final String nome = a.getProduto().getNome().toLowerCase();
                        final String estabelecimento = a.getEstabelecimento().getNome().toLowerCase();
                        if (nome.contains(lowerCaseQuery) || estabelecimento.contains(lowerCaseQuery)) {
                            filteredList.add(a);
                        }
                    }
                    promocoesFiltro = filteredList;
                }

                FilterResults results = new FilterResults();
                results.count = promocoesFiltro.size();
                results.values = promocoesFiltro;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                promocoesFiltro = (List<Promocao>) results.values;
                notifyDataSetChanged();
            }
        };
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