package byakkono.debugstick;

import org.powernukkitx.block.Block;
import org.powernukkitx.block.BlockState;
import org.powernukkitx.block.property.type.BlockPropertyType;
import org.powernukkitx.block.property.type.BlockPropertyType.BlockPropertyValue;

import java.util.List;
import java.util.Optional;

final class PropertyCycler {

    record Change(BlockState state, String property, Object value) {
    }

    private PropertyCycler() {
    }

    /** The property after {@code selected} in the block's property list, or the first one if nothing matches. */
    static Optional<BlockPropertyValue<?, ?, ?>> nextProperty(BlockState state, String selected, int step) {
        List<BlockPropertyValue<?, ?, ?>> values = state.getBlockPropertyValues();
        if (values.isEmpty()) {
            return Optional.empty();
        }
        int index = indexOf(values, selected);
        return Optional.of(values.get(index < 0 ? 0 : Math.floorMod(index + step, values.size())));
    }

    /** The block state with the selected property (or the first one) moved to its next valid value. */
    static Optional<Change> nextValue(Block block, String selected, int step) {
        BlockState state = block.getBlockState();
        List<BlockPropertyValue<?, ?, ?>> values = state.getBlockPropertyValues();
        if (values.isEmpty()) {
            return Optional.empty();
        }
        BlockPropertyValue<?, ?, ?> current = values.get(Math.max(indexOf(values, selected), 0));
        BlockPropertyType<?> type = current.getPropertyType();
        List<?> valid = type.getValidValues();
        if (valid.isEmpty()) {
            return Optional.empty();
        }
        Object next = valid.get(Math.floorMod(Math.max(valid.indexOf(current.getValue()), 0) + step, valid.size()));
        BlockState changed = state.setPropertyValue(block.getProperties(), type.tryCreateValue(next));
        return Optional.of(new Change(changed, type.getName(), next));
    }

    private static int indexOf(List<BlockPropertyValue<?, ?, ?>> values, String name) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getPropertyType().getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
