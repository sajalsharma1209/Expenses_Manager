package com.example.expensesmanager.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensesmanager.R;
import com.example.expensesmanager.adapter.AccountsAdapter;
import com.example.expensesmanager.adapter.CategoryAdapter;
import com.example.expensesmanager.databinding.FragmentAddTransactionBinding;
import com.example.expensesmanager.databinding.ListDialogBinding;
import com.example.expensesmanager.model.Account;
import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;
import com.example.expensesmanager.view.activity.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {

    FragmentAddTransactionBinding binding;

    Transaction transaction;

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
        transaction = new Transaction();

        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.testColor));
            transaction.setType(Constants.INCOME);
        });

        binding.expenseBtn.setOnClickListener(view -> {
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.red));
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.testColor));
            transaction.setType(Constants.EXPENSE);
        });

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                calendar.set(Calendar.MONDAY, datePickerDialog.getDatePicker().getMonth());
                calendar.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());

                String dateToShow = Helper.formatDate(calendar.getTime());
                binding.date.setText(dateToShow);

                transaction.setDate(calendar.getTime());
                transaction.setId(calendar.getTime().getTime());
            });
            datePickerDialog.show();
        });

        binding.category.setOnClickListener(view -> {

            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(listDialogBinding.getRoot());

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, category -> {
                binding.category.setText(category.getCategoryName());
                transaction.setCategory(category.getCategoryName());
                categoryDialog.dismiss();
            });
            listDialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            listDialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();

        });

        binding.account.setOnClickListener(view -> {
            ListDialogBinding listDialogBinding = ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(listDialogBinding.getRoot());

            ArrayList<Account> accounts = new ArrayList<>();

            accounts.add(new Account(1000, "Cash"));
            accounts.add(new Account(1000, "Bank"));
            accounts.add(new Account(1000, "Paytm"));
            accounts.add(new Account(1000, "Phone Pay"));
            accounts.add(new Account(1000, "Other"));

            AccountsAdapter accountsAdapter = new AccountsAdapter(getContext(), accounts, account -> {
                binding.account.setText(account.getAccountName());
                transaction.setAccount(account.getAccountName());
                accountDialog.dismiss();
            });

            listDialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            listDialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            listDialogBinding.recyclerView.setAdapter(accountsAdapter);

            accountDialog.show();

        });

        binding.saveTransactionBtn.setOnClickListener(view -> {

            String getincometextcolor = "" + binding.incomeBtn.getCurrentTextColor();
            String getgreencolor = "" + getResources().getColor(R.color.green);

            String getexpensetextcolor = "" + binding.expenseBtn.getCurrentTextColor();
            String getredcolor = "" + getResources().getColor(R.color.red);

            if (!(getincometextcolor.equals(getgreencolor) || getexpensetextcolor.equals(getredcolor))) {
                Toast.makeText(getActivity(), "Please select either income or expense", Toast.LENGTH_SHORT).show();
            } else {

                if (binding.date.getText().toString().trim().equals("") && binding.category.getText().toString().trim().equals("") && binding.account.getText().toString().trim().equals("") && binding.note.getText().toString().trim().equals("")) {
                    Toast.makeText((MainActivity) getActivity(), "All fields is mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    double amount = Double.parseDouble(binding.amount.getText().toString());
                    String note = binding.note.getText().toString();

                    if (transaction.getType().equals(Constants.EXPENSE))
                        transaction.setAmount(amount * -1);
                    else
                        transaction.setAmount(amount);
                    transaction.setNote(note);

                    ((MainActivity) getActivity()).viewModel.addTransactions(transaction);
                    ((MainActivity) getActivity()).getTransactions();
                    dismiss();

                }
            }

        });
        return binding.getRoot();
    }
}