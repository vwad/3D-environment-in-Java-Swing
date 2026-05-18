public class OSChecker {
    public static boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("linux");
    }

    public static boolean isWayland() {
        if (!isLinux()) return false;

        String waylandDisplay = System.getenv("WAYLAND_DISPLAY");
        return waylandDisplay.contains("wayland");
    }
}
