package myhealth.com.myhealth.login;

import android.content.SharedPreferences;

import myhealth.com.myhealth.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    @Mock
    private SharedPreferences.Editor editor;
    @Mock
    private LoginActivity view;
    @Mock
    private LoginService service;
    private LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(view);
    }


    @Test
    public void shouldShowErrorMessageWhenEmailIsInvalid() throws Exception {
        when(view.getEmail()).thenReturn("");
        presenter.onLoginClicked();

        when(view.getEmail()).thenReturn("johnbakkergmail.com");
        presenter.onLoginClicked();

        when(view.getEmail()).thenReturn("johnbakker@gmailcom");
        presenter.onLoginClicked();

        when(view.getEmail()).thenReturn("johnbakkergmailcom");
        presenter.onLoginClicked();
        verify(view, times(4)).showEmailError(R.string.email_error);
    }

    @Test
    public void shouldNotShowErrorMessageWhenEmailIsValid() throws Exception {
        when(view.getEmail()).thenReturn("johnbakker@gmail.com");
        when(view.getEmail()).thenReturn("test");
        presenter.onLoginClicked();
        verify(view, times(0)).showEmailError(R.string.email_error);
    }

    @Test
    public void shouldShowErrorMessageWhenPasswordIsInvalid() throws Exception {
        when(view.getEmail()).thenReturn("johnbakker@gmail.com");
        when(view.getPassword()).thenReturn("");
        presenter.onLoginClicked();
        verify(view).showPasswordError(R.string.password_error);
    }

    @Test
    public void shouldNotShowErrorMessageWhenPasswordIsValid() throws Exception {
        when(view.getEmail()).thenReturn("johnbakker@gmail.com");
        when(view.getPassword()).thenReturn("test");
        presenter.onLoginClicked();
        verify(view, never()).showPasswordError(R.string.password_error);
    }

    @Test
    public void saveJWTShouldSaveJWT() throws Exception {
        String s = "test";
        presenter.saveJWT(s);
        verify(editor).putString("jwt", s);
        verify(editor).apply();
    }
}