package com.example.expensesmanager.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensesmanager.R;
import com.example.expensesmanager.adapter.AccountsAdapter;
import com.example.expensesmanager.adapter.CategoryAdapter;
import com.example.expensesmanager.databinding.FragmentAddTransactionBinding;
import com.example.expensesmanager.databinding.ListDialogBinding;
import com.example.expensesmanager.model.Account;
import com.example.expensesmanager.model.Category;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    FragmentAddTransactionBinding binding;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater);

        binding.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.testColor));
            }
        });

        binding.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
                binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
                binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
                binding.incomeBtn.setTextColor(getContext().getColor(R.color.testColor));
            }
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        calendar.set(Calendar.MONDAY, datePickerDialog.getDatePicker().getMonth());
                        calendar.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());

                        String dateToShow = Helper.formatDate(calendar.getTime());
                        binding.date.setText(dateToShow);
                    }
                });
                datePickerDialog.show();
            }
        });

        binding.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
                AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
                categoryDialog.setView(listDialogBinding.getRoot());


                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickedListener() {
                    @Override
                    public void onClickedLstener(Category category) {
                        binding.category.setText(category.getCategoryName());
                        categoryDialog.dismiss();
                    }
                });
                listDialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                listDialogBinding.recyclerView.setAdapter(categoryAdapter);

                categoryDialog.show();

            }
        });

        binding.account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
                AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
                accountDialog.setView(listDialogBinding.getRoot());

                ArrayList<Account> accounts = new ArrayList<>();

                accounts.add(new Account(1000, "Card"));
                accounts.add(new Account(1000, "Bank"));
                accounts.add(new Account(1000, "Paytm"));
                accounts.add(new Account(1000, "Phone Pay"));
                accounts.add(new Account(1000, "Other"));

                AccountsAdapter accountsAdapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
                    @Override
                    public void onAccountSelected(Account account) {
                        binding.account.setText(account.getAccountName());
                        accountDialog.dismiss();
                    }
                });

                listDialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                listDialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                listDialogBinding.recyclerView.setAdapter(accountsAdapter);

                accountDialog.show();

            }
        });
        return binding.getRoot();
    }
}