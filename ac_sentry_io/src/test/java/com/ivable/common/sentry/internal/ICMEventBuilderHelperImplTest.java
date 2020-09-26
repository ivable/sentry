package com.ivable.common.sentry.internal;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import io.sentry.event.EventBuilder;

public class ICMEventBuilderHelperImplTest
{
    @Rule
    public final MockitoRule mockingRule = MockitoJUnit.rule();
    
    @Mock
    EventBuilder event;

    @Test
    public void eventBuilderIsNullTest()
    {
        ICMEventBuilderHelperImpl eventBuilderHelper = new ICMEventBuilderHelperImpl();
        eventBuilderHelper.helpBuildingEvent(null);
    }
    
    @Test
    public void eventBuilderTest() {
        ICMEventBuilderHelperImpl eventBuilderHelper = new ICMEventBuilderHelperImpl();
        eventBuilderHelper.helpBuildingEvent(event);
    }

}
