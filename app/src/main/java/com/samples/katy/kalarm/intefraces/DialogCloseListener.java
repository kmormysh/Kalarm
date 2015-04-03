package com.samples.katy.kalarm.intefraces;

public interface DialogCloseListener {

    public void onCloseCreate(boolean[] days, int hours, int minutes, String name, boolean isRepeat);
    public void onCloseUpdate(int alarm_id, boolean[] days, int hours, int minutes, String name, boolean isRepeat);
}
