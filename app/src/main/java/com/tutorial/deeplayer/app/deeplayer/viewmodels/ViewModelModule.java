package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ilya.savritsky on 27.07.2015.
 */
@Module
public class ViewModelModule {
    @Provides
    public LoginViewModel provideLoginViewModel() {
        return new LoginViewModel();
    }

    @Provides
    public RadioViewModel provideRadioViewModel() {
        return new RadioViewModel();
    }
}
