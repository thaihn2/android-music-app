package com.framgia.thaihn.tmusic.util.music;

import android.support.annotation.IntDef;

public class StateManager {
    public static final int PREPARE = 0;
    public static final int PLAYING = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;
    public static final int ERROR = 4;

    public static final int LOOP_DISABLE = 0;
    public static final int LOOP_ALL = 1;
    public static final int LOOP_ONE = 2;

    @IntDef({PREPARE, StateManager.PLAYING, StateManager.PAUSE, StateManager.STOP,
            StateManager.ERROR})
    public @interface StatePlay {
    }

    @IntDef({LOOP_DISABLE, LOOP_ALL, LOOP_ONE})
    public @interface StateLoop {
    }
}
