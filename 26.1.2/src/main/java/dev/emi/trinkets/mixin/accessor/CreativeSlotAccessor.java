package dev.emi.trinkets.mixin.accessor;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * You'll access widen this into being accessible but won't make its field accessible? Yes.
 */
@Mixin(targets = "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$SlotWrapper")
public interface CreativeSlotAccessor {
	
	@Accessor("target")
	public Slot getSlot();
}
