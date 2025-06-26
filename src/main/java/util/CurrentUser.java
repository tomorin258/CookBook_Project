package util;

public class CurrentUser {
    private static int id;
    public static void setId(int userId) { id = userId; }
    public static int getId() { return id; }
}