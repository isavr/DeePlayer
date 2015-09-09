package com.tutorial.deeplayer.app.deeplayer.viewmodels;

import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.rest.service.RestService;
import com.tutorial.deeplayer.app.deeplayer.rest.service.ServiceMoc;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

/**
 * Created by ilya.savritsky on 19.08.2015.
 */
public class RadioViewModelTest extends RadioViewModel {
    @Test
    public void testRadioWithStatuses() {
        TestSubscriber<List<Radio>> testSubscriber = new TestSubscriber<>();
//        getRadiosWithStatusesObservable().subscribe(testSubscriber);
//        testSubscriber.assertNoErrors();
//        Assert.assertEquals(testSubscriber.getOnNextEvents().size(), 1);
//        testSubscriber.assertValueCount(1);
//        testSubscriber.assertCompleted();
    }

    @Override
    protected RestService getRestService() {
        return new ServiceMoc();
    }

    //@Override
    protected Observable<DataList<Radio>> warpToIoThread(Observable<DataList<Radio>> dataObservable) {
        // Do nothing while testing
        return dataObservable;
    }
}
