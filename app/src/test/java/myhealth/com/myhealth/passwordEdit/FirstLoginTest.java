package myhealth.com.myhealth.passwordEdit;

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

/**
 * Created by wessel on 25-9-2015.
 */

@RunWith(MockitoJUnitRunner.class)
public class FirstLoginTest {

    @Mock
    private PasswordEditActivity view;
    @Mock
    private PasswordEditService service;
    private PasswordEditPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new PasswordEditPresenter(view);
        presenter.setService(service);
    }

    @Test
    public void testOnChangeClicked() throws  Exception {
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
        verify(service, times(1)).changePassword("oldtest", "test");
    }
}
