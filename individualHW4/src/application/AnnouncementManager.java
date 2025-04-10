package application;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementManager {
    private static List<String> announcements = new ArrayList<>();

    public static void addAnnouncement(String announcement) {
        announcements.add(announcement);
    }

    public static List<String> getAnnouncements() {
        return announcements;
    }
}
