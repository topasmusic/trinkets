package dev.emi.trinkets;

import dev.emi.trinkets.api.SlotType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A gui slot for a trinket slot in the creative inventory
 */
public class CreativeTrinketSlot extends Slot implements TrinketSlot {
	private final SurvivalTrinketSlot original;

	public CreativeTrinketSlot(SurvivalTrinketSlot original, int s, int x, int y) {
		super(original.container, s, x, y);
		this.original = original;
	}

	@Override
	public void onTake(Player player, ItemStack stack) {
		original.onTake(player, stack);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return original.mayPlace(stack);
	}

	@Override
	public ItemStack getItem() {
		return original.getItem();
	}

	@Override
	public boolean hasItem() {
		return original.hasItem();
	}

	@Override
	public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
		original.setByPlayer(newStack, oldStack);
	}

	@Override
	public void set(ItemStack stack) {
		original.set(stack);
	}

	@Override
	public void setChanged() {
		original.setChanged();
	}

	@Override
	public int getMaxStackSize() {
		return original.getMaxStackSize();
	}

	@Override
	public int getMaxStackSize(ItemStack stack) {
		return original.getMaxStackSize(stack);
	}

	@Override
	public Identifier getNoItemIcon() {
		return original.getNoItemIcon();
	}

	@Override
	public ItemStack remove(int amount) {
		return original.remove(amount);
	}

	@Override
	public boolean mayPickup(Player player) {
		return original.mayPickup(player);
	}

	@Override
	public boolean isActive() {
		return original.isActive();
	}

	@Override
	public boolean isTrinketFocused() {
		return original.isTrinketFocused();
	}

	@Override
	public boolean renderAfterRegularSlots() {
		return original.renderAfterRegularSlots();
	}

	@Override
	public Identifier getBackgroundIdentifier() {
		return original.getBackgroundIdentifier();
	}

	@Override
	public SlotType getType() {
		return original.getType();
	}
}
