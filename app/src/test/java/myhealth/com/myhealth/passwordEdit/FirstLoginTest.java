package myhealth.com.myhealth.passwordEdit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.api.API;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirstLoginTest {

    @Mock
    private PasswordEditActivity view;
    @Mock
    private API api;
    private PasswordEditPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new PasswordEditPresenter(view, api);
    }

    @Test
    public void testOnChangeClicked() throws Exception {
        when(view.getNewPassword1()).thenReturn("test");
        when(view.getNewPassword2()).thenReturn("test");
        when(view.getOldPassword()).thenReturn("oldtest");
        presenter.onChangeClicked();

        when(view.getNewPassword1()).thenReturn("");
        when(view.getNewPassword2()).thenReturn("test");
        when(view.getOldPassword()).thenReturn("oldtest");
        presenter.onChangeClicked();


        when(view.getNewPassword1()).thenReturn("test");
        when(view.getNewPassword2()).thenReturn("");
        when(view.getOldPassword()).thenReturn("oldtest");
        presenter.onChangeClicked();


        when(view.getNewPassword1()).thenReturn("test1");
        when(view.getNewPassword2()).thenReturn("test2");
        when(view.getOldPassword()).thenReturn("oldtest");
        presenter.onChangeClicked();

        verify(view, times(1)).showPassword1Error(R.string.password_empty1);
        verify(view, times(1)).showPassword2Error(R.string.password_empty2);
        verify(view, times(1)).showPassword2Error(R.string.password_not_the_same);
    }
}
