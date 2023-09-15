package com.example.expensesmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensesmanager.R;
import com.example.expensesmanager.databinding.SampleCategoryItemBinding;
import com.example.expensesmanager.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    public interface CategoryClickedListener
    {
        void onClickedLstener(Category category);
    }
    Context context;
    ArrayList<Category> categories;
    CategoryClickedListener categoryClickedListener;
    public CategoryAdapter(Context context, ArrayList<Category> categories,CategoryClickedListener categoryClickedListener) {
        this.context = context;
        this.categories = categories;
        this.categoryClickedListener = categoryClickedListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.categoryIcon.setImageResource(category.getCategoryImage());

        holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClickedListener.onClickedLstener(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        SampleCategoryItemBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}
