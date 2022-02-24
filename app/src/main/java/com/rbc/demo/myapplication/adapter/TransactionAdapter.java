package com.rbc.demo.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rbc.demo.myapplication.R;
import com.rbc.demo.myapplication.model.TransactionListItemType;
import com.rbc.rbcaccountlibrary.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private ArrayList<TransactionListItemType> items = new ArrayList();

    public void setData(List<TransactionListItemType> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == Type.HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_header_transaction, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_item_transaction, parent, false);
        }
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        if (items.get(position).getClass().equals(TransactionListItemType.TypeTransaction.class))
            holder.bind(Type.TRANSACTION, ((TransactionListItemType.TypeTransaction) items.get(position)).getTransaction());
        else {
            holder.bind(Type.HEADER, ((TransactionListItemType.TypeHeader) items.get(position)).getDate());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getClass() == TransactionListItemType.TypeHeader.class) {
            return Type.HEADER;
        } else {
            return Type.TRANSACTION;
        }
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        void bind(Integer type, Object value) {
            if (type == Type.HEADER) bindHeader((Calendar) value);
            if (type == Type.TRANSACTION) bindAccount((Transaction) value);
        }

        private void bindHeader(Calendar calendar) {
            StringBuilder date = new StringBuilder();
            date.append(calendar.get(Calendar.MONTH) + 1)
                    .append("/").append(calendar.get(Calendar.DATE))
                    .append("/").append(calendar.get(Calendar.YEAR));
            ((TextView)itemView.findViewById(R.id.header_text_trans)).setText(date.toString());
        }

        private void bindAccount(Transaction transaction) {
            ((TextView) view.findViewById(R.id.rv_trans_details)).setText(transaction.getDescription());
            ((TextView) view.findViewById(R.id.rv_trans_amnt)).setText(transaction.getAmount());
        }
    }
}
