package byakkono.debugstick;

import org.powernukkitx.Player;
import org.powernukkitx.inventory.HumanInventory;
import org.powernukkitx.item.Item;
import org.powernukkitx.nbt.tag.CompoundTag;

final class DebugStickItem {

    private static final String ITEM_ID = "minecraft:stick";
    private static final String MARKER = "byakkono_debugstick";
    private static final String SELECTED = "byakkono_debugstick_property";

    private final Messages messages;

    DebugStickItem(Messages messages) {
        this.messages = messages;
    }

    boolean giveTo(Player player) {
        HumanInventory inventory = player.getInventory();
        Item held = inventory.getItemInMainHand();
        if (!held.isNull() && !matches(held)) {
            int slot = firstEmptyStorageSlot(inventory);
            if (slot < 0) {
                return false;
            }
            inventory.setItem(slot, held);
        }
        inventory.setItemInMainHand(create(player));
        return true;
    }

    static boolean matches(Item item) {
        return item != null && ITEM_ID.equals(item.getId()) && item.hasNbt() && item.getNbt().containsByte(MARKER);
    }

    static String selectedProperty(Item stick) {
        return stick.hasNbt() ? stick.getNbt().getString(SELECTED) : "";
    }

    static void selectProperty(Player player, Item stick, String property) {
        CompoundTag tag = stick.getOrCreateNbt();
        tag.putString(SELECTED, property);
        stick.setNbt(tag);
        player.getInventory().setItemInMainHand(stick);
    }

    private Item create(Player player) {
        Item stick = Item.get(ITEM_ID);
        CompoundTag tag = stick.getOrCreateNbt();
        tag.putByte(MARKER, 1);
        stick.setNbt(tag);
        stick.setCustomName(messages.text(player, Messages.Key.ITEM_NAME));
        stick.setLore(messages.lines(player, Messages.Key.ITEM_LORE).toArray(String[]::new));
        return stick;
    }

    // Inventory#firstEmpty also returns armor slots
    private static int firstEmptyStorageSlot(HumanInventory inventory) {
        for (int slot = 0; slot < HumanInventory.ARMORS_INDEX; slot++) {
            if (inventory.getItem(slot).isNull()) {
                return slot;
            }
        }
        return -1;
    }
}
