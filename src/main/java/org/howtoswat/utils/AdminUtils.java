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

    public static boolean isSupporter(String uuid) {
        return getSupporter().contains(uuid);
    }

    public static List<String> getSupporter() {
        List<String> supporters = new ArrayList<>();
        for (Object supporter : HowToSWAT.supporter) supporters.add(supporter.toString());
        return supporters;
    }

    public static boolean isBuilder(String uuid) {
        return getBuilder().contains(uuid);
    }

    public static List<String> getBuilder() {
        List<String> builders = new ArrayList<>();
        for (Object builder : HowToSWAT.builder) builders.add(builder.toString());
        return builders;
    }

    public static boolean isVerified(String uuid) {
        return getVerfied().contains(uuid);
    }

    public static List<String> getVerfied() {
        List<String> verified = new ArrayList<>();
        for (Object verify : HowToSWAT.verifies) verified.add(verify.toString());
        return verified;
    }
}
