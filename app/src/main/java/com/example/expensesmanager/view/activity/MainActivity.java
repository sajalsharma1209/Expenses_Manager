package com.example.expensesmanager.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensesmanager.R;
import com.example.expensesmanager.adapter.TransactionsAdapter;
import com.example.expensesmanager.databinding.ActivityMainBinding;
import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;
import com.example.expensesmanager.view.fragment.AddTransactionFragment;
import com.example.expensesmanager.viewModel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public MainViewModel viewModel;
    ActivityMainBinding binding;
    Calendar calendar;
    /*
    daily = 0
    monthly = 1
    calendar = 2
    summary = 3
    notes = 4
    */
    int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transaction");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.SELECTED_TAB == Constants.DAILY)
                    calendar.add(Calendar.DATE, 1);
                else if (Constants.SELECTED_TAB == Constants.MONTHLY )
                    calendar.add(Calendar.MONTH, 1);
                updateDate();
            }
        });

        binding.previousDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.SELECTED_TAB == Constants.DAILY)
                    calendar.add(Calendar.DATE, -1);
                else if (Constants.SELECTED_TAB == Constants.MONTHLY)
                    calendar.add(Calendar.MONTH, -1);

                updateDate();
            }
        });

        binding.floatingActionButton.setOnClickListener(view -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionsAdapter adapter = new TransactionsAdapter(MainActivity.this, transactions);

                binding.transactionsList.setAdapter(adapter);

                if (transactions.size() > 0) {
                    binding.emptyState.setVisibility(View.GONE);
                } else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.getTransactions(calendar);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getText().equals("Monthly"))
                    Constants.SELECTED_TAB = 1;
                else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = 0;

                }

                updateDate();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
}