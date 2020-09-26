package com.ivable.common.sentry.internal.modules;

import com.ivable.common.sentry.internal.ICMEventBuilderHelperImpl;

import io.sentry.event.helper.EventBuilderHelper;

/**
 * Contributes bindings and other configurations.
 */
public class Sentry_ioNamingModule extends com.google.inject.AbstractModule
{
    @Override
    protected void configure()
    {
        bind(EventBuilderHelper.class).to(ICMEventBuilderHelperImpl.class);
    }
}