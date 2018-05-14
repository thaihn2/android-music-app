package com.framgia.thaihn.tmusic.util.music;

public interface MediaListener {

    interface ServiceListener {
        void eventPause();

        void eventPlay();

        void eventPlayFail(String message);

        void eventPrevious();

        void eventPreviousFail(String message);

        void eventNext();

        void eventNextFail(String message);

        void eventPlayExit();
    }
}
