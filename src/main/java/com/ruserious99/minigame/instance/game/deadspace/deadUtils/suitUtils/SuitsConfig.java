package com.ruserious99.minigame.instance.game.deadspace.deadUtils.suitUtils;

import com.ruserious99.minigame.Minigame;
import com.ruserious99.minigame.instance.game.deadspace.gameItems.PersistentData;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class SuitsConfig {

    public static String eliteSuitSignature() {
        return "sYJj4J8PWFaAaKcl+Rshi2eQX5ZaMiYf+baMIeiMoWYttgsujvHCJNHWODYAAhAAsYCSej0Kk/pwzvwL7Dngb2dVgyJ+oStY8V4h0dX+G8+uQAVlTYNTlzdGR2VOLd0sUf6NI4yjxlTda+kweRAoa/c8toObNh2qddoCzJy1TAmmztpdmRH7feLZCU7srKiOHcsFOHXwDMHBoMi4UPbLTU9wfLvCzPVX32IQfn7MibMPWoPKk1d3xp4lz3x6854d6MERfSjl7CMCAtrR/DR5vqqoOJc748X3j8Ci0T7GRcBBytWh9F9lhr/GqwZ6MyGISDtOK7pLbyRQeYp88ZQuQes8orKRAyKUI0e23Uko5WedFB3tmz2hL2RYZdGGQOWNR7VecmtX+Vu0tYqD01ljff8b1WN/nyTo/J7l3Uf2Nbju85AwF2ezeuR4iFWNfm9D2k/LHAkeMirPtePq3/Ev0di8PUBc2MBVD/TJGai9iowmTqJ8Wbu1eUEY0sdsvMyacW8hk3D6nPWwa30MkNCswGJUjGzUJm80PvkyRu6XD7B7gwa32TlOIY7ol/3bDhbfH2qlmVyYcB0zI6+mVwoDhb1HSRdClxT6DUhx3dvv5RXZx2MLLVP+mensQDiGPu24OcIdk95urh/dFtbVDmNux8yOl2nm9INPYmcF1VQyB+g=";
    }

    public static String eliteSuitValue() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTY4MDc3NjAwOTMyOCwKICAicHJvZmlsZUlkIiA6ICI5MWYwNGZlOTBmMzY0M2I1OGYyMGUzMzc1Zjg2ZDM5ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdG9ybVN0b3JteSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85ZjIxYzNhZTc2ZjQzYzBiZDE0YWRkY2YwY2FjOTVhYzRkMWRjYTk1ODg3NWYxNmUwODA1NjMyZGE4ZjA4NTRmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=";
    }


    public static String securitySuitSignature() {
        return "itL0snBabXNsV9u7sNvnwaSAPSiAhQTqiKK65wzI+zRE8iX792jpocs5fKNjFrZ7q8LorSNUZDjc7wjDEFrS7r9Hos/gH6NgK+w0adCV0E9dNeGZh6UXbTgr3yUVQqEeVq0OmPtJnIm4jmZj9WUS0XI2unvKLTsmPQxE2GVzc3bxXya5whm4aom/cBaW9g9g0snP3To3VNCF1f3oflXBeSBNle6KzH0mbTv6Me4cSzh8bxzwNLHw5827DQuYGKArlseqmY5JIzQ5m1e4TsXpiEWimPVGsaovacNxykQ7oSrcYfrr33nSpf2SGSsv1NzQsn6EJGX7HPYbrOyuDVolqnl/2R23/m2HWc77GACXqiIiiHwOtgyNNZGhVBMW8t/7c/jDXMyT132AX6Lm9P10Z68g73ikRJUloOz8z5ggp3Y3Q3sBTkyjdF2HWYkGU+xHC7dSKsjVTGBzFFF3A3lPVFIKZBAxTE5mQZH4SNRToHoJrMGPD2LJwpA1QIl7UV0unEFMzJ7a5oXf3e7izrSS/ZgJ7cp1SdoSkEY1JZPsB0rEGDnN8/VonDhEj1frZixm2H8tda4OVTcpSMUytCGTssibiK/Toiqnk7Zs88kwNgXIyaKBWVuvQwmpzoyZ5ibNlhdWzrJYShAeyPZEQH547wPEfw5+WjU797pNl11S5GE=";
    }

    public static String securitySuitValue() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYzMDQzMzk1Njk2NywKICAicHJvZmlsZUlkIiA6ICJhYzYxZjQwZGJhNDE0YzkwOWU0NWJkMTgwMmY5MTYxYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJBbmlmYW5pIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRmOWEwYWFjZWEwMTkyNWNhNDY0YjZjNmU4ZmVlZWVkYzc4NWMzZWY5OWI3OTkyZWFkNmEyNGY2MmQ0NDcyZDgiCiAgICB9CiAgfQp9";
    }


    public static String startSuitSignature() {
        return "PyFK5I2yD3sJlp2JFpy0i8Ios2hdcfFZArayTU8N0jbnUlw0lT94gSUhTg4AGAYrd0AgvJRekbbrwHjxLsZs9ylQCg2AW12vdGt3EyLw2RZgySB64JeluRtmXXiyz+GMAmBB3nGIw9xWSEvwJl3Jq/Spe+dXDZTTnh5vQYtw0Aw94QXbCZVl5OcriHPHz7Z+hxTgtFo7JvzgegQyVESMXYAtrElqHNKmMEn2fj4BMih1uGC73DqSe7x30zoAlKd6p5Cv3ghWMd8rHTatMndYEYYlliyv+sT5lTutg6z2jzttmfCeQdDGVKFfbgJlSqC3p37clKDeDrKRgLEv0Fx220FGS9iqyWSgkxskTErMEcrPeXupq9TVfpqqWG6Etssp2AEL1arY7b7PMO53r1Am9ILxN9W8R5VmTnoEeh2iQ4z+g/ArL/ykmhz29RsKrjOTHAukNPybc/xqaO2gwAA9a/xb7pslSY0GkAY8BbItQlBoHihO5Ca+FkbfSL5sD8ixjdTF72BBQDUp27c6H7UJONsaGOpcEN944ngG8eDXFLYAjp7JHEZUYFKU1DXvklmD35bFktNOAgUKUv2JOxlnq9lwxCw+IPmQk86RNfHJNnl2limowxME/s2j0EzmcdDCQb/xuHJN7CPH/Mbh15jA2TGihp51McgMqrPZwzhVEvc=";
    }

    public static String startSuitValue() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYxMTM0NTU0MTUyNCwKICAicHJvZmlsZUlkIiA6ICI2OTBkMDM2OGM2NTE0OGM5ODZjMzEwN2FjMmRjNjFlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ5emZyXzciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzcyZjEwMGEzOTk2OThkYWJhZTg3MGY4ODNhMDkyYjIwYTUxY2YyYjg5ZjRiMDNkYTEyYTNkY2EyZjg1ZmQxZiIKICAgIH0KICB9Cn0=";
    }


    public static void setSuitparams(Player player) {

        PersistentData persistentData = new PersistentData();
           String suit = persistentData.deadPlayerGetCustomDataTag(player, "deadInfoSuit");

        if (suit == null || suit.equals("startSuit")) {
            Minigame.getInstance().getDisguiseManager().applyDisguise(player, "Isac Clark", SuitsConfig.startSuitValue(), SuitsConfig.startSuitSignature());
            AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert maxHealthAttribute != null;
            maxHealthAttribute.setBaseValue(maxHealthAttribute.getBaseValue() + 4.0);
        } else if (suit.equals("eliteSuit")) {
            Minigame.getInstance().getDisguiseManager().applyDisguise(player, "Isac Clark", SuitsConfig.eliteSuitValue(), SuitsConfig.eliteSuitSignature());
            AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert maxHealthAttribute != null;
            maxHealthAttribute.setBaseValue(maxHealthAttribute.getBaseValue() + 12.0); // 1 extra heart = 2 health points
        } else if (suit.equals("securitySuit")) {
            Minigame.getInstance().getDisguiseManager().applyDisguise(player, "Isac Clark", SuitsConfig.securitySuitValue(), SuitsConfig.securitySuitSignature());
            AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            assert maxHealthAttribute != null;
            maxHealthAttribute.setBaseValue(maxHealthAttribute.getBaseValue() + 8.0);

        }
    }
}