package com.example.expensesmanager.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensesmanager.R;
import com.example.expensesmanager.adapter.TransactionsAdapter;
import com.example.expensesmanager.databinding.ActivityMainBinding;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;
import com.example.expensesmanager.view.fragment.AddTransactionFragment;
import com.example.expensesmanager.viewModel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public MainViewModel viewModel;
    ActivityMainBinding binding;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transaction");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        calendar = Calendar.getInstance();
        updateDate();

        Constants.setCategories();

        binding.nextDateBtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY)
                calendar.add(Calendar.DATE, 1);
            else if (Constants.SELECTED_TAB == Constants.MONTHLY)
                calendar.add(Calendar.MONTH, 1);

            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(view -> {

            if (Constants.SELECTED_TAB == Constants.DAILY)
                calendar.add(Calendar.DATE, -1);
            else if (Constants.SELECTED_TAB == Constants.MONTHLY)
                calendar.add(Calendar.MONTH, -1);

            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(view -> new AddTransactionFragment().show(getSupportFragmentManager(), null));

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
        viewModel.transactions.observe(this, transactions -> {
            TransactionsAdapter adapter = new TransactionsAdapter(MainActivity.this, transactions);

            binding.transactionsList.setAdapter(adapter);

            if (transactions.size() > 0) {
                binding.emptyState.setVisibility(View.GONE);
            } else {
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });

        viewModel.totalIncome.observe(this, aDouble -> binding.incomeLbl.setText(String.valueOf(aDouble)));

        viewModel.totalExpense.observe(this, aDouble -> binding.expenseLbl.setText(String.valueOf(aDouble)));

        viewModel.totalAmount.observe(this, aDouble -> binding.totalLbl.setText(String.valueOf(aDouble)));

        //  viewModel.getTransactions(calendar);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = Constants.MONTHLY;
                    binding.previousDateBtn.setVisibility(View.VISIBLE);
                    binding.nextDateBtn.setVisibility(View.VISIBLE);
                    binding.currentDate.setClickable(false);
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = Constants.DAILY;
                    binding.previousDateBtn.setVisibility(View.VISIBLE);
                    binding.nextDateBtn.setVisibility(View.VISIBLE);
                    binding.currentDate.setClickable(false);
                } else if (tab.getText().equals("Jump to date")) {
                    Constants.SELECTED_TAB = Constants.JUMP_TO_DATE;
                    binding.previousDateBtn.setVisibility(View.GONE);
                    binding.nextDateBtn.setVisibility(View.GONE);
                    binding.currentDate.setClickable(true);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);
                    datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        calendar1.set(Calendar.MONDAY, datePickerDialog.getDatePicker().getMonth());
                        calendar1.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());

                        String dateToShow = Helper.formatDate(calendar1.getTime());
                        binding.currentDate.setText(dateToShow);

                        calendar = (Calendar) calendar1.clone();
                        updateDate();

                    });
                    datePickerDialog.show();

                }
                updateDate();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getText().equals("Jump to date")) {

                    Constants.SELECTED_TAB = Constants.JUMP_TO_DATE;
                    binding.previousDateBtn.setVisibility(View.GONE);
                    binding.nextDateBtn.setVisibility(View.GONE);
                    binding.currentDate.setClickable(true);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this);
                    datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.DAY_OF_MONTH, datePickerDialog.getDatePicker().getDayOfMonth());
                        calendar1.set(Calendar.MONDAY, datePickerDialog.getDatePicker().getMonth());
                        calendar1.set(Calendar.YEAR, datePickerDialog.getDatePicker().getYear());

                        String dateToShow = Helper.formatDate(calendar1.getTime());
                        binding.currentDate.setText(dateToShow);

                        calendar = (Calendar) calendar1.clone();
                        updateDate();

                    });
                    datePickerDialog.show();

                }

            }
        });

    }

    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }

    private void updateDate() {

        if (Constants.SELECTED_TAB == Constants.DAILY)
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        else if (Constants.SELECTED_TAB == Constants.MONTHLY)
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        else if (Constants.SELECTED_TAB == Constants.JUMP_TO_DATE)
            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
//        Toast.makeText(this, "" + new Date(calendar.getTime().getTime()), Toast.LENGTH_SHORT).show();
        viewModel.getTransactions(calendar);

//        Toast.makeText(this, "" + new Date(calendar.getTime().getTime()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
}