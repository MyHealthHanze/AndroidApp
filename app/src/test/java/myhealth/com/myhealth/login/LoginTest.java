package myhealth.com.myhealth.login;

import myhealth.com.myhealth.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    @Mock
    private LoginActivity view;
    @Mock
    private LoginService service;
    private LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new LoginPresenter(view);
    }

    @Test
    public void shouldShowErrorMessageWhenEmailIsEmpty() throws Exception {
        when(view.getEmail()).thenReturn("");
        presenter.onLoginClicked();

        verify(view).showEmailError(R.string.email_error);
    }

    @Test
    public void shouldShowErrorMessageWhenPasswordIsEmpty() throws Exception {
        when(view.getEmail()).thenReturn("johnbakker@gmail.com");
        when(view.getPassword()).thenReturn("test");
        presenter.onLoginClicked();

        verify(view).showPasswordError(R.string.password_error);
    }
}