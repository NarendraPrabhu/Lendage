package com.naren.lendage.ui.activities;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import com.naren.lendage.R;
import com.naren.lendage.data.LendageDatabase;
import com.naren.lendage.data.LoanType;
import com.naren.lendage.data.dao.DataTransactionThread;
import com.naren.lendage.data.dao.UserTransactionDaoRunnable;
import com.naren.lendage.data.entities.User;
import com.naren.lendage.databinding.AddUserLayoutBinding;
import com.naren.lendage.ui.listener.AddUserBindingClickListener;
import com.naren.lendage.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class AddUserActivity extends BaseActivity implements AddUserBindingClickListener {

    private AddUserLayoutBinding binding = null;
    private AlertDialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.add_user_layout);
        binding.setUser(new User());
        binding.setListener(this);
        binding.executePendingBindings();
        updateButton(new Date());
    }

    private void updateButton(Date date){
        binding.date.setTag(date.getTime());
        binding.date.setText(Utils.format(date));
    }

    @Override
    public void save(User user) {
        Runnable r = new UserTransactionDaoRunnable(user) {
            @Override
            protected void run(User u) {
                u.lastTransactionDate = u.lastUpdatedTime = u.time;
                LendageDatabase.getInstance(getApplicationContext()).getUserDao().insert(u);
            }
        };
        execute(r, new DataTransactionThread.OnTransactionCompleteCallback() {
            @Override
            public void onTransactionComplete() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }

    private void execute(Runnable r, DataTransactionThread.OnTransactionCompleteCallback callback){
        DataTransactionThread dtt = new DataTransactionThread(r, callback);
        dtt.start();
    }

    @Override
    public void selectDate() {
        if(dialog == null){
            DatePicker dp = new DatePicker(this);
            Calendar c = Calendar.getInstance();
            dp.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, monthOfYear);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateButton(c.getTime());
                }
            });
            dialog = new AlertDialog.Builder(this).setView(dp).create();
        }
        dialog.show();
    }

    @Override
    public void item(int type) {
        binding.numberOfInstallments.setVisibility(type == LoanType.Type.EMI.ordinal() ? View.VISIBLE : View.GONE);
        binding.numberOfInstallments.setText("");
    }
}
