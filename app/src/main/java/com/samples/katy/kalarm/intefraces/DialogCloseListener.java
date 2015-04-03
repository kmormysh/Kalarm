package com.samples.katy.kalarm.intefraces;

public interface DialogCloseListener {

    public void onCloseCreate(boolean[] days, String time, String name, boolean isRepeat);
    public void onCloseUpdate(int alarm_id, boolean[] days, String time, String name, boolean isRepeat);
}
