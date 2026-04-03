package studio.semicolon.prc.core.game;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class RandomSlotSelector {
    private final Set<Integer> excludedSlots;
    private final List<Predicate<ItemStack>> excludedPredicates;

    private RandomSlotSelector(Builder builder) {
        this.excludedSlots = ImmutableSet.copyOf(builder.excludedSlots);
        this.excludedPredicates = ImmutableList.copyOf(builder.excludedPredicates);
    }

    public OptionalInt select(Inventory inventory) {
        List<Integer> candidates = Lists.newArrayList();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item == null || item.getType().isAir()) continue;
            if (excludedSlots.contains(i)) continue;
            if (excludedPredicates.stream().anyMatch(p -> p.test(item))) continue;
            candidates.add(i);
        }

        if (candidates.isEmpty()) return OptionalInt.empty();
        return OptionalInt.of(candidates.get(ThreadLocalRandom.current().nextInt(candidates.size())));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Set<Integer> excludedSlots = Sets.newHashSet();
        private final List<Predicate<ItemStack>> excludedPredicates = Lists.newArrayList();

        private Builder() { }

        public Builder excludeSlot(int slot) {
            this.excludedSlots.add(slot);
            return this;
        }

        public Builder excludeSlots(int... slots) {
            for (int slot : slots) this.excludedSlots.add(slot);
            return this;
        }

        public Builder excludeIf(Predicate<ItemStack> predicate) {
            this.excludedPredicates.add(predicate);
            return this;
        }

        public RandomSlotSelector build() {
            return new RandomSlotSelector(this);
        }
    }
}
