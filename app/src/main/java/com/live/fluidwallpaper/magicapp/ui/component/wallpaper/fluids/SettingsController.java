package com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids;

;
import com.live.fluidwallpaper.magicapp.R;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.WallpaperActivity;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabColors;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabEffects;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabFluid;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabInput;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabSet;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabSimulation;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabEffects;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabFluid;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabInput;
import com.live.fluidwallpaper.magicapp.ui.component.wallpaper.tab.TabSimulation;
import com.magicfluids.Config;


public class SettingsController {
    private TabSet mainTabWidget;

    public void initControls(WallpaperActivity wallpaperActivity, Config config) {
        ColorPalette.init();
        if (mainTabWidget == null) {
            this.mainTabWidget = new TabSet(wallpaperActivity.findViewById(R.id.tabHost));
            this.mainTabWidget.addTab(new TabInput(config, wallpaperActivity));
            this.mainTabWidget.addTab(new TabSimulation(config, wallpaperActivity));
            this.mainTabWidget.addTab(new TabColors(config, wallpaperActivity));
            this.mainTabWidget.addTab(new TabFluid(config, wallpaperActivity));
            this.mainTabWidget.addTab(new TabEffects(config, wallpaperActivity));
            this.mainTabWidget.finalizeSetup();
        }
    }

    public void reloadEverything() {
        this.mainTabWidget.refreshState();
    }
}
