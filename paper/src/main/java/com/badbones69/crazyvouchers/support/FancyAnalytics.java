package com.badbones69.crazyvouchers.support;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazyvouchers.CrazyVouchers;
import com.badbones69.crazyvouchers.config.ConfigManager;
import com.badbones69.crazyvouchers.config.types.ConfigKeys;
import de.oliver.fancyanalytics.api.FancyAnalyticsAPI;

public class FancyAnalytics {

    private static final String token = "bdjvCTc5Njc3YjQ1ZmYyODQyZmMRYveG";
    private static final String id = "7a656685-2969-498f-ad82-d371fc3dac78";

    private final CrazyVouchers plugin = CrazyVouchers.get();
    private final SettingsManager config = ConfigManager.getConfig();

    private FancyAnalyticsAPI analytics;

    public void start() {
        if (!this.config.getProperty(ConfigKeys.toggle_metrics)) return;

        this.analytics = new FancyAnalyticsAPI(id, token);

        this.analytics.registerMinecraftPluginMetrics(this.plugin);
        this.analytics.getExceptionHandler().registerLogger(this.plugin.getLogger());
        this.analytics.initialize();
    }
}