package byakkono.debugstick;

import org.powernukkitx.Player;

import java.util.Locale;

enum Access {
    /** Players with debugstick.use, which operators have by default. */
    OP,
    /** Anyone in creative mode. */
    CREATIVE,
    /** Anyone in any game mode. */
    EVERYONE;

    boolean allows(Player player) {
        return switch (this) {
            case OP -> player.hasPermission(DebugStickPlugin.PERMISSION);
            case CREATIVE -> player.isCreative();
            case EVERYONE -> true;
        };
    }

    static Access parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
