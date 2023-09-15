package com.example.expensesmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesmanager.R;
import com.example.expensesmanager.databinding.RowTransactionBinding;
import com.example.expensesmanager.model.Category;
import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder> {

    Context context;

    ArrayList<Transaction> transactions;

    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.binding.transactionAmount.setText("" + transaction.getAmount());

        if (transaction.getType().equals(Constants.INCOME))
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.green));
        else if (transaction.getType().equals(Constants.EXPENSE))
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.red));

        holder.binding.accountLbl.setText(transaction.getAccount());
        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));
        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
