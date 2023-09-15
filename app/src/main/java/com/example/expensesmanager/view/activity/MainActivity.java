package com.example.expensesmanager.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensesmanager.adapter.TransactionsAdapter;
import com.example.expensesmanager.model.Transaction;
import com.example.expensesmanager.utils.Constants;
import com.example.expensesmanager.utils.Helper;
import com.example.expensesmanager.view.fragment.AddTransactionFragment;
import com.example.expensesmanager.R;
import com.example.expensesmanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transaction");

        Constants.setCategories();
        calendar=Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE,1);
                updateDate();
            }
        });

        binding.previousDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE,-1);
                updateDate();
            }
        });

        binding.floatingActionButton.setOnClickListener(view -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        ArrayList<Transaction> transactions=new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Loan","Cash","some note",new Date(),200,5));
        transactions.add(new Transaction(Constants.INCOME,"Rent","Cash","some note",new Date(),200,5));
        transactions.add(new Transaction(Constants.INCOME,"Salary","Bank","some note",new Date(),200,5));
        transactions.add(new Transaction(Constants.EXPENSE,"Other","Cash","some note",new Date(),200,5));


        TransactionsAdapter adapter=new TransactionsAdapter(getApplicationContext(),transactions);
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionsList.setAdapter(adapter);
    }

    private void updateDate() {
        binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }
}