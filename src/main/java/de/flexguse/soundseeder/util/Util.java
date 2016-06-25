package de.flexguse.soundseeder.util;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class Util {

    public static void showNotification(String caption, String description) {

        Notification notification = new Notification("", description, Type.HUMANIZED_MESSAGE);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
        notification.setDelayMsec(10000);
        notification.setIcon(FontAwesome.INFO_CIRCLE);
        
        notification.show(Page.getCurrent());
    }

}
