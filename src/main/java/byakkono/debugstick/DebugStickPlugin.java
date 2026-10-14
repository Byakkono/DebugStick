package byakkono.debugstick;

import org.powernukkitx.Player;
import org.powernukkitx.command.Command;
import org.powernukkitx.command.CommandSender;
import org.powernukkitx.plugin.PluginBase;

public class DebugStickPlugin extends PluginBase {

    static final String PERMISSION = "debugstick.use";

    private Messages messages;
    private DebugStickItem item;
    private Access access;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messages = Messages.load(this);
        item = new DebugStickItem(messages);
        access = loadAccess();
        getServer().getPluginManager().registerEvents(new DebugStickListener(access), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.text(sender, Messages.Key.PLAYER_ONLY));
        } else if (!access.allows(player)) {
            Messages.Key reason = access == Access.CREATIVE ? Messages.Key.CREATIVE_ONLY : Messages.Key.NO_PERMISSION;
            player.sendMessage(messages.text(player, reason));
        } else if (!item.giveTo(player)) {
            player.sendMessage(messages.text(player, Messages.Key.INVENTORY_FULL));
        }
        return true;
    }

    private Access loadAccess() {
        String value = getConfig().getString("access", "op");
        Access access = Access.parse(value);
        if (access == null) {
            getLogger().warning("Unknown access '" + value + "', using op");
            return Access.OP;
        }
        return access;
    }
}
