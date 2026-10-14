package byakkono.debugstick;

import org.powernukkitx.Player;
import org.powernukkitx.block.Block;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.block.BlockBreakEvent;
import org.powernukkitx.event.player.PlayerInteractEvent;
import org.powernukkitx.event.player.PlayerQuitEvent;
import org.powernukkitx.item.Item;
import org.powernukkitx.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Runs at HIGH and skips cancelled events, so protection plugins (usually NORMAL)
 * can stop the stick simply by cancelling the click.
 *
 * Left click: creative breaks the block at once (BlockBreakEvent), survival only starts
 * breaking (LEFT_CLICK_BLOCK), so the property is selected on whichever comes first.
 */
public final class DebugStickListener implements Listener {

    // Bedrock can send the same click several times in a row
    private static final long REPEAT_GUARD_NANOS = 200_000_000L;

    private final Access access;
    private final Map<UUID, Long> lastChange = new HashMap<>();
    private final Map<UUID, Long> lastSelect = new HashMap<>();

    DebugStickListener(Access access) {
        this.access = access;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        PlayerInteractEvent.Action action = event.getAction();
        if (action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        Item stick = event.getItem();
        if (!canUse(player, stick)) {
            return;
        }
        event.setCancelled(true);
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (!isRepeat(lastChange, player)) {
                changeValue(player, stick, event.getBlock());
            }
        } else if (!isRepeat(lastSelect, player)) {
            selectProperty(player, stick, event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Item stick = event.getItem();
        if (!canUse(player, stick)) {
            return;
        }
        event.setCancelled(true);
        // in survival this comes after mining finished; the click already selected
        if (player.isCreative() && !isRepeat(lastSelect, player)) {
            selectProperty(player, stick, event.getBlock());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastChange.remove(event.getPlayer().getUniqueId());
        lastSelect.remove(event.getPlayer().getUniqueId());
    }

    private static void changeValue(Player player, Item stick, Block block) {
        String selected = DebugStickItem.selectedProperty(stick);
        PropertyCycler.nextValue(block, selected, step(player)).ifPresent(change -> {
            apply(block, change);
            if (!change.property().equals(selected)) {
                DebugStickItem.selectProperty(player, stick, change.property());
            }
            showTip(player, change.property(), change.value());
        });
    }

    private static void selectProperty(Player player, Item stick, Block block) {
        PropertyCycler.nextProperty(block.getBlockState(), DebugStickItem.selectedProperty(stick), step(player))
                .ifPresent(selected -> {
                    String name = selected.getPropertyType().getName();
                    DebugStickItem.selectProperty(player, stick, name);
                    showTip(player, name, selected.getValue());
                });
    }

    // no block update, or fences and walls recalculate their connections right away
    private static void apply(Block block, PropertyCycler.Change change) {
        Level level = block.getLevel();
        int x = block.getFloorX();
        int y = block.getFloorY();
        int z = block.getFloorZ();
        level.setBlock(x, y, z, Block.get(change.state(), level, x, y, z), true, false);
    }

    private static void showTip(Player player, String property, Object value) {
        player.sendTip(property + ": " + value);
    }

    private static boolean isRepeat(Map<UUID, Long> last, Player player) {
        long now = System.nanoTime();
        Long previous = last.get(player.getUniqueId());
        if (previous != null && now - previous < REPEAT_GUARD_NANOS) {
            return true;
        }
        last.put(player.getUniqueId(), now);
        return false;
    }

    private boolean canUse(Player player, Item item) {
        return DebugStickItem.matches(item) && access.allows(player);
    }

    private static int step(Player player) {
        return player.isSneaking() ? -1 : 1;
    }
}
