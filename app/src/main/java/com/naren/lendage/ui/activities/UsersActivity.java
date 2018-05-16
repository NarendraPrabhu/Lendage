package com.naren.lendage.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.naren.lendage.R;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.entities.User;
import com.naren.lendage.databinding.UserItemBinding;
import com.naren.lendage.ui.decor.ItemTouchHelperCallback;
import com.naren.lendage.ui.decor.ItemsDividerDecoration;
import com.naren.lendage.ui.listener.OnUserClickListener;
import com.naren.lendage.ui.vm.UsersViewModel;

import java.util.List;

public class UsersActivity extends BaseActivity implements View.OnClickListener{

    private UsersViewModel viewModel;
    private final UserAdapter mUserAdapter = new UserAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.list);
        rv.setAdapter(mUserAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        ItemsDividerDecoration decoration = new ItemsDividerDecoration(this, 1);
        rv.addItemDecoration(decoration);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(this, mUserAdapter));
        helper.attachToRecyclerView(rv);

        viewModel = ViewModelProviders.of(this).get(UsersViewModel.class);
        viewModel.getUserLive().observe(this, mUserAdapter);
        findViewById(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(UsersActivity.this, AddUserActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class Holder extends RecyclerView.ViewHolder{
        public UserItemBinding binding;
        public Holder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(User user){
            binding.setUser(user);
            binding.executePendingBindings();
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<Holder> implements OnUserClickListener, ItemTouchHelperCallback.SwipeItemCallback, Observer<List<User>> {

        private List<User> users = null;

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            UserItemBinding binding = UserItemBinding.inflate(LayoutInflater.from(UsersActivity.this));
            binding.setListener(this);
            return new Holder(binding.getRoot());
        }

        @Override
        public void onClick(User user) {
            Intent intent = new Intent(UsersActivity.this, TransactionsActivity.class);
            intent.putExtra(User.class.getSimpleName(), user);
            startActivity(intent);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bind(users.get(position));
        }

        @Override
        public int getItemCount() {
            return users != null ? users.size() : 0;
        }

        @Override
        public void onSwipe(RecyclerView.ViewHolder viewHolder) {
            if(viewHolder instanceof Holder) {
                User u = ((Holder)viewHolder).binding.getUser();
                showDeleteAlert(u);
            }
        }

        @Override
        public void onChanged(@Nullable List<User> users) {
            if(users == null){
                return;
            }
            this.users = users;
            notifyItemRangeChanged(0, users.size());
        }

        private void showDeleteAlert(User u){
            mUserAdapter.notifyDataSetChanged();
            new AlertDialog.Builder(UsersActivity.this)
                    .setTitle(R.string.warning_delete_title_user)
                    .setMessage(R.string.warning_delete_message)
                    .setPositiveButton(android.R.string.yes, new DeleteUserListener(u))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .show();
        }

        private class DeleteUserListener implements DialogInterface.OnClickListener{

            private final User user;
            DeleteUserListener(User u){
                this.user = u;
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                users.remove(user);
                viewModel.delete(user, new DataTransactionThread.OnTransactionCompleteCallback() {
                    @Override
                    public void onTransactionComplete() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mUserAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }

        }
    }
}
