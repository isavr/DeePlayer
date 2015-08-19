package com.tutorial.deeplayer.app.deeplayer.rest;

import com.tutorial.deeplayer.app.deeplayer.pojo.DataList;
import com.tutorial.deeplayer.app.deeplayer.pojo.Radio;
import com.tutorial.deeplayer.app.deeplayer.rest.service.ServiceMoc;

import org.junit.Assert;
import org.junit.Test;

import rx.observers.TestSubscriber;

/**
 * Created by ilya.savritsky on 19.08.2015.
 */
public class ServiceTest {
    @Test
    public void testRadioFetching() {
        ServiceMoc restService = new ServiceMoc();
        TestSubscriber<DataList<Radio>> testSubscriber = new TestSubscriber<>();
        restService.fetchRadioInfo().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
        DataList<Radio> radioDataList = testSubscriber.getOnNextEvents().get(0);
        Assert.assertEquals(restService.getMocRadioInfo().getData(), radioDataList.getData());
    }

    @Test
    public void testUserRadioFetching() {
        ServiceMoc restService = new ServiceMoc();
        TestSubscriber<DataList<Radio>> testSubscriber = new TestSubscriber<>();
        restService.fetchUserRadioInfo().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertCompleted();
        testSubscriber.assertValueCount(1);
        DataList<Radio> radioDataList = testSubscriber.getOnNextEvents().get(0);
        Assert.assertNotEquals(restService.getMocRadioInfo().getData(), radioDataList.getData());
        Assert.assertEquals(restService.getMocUserRadioInfo().getData(), radioDataList.getData());
    }
}
