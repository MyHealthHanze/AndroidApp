package myhealth.com.myhealth.passwordEdit;

import android.content.Context;

import myhealth.com.myhealth.R;

public class PasswordEditPresenter {
    PasswordEditView mView;
    PasswordEditService mService;

    public PasswordEditPresenter(PasswordEditView view) {
        mView = view;
        mService = new PasswordEditService((PasswordEditActivity) mView, this);
    }

    public void setService(PasswordEditService service) {
        this.mService = service;
    }

    /**
     * Handles the users interaction with the app
     */
    public void onChangeClicked() {
        String passwordOld = mView.getOldPassword();
        String password1 = mView.getNewPassword1();
        String password2 = mView.getNewPassword2();
        if (password1.isEmpty()) {
            mView.showPassword1Error(R.string.password_empty1);
            return;
        }
        if (password2.isEmpty()) {
            mView.showPassword2Error(R.string.password_empty2);
            return;
        }
        if (!password1.equals(password2)) {
            mView.showPassword2Error(R.string.password_not_the_same);
            return;
        }
        mService.changePassword(passwordOld, password1);
    }
}
