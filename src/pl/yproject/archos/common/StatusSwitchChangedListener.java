package pl.yproject.archos.common;

import android.widget.CompoundButton;
import pl.yproject.archos.activities.ArchosControlPanelActivity;

/**
 * Created with notepad.exe.
 * Author: Mateusz
 * Date: 23.11.13
 * Time: 16:44
 */
public class StatusSwitchChangedListener implements CompoundButton.OnCheckedChangeListener {

    private ArchosControlPanelActivity archosControlPanelActivity;

    public StatusSwitchChangedListener(ArchosControlPanelActivity archosControlPanelActivity) {
        this.archosControlPanelActivity = archosControlPanelActivity;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        archosControlPanelActivity.setPrefs();
    }
}
