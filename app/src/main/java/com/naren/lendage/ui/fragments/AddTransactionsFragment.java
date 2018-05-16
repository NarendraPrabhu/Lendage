package com.naren.lendage.ui.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.naren.lendage.R;
import com.naren.lendage.data.LendageDatabase;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.dao.TransactionDaoRunnable;
import com.naren.lendage.data.entities.UnitTransaction;
import com.naren.lendage.data.entities.User;
import com.naren.lendage.databinding.AddTransactionBinding;
import com.naren.lendage.ui.listener.AddTransactionClickListener;

public class AddTransactionsFragment extends DialogFragment implements AddTransactionClickListener{

    private AddTransactionBinding binding = null;
    private User user = null;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        user = args.getParcelable(User.class.getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddTransactionBinding.inflate(getActivity().getLayoutInflater());
        binding.setTransaction(new UnitTransaction());
        binding.setUser(user);
        binding.setListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(binding.getUser() == null){
            dismiss();
        }
    }

    @Override
    public void save(UnitTransaction ut) {
        Runnable runnable = new TransactionDaoRunnable(ut) {
            @Override
            protected void run(UnitTransaction t) {
                User user = binding.getUser();
                UnitTransaction ut = binding.getTransaction();
                ut.userId = user.id;
                ut.time = System.currentTimeMillis();

                LendageDatabase db = LendageDatabase.getInstance(getActivity().getApplicationContext());
                db.getTransactionDao().insert(ut);
                user.lastTransactionDate = System.currentTimeMillis();
                user.balance = user.balance - ut.amount;
                db.getUserDao().update(user);
            }
        };
        DataTransactionThread dtt = new DataTransactionThread(runnable, new DataTransactionThread.OnTransactionCompleteCallback() {
            @Override
            public void onTransactionComplete() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                });
            }
        });
        dtt.start();
    }
}
