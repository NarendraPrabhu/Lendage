package com.naren.lendage.ui.listener;

import com.naren.lendage.data.entities.User;

public interface AddUserBindingClickListener {
    void save(User user);
    void selectDate();
    void item(int type);
}
