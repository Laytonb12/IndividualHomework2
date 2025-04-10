package application;

import java.util.ArrayList;
import java.util.List;

/**
 * AnnouncementManager is a simple utility class that manages
 * the list of all announcements made by staff members.
 *
 * Responsibilities:
 * - Adding new announcements
 * - Providing access to the current list of announcements
 *
 * Announcements are stored temporarily in memory and can be retrieved at any time.
 *
 * This class supports the PostAnnouncementPage and ViewAnnouncementsPage.
 */
public class AnnouncementManager {
    private static List<String> announcements = new ArrayList<>();

    public static void addAnnouncement(String announcement) {
        announcements.add(announcement);
    }

    public static List<String> getAnnouncements() {
        return announcements;
    }
}
