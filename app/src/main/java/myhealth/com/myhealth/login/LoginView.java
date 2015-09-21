package myhealth.com.myhealth.login;

public interface LoginView {
    String getEmail();

    void showEmailError(int resId);

    String getPassword();

    void showPasswordError(int resId);

}
