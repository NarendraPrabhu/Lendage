package com.naren.lendage.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.naren.lendage.R;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.entities.UnitTransaction;
import com.naren.lendage.data.entities.User;
import com.naren.lendage.databinding.TransactionItemBinding;
import com.naren.lendage.databinding.UserItemBinding;
import com.naren.lendage.ui.fragments.AddTransactionsFragment;
import com.naren.lendage.ui.decor.ItemTouchHelperCallback;
import com.naren.lendage.ui.decor.ItemsDividerDecoration;
import com.naren.lendage.ui.vm.TransactionsViewModel;

import java.util.List;

public class TransactionsActivity extends BaseActivity implements View.OnClickListener{

    private final TransactionsAdapter mAdapter = new TransactionsAdapter();
    private User user = null;
    private TransactionsViewModel tvm = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactions_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ItemsDividerDecoration did = new ItemsDividerDecoration(this, 1);
        rv.setLayoutManager(llm);
        rv.addItemDecoration(did);
        rv.setAdapter(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(this, mAdapter));
        helper.attachToRecyclerView(rv);

        user = getIntent().getParcelableExtra(User.class.getSimpleName());
        if(user == null){
            finish();
            return;
        }
        UserItemBinding binding = UserItemBinding.bind(findViewById(R.id.user));
        binding.setUser(user);
        findViewById(R.id.user).findViewById(R.id.name).setVisibility(View.GONE);
        setTitle(user.name);

        tvm = ViewModelProviders.of(this).get(TransactionsViewModel.class);
        tvm.getTransactionsLive(user.id).observe(this, new Observer<List<UnitTransaction>>() {
            @Override
            public void onChanged(@Nullable List<UnitTransaction> unitTransactions) {
                mAdapter.setUnitTransactions(unitTransactions);
            }
        });
        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AddTransactionsFragment atf = new AddTransactionsFragment();
        Bundle b = new Bundle();
        b.putParcelable(User.class.getSimpleName(), user);
        atf.setArguments(b);
        atf.show(getFragmentManager(), "add");
    }

    private static class Holder extends RecyclerView.ViewHolder{
        TransactionItemBinding binding = null;

        public Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(UnitTransaction t){
            binding.setUnitTransaction(t);
            binding.executePendingBindings();
        }
    }

    private class TransactionsAdapter extends RecyclerView.Adapter<Holder> implements ItemTouchHelperCallback.SwipeItemCallback{

        private List<UnitTransaction> unitTransactions;

        public void setUnitTransactions(List<UnitTransaction> unitTransactions) {
            this.unitTransactions = unitTransactions;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TransactionItemBinding binding = TransactionItemBinding.inflate(LayoutInflater.from(TransactionsActivity.this), parent, false);
            return new Holder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(unitTransactions.get(position));
        }

        @Override
        public int getItemCount() {
            return unitTransactions != null ? unitTransactions.size() : 0;
        }

        @Override
        public void onSwipe(RecyclerView.ViewHolder viewHolder) {
            if(viewHolder instanceof Holder) {
                UnitTransaction ut = ((Holder)viewHolder).binding.getUnitTransaction();
                showDeleteAlert(ut);
            }
        }

        private void showDeleteAlert(UnitTransaction ut){
            mAdapter.notifyDataSetChanged();
            new AlertDialog.Builder(TransactionsActivity.this)
                    .setTitle(R.string.warning_delete_title_transaction)
                    .setMessage(R.string.warning_delete_message)
                    .setPositiveButton(android.R.string.yes, new DeleteTransactionListener(ut))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .show();
        }

        private class DeleteTransactionListener implements DialogInterface.OnClickListener{

            private final UnitTransaction ut;
            DeleteTransactionListener(UnitTransaction ut){
                this.ut = ut;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                unitTransactions.remove(ut);
                tvm.delete(ut, new DataTransactionThread.OnTransactionCompleteCallback() {
                    @Override
                    public void onTransactionComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }

        }
    }


}
