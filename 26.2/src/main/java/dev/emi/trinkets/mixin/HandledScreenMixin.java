package dev.emi.trinkets.mixin;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.emi.trinkets.CreativeTrinketScreen;
import dev.emi.trinkets.TrinketScreenManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.trinkets.TrinketSlot;
import dev.emi.trinkets.TrinketsClient;
import dev.emi.trinkets.mixin.accessor.CreativeSlotAccessor;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;

/**
 * Draws trinket slot backs, adjusts z location of draw calls, and makes non-trinket slots un-interactable while a trinket slot group is focused
 * 
 * @author Emi
 */
@Mixin(AbstractContainerScreen.class)
public abstract class HandledScreenMixin extends Screen {
	@Shadow @Nullable protected Slot hoveredSlot;
	@Shadow @Final private static Identifier SLOT_HIGHLIGHT_BACK_SPRITE;
	@Unique
	private static final Identifier MORE_SLOTS = Identifier.fromNamespaceAndPath("trinkets", "textures/gui/more_slots.png");
	@Unique
	private static final Identifier BLANK_BACK = Identifier.fromNamespaceAndPath("trinkets", "textures/gui/blank_back.png");

	private HandledScreenMixin() {
		super(null);
	}

	@Inject(at = @At("HEAD"), method = "removed")
	private void removed(CallbackInfo info) {
		if ((Object)this instanceof InventoryScreen) {
			TrinketScreenManager.removeSelections();
		}
	}

	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractSlot(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/inventory/Slot;II)V"),
			method = "extractSlots")
	private boolean preventDrawingSlots(AbstractContainerScreen instance, GuiGraphicsExtractor context, Slot slot, int mouseX, int mouseY) {
		return !(slot instanceof TrinketSlot trinketSlot) || !trinketSlot.renderAfterRegularSlots();
	}

	@Inject(at = @At("HEAD"), method = "extractSlot")
	private void drawSlotBackground(GuiGraphicsExtractor context, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
		if (slot instanceof TrinketSlot ts) {
			assert this.minecraft != null;
			Identifier slotTextureId = ts.getBackgroundIdentifier();

			if (!slot.getItem().isEmpty() || slotTextureId == null) {
				slotTextureId = BLANK_BACK;
			}

			if (ts.isTrinketFocused()) {
				context.blit(RenderPipelines.GUI_TEXTURED, slotTextureId, slot.x, slot.y, 0, 0, 16, 16, 16, 16);
				if (this.hoveredSlot == slot && this.hoveredSlot.isHighlightable()) {
					context.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 24, 24);
				}
			} else {
				context.blit(RenderPipelines.GUI_TEXTURED, slotTextureId, slot.x, slot.y, 0, 0, 16, 16, 16, 16);
				context.blit(RenderPipelines.GUI_TEXTURED, MORE_SLOTS, slot.x - 1, slot.y - 1, 4, 4, 18, 18, 256, 256);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", shift = At.Shift.AFTER), method = "extractRenderState")
	private void renderCreativeSlots(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
		if (this instanceof CreativeTrinketScreen screen) {
			screen.trinkets$renderCreative(context, mouseX, mouseY, deltaTicks);
		}
	}

	@Inject(at = @At("TAIL"), method = "extractSlots")
	private void normalizeHoveredSlotAfterExtract(GuiGraphicsExtractor context, int mouseX, int mouseY, CallbackInfo info) {
		trinkets$normalizeHoveredSlot();
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked")
	private void normalizeHoveredSlotBeforeClick(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> info) {
		trinkets$normalizeHoveredSlot();
	}

	@Unique
	private void trinkets$normalizeHoveredSlot() {
		if (TrinketsClient.activeGroup == null || this.hoveredSlot == null) {
			return;
		}
		if (this.hoveredSlot instanceof TrinketSlot trinketSlot) {
			if (!trinketSlot.isTrinketFocused()) {
				this.hoveredSlot = null;
			}
			return;
		}
		if (this.hoveredSlot.getClass().getName().endsWith("$SlotWrapper")) {
			if (((CreativeSlotAccessor) this.hoveredSlot).getSlot().index != TrinketsClient.activeGroup.getSlotId()) {
				this.hoveredSlot = null;
			}
			return;
		}
		if (this.hoveredSlot.index != TrinketsClient.activeGroup.getSlotId()) {
			this.hoveredSlot = null;
		}
	}
}
