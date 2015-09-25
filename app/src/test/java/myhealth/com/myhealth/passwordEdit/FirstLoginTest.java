package myhealth.com.myhealth.passwordEdit;

import myhealth.com.myhealth.R;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
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
    }

    @Test
    public void testOnChangeClicked() throws  Exception {

    }
}
