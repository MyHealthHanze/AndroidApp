package myhealth.com.myhealth.passwordEdit;

public interface PasswordEditView {
    String getOldPassword();

    String getNewPassword1();

    String getNewPassword2();

    void showPassword1Error(int resId);

    void showPassword2Error(int resId);
}
