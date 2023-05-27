package org.howtoswat.utils;

import org.howtoswat.HowToSWAT;

import java.util.ArrayList;
import java.util.List;

public class AdminUtils {

    public static boolean isAdmin(String uuid) {
        return getAdmins().contains(uuid);
    }

    public static List<String> getAdmins() {
        List<String> admins = new ArrayList<>();
        for (Object admin : HowToSWAT.admins) admins.add(admin.toString());
        return admins;
    }

    public static boolean isBuilder(String uuid) {
        return getBuilder().contains(uuid);
    }

    public static List<String> getBuilder() {
        List<String> builders = new ArrayList<>();
        for (Object builder : HowToSWAT.builder) builders.add(builder.toString());
        return builders;
    }
}
