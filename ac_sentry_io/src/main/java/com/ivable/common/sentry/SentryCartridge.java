package com.ivable.common.sentry;

import com.intershop.beehive.core.capi.configuration.ConfigurationMgr;
import com.intershop.beehive.core.capi.environment.LifecycleListenerException;
import com.intershop.beehive.core.capi.log.Logger;
import com.intershop.beehive.core.capi.naming.NamingMgr;
import com.intershop.beehive.core.internal.environment.ExtendedVersionInformation;
import com.intershop.beehive.core.internal.environment.InstallationRegistryHelper;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.event.helper.EventBuilderHelper;

public class SentryCartridge extends com.intershop.beehive.core.capi.cartridge.Cartridge
{
    private static final String UNKNOWN = "unknown";

    @Override
    public void onReadyHook() throws LifecycleListenerException
    {
        super.onReadyHook();

        String dns = ConfigurationMgr.getString("io.sentry.dns", null);
        if (dns != null && !dns.isEmpty())
        {
            ExtendedVersionInformation versionInfo = InstallationRegistryHelper.getSFSVersionInformation();
            String env = ConfigurationMgr.getString("environment", UNKNOWN);
            String dist = ConfigurationMgr.getString("intershop.PlatformID", UNKNOWN);
            String servername = ConfigurationMgr.getString("intershop.HostName", UNKNOWN);
            String version = versionInfo.getVersion();

            SentryClient client = Sentry.init(dns);
            client.setRelease(version);
            client.setEnvironment(env);
            client.setServerName(servername);
            client.setDist(dist);
            
            //add sentry config
            ConfigurationMgr.setString("io.sentry.release", version);
            ConfigurationMgr.setString("io.sentry.environment", env);
            ConfigurationMgr.setString("io.sentry.serverName", servername);
            ConfigurationMgr.setString("io.sentry.dist", dist);

            // event builder specially for ICM
            EventBuilderHelper eventBuilderHelper = NamingMgr.get(EventBuilderHelper.class);
            client.addBuilderHelper(eventBuilderHelper);

            Logger.info(this, "Sentry init successful : dns = {},release = {},env = {},servername={},dist = {}", dns,
                            versionInfo.getVersion(), env, servername, dist);
        }
        else
        {
            Logger.error(this, "Sentry not loaded, could not find DNS setting");
        }
    }
}