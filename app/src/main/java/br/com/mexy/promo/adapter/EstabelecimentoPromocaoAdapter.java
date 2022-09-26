package br.com.mexy.promo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.mexy.promo.R;
import br.com.mexy.promo.model.Estabelecimento;

public class EstabelecimentoPromocaoAdapter  extends RecyclerView.Adapter<EstabelecimentoPromocaoAdapter.MyViewHolder> {

    private List<Estabelecimento> estabelecimentos;
    private EmpresaAdapterListener listener;

    public EstabelecimentoPromocaoAdapter(List<Estabelecimento> empresas, EmpresaAdapterListener listener) {
        this.estabelecimentos = empresas;
        this.listener = listener;
    }

    @Override
    public EstabelecimentoPromocaoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_promocao, parent, false);
        return new EstabelecimentoPromocaoAdapter.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(EstabelecimentoPromocaoAdapter.MyViewHolder holder, int position) {
        Estabelecimento estabelecimento = estabelecimentos.get(position);
        holder.textNomeEmpresa.setText(estabelecimento.getPromocao().getProduto().getMarca());
    }

    @Override
    public int getItemCount() {
        return estabelecimentos.size();
    }

    public interface EmpresaAdapterListener {
        void onSelected(Estabelecimento item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textNomeEmpresa;

        public MyViewHolder(View itemView) {
            super(itemView);
            textNomeEmpresa = itemView.findViewById(R.id.textViewMarca);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSelected(estabelecimentos.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onSelected(estabelecimentos.get(getAdapterPosition()));
                    return false;
                }
            });
        }
    }

}