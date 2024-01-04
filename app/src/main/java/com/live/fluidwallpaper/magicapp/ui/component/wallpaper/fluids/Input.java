package com.live.fluidwallpaper.magicapp.ui.component.wallpaper.fluids;

import com.magicfluids.MotionEventWrapper;


public class Input {
    MotionEventWrapper[] Events = new MotionEventWrapper[1024];
    int NumEvents;
    public Input() {
        for (int i = 0; i < 1024; i++) {
            this.Events[i] = new MotionEventWrapper();
        }
        this.NumEvents = 0;
    }
}
