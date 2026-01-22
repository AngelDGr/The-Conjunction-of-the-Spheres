package mors.tcots.utils.tooltip;

import com.google.common.collect.ImmutableList;
//import com.simibubi.create.foundation.utility.CreateLang;

import mors.tcots.utils.tooltip.FontHelper.Palette;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

//import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public record ItemDescription(ImmutableList<Component> lines, ImmutableList<Component> linesOnShift,
							  ImmutableList<Component> linesOnCtrl) {

	public static void create(final Item item, final Palette palette) {

	}
//	private static final Map<Item, Supplier<String>> CUSTOM_TOOLTIP_KEYS = new IdentityHashMap<>();
//
//	@Nullable
//	public static ItemDescription create(final Item item, final Palette palette) {
//		return create(getTooltipTranslationKey(item), palette);
//	}
//
//	@Nullable
//	public static ItemDescription create(final String translationKey, final Palette palette) {
//		if (!canFillBuilder(translationKey + ".summary")) {
//			return null;
//		}
//
//		final Builder builder = new Builder(palette);
//		fillBuilder(builder, translationKey);
//		return builder.build();
//	}
//
//	public static boolean canFillBuilder(final String translationKey) {
//		return I18n.exists(translationKey);
//	}
//
//	public static void fillBuilder(final Builder builder, final String translationKey) {
//		// Summary
//		final String summaryKey = translationKey + ".summary";
//		if (I18n.exists(summaryKey)) {
//			builder.addSummary(I18n.get(summaryKey));
//		}
//
//		// Behaviours
//		for (int i = 1; i < 100; i++) {
//			final String conditionKey = translationKey + ".condition" + i;
//			final String behaviourKey = translationKey + ".behaviour" + i;
//			if (!I18n.exists(conditionKey))
//				break;
//			builder.addBehaviour(I18n.get(conditionKey), I18n.get(behaviourKey));
//		}
//
//		// Actions
//		for (int i = 1; i < 100; i++) {
//			final String controlKey = translationKey + ".control" + i;
//			final String actionKey = translationKey + ".action" + i;
//			if (!I18n.exists(controlKey))
//				break;
//			builder.addAction(I18n.get(controlKey), I18n.get(actionKey));
//		}
//	}
//
//	public static void useKey(final Item item, final Supplier<String> supplier) {
//		CUSTOM_TOOLTIP_KEYS.put(item, supplier);
//	}
//
//	public static void useKey(final ItemLike item, final String string) {
//		useKey(item.asItem(), () -> string);
//	}
//
//	public static void referKey(final ItemLike item, final Supplier<? extends ItemLike> otherItem) {
//		useKey(item.asItem(), () -> otherItem.get()
//			.asItem()
//			.getDescriptionId());
//	}
//
//	public static String getTooltipTranslationKey(final Item item) {
//		if (CUSTOM_TOOLTIP_KEYS.containsKey(item)) {
//			return CUSTOM_TOOLTIP_KEYS.get(item).get() + ".tooltip";
//		}
//		return item.getDescriptionId() + ".tooltip";
//	}
//
//	public ImmutableList<Component> getCurrentLines() {
//		if (Screen.hasShiftDown()) {
//			return linesOnShift;
//		} else if (Screen.hasControlDown()) {
//			return linesOnCtrl;
//		} else {
//			return lines;
//		}
//	}
//
//	public static class Builder {
//		protected final Palette palette;
//		protected final List<String> summary = new ArrayList<>();
//		protected final List<Pair<String, String>> behaviours = new ArrayList<>();
//		protected final List<Pair<String, String>> actions = new ArrayList<>();
//
//		public Builder(final Palette palette) {
//			this.palette = palette;
//		}
//
//		public Builder addSummary(final String summaryLine) {
//			summary.add(summaryLine);
//			return this;
//		}
//
//		public Builder addBehaviour(final String condition, final String behaviour) {
//			behaviours.add(Pair.of(condition, behaviour));
//			return this;
//		}
//
//		public Builder addAction(final String condition, final String action) {
//			actions.add(Pair.of(condition, action));
//			return this;
//		}
//
//		public ItemDescription build() {
//			final List<Component> lines = new ArrayList<>();
//			final List<Component> linesOnShift = new ArrayList<>();
//			final List<Component> linesOnCtrl = new ArrayList<>();
//
//			for (final String summaryLine : summary) {
//				linesOnShift.addAll(TooltipHelper.cutStringTextComponent(summaryLine, palette));
//			}
//
//			if (!behaviours.isEmpty()) {
//				linesOnShift.add(CommonComponents.EMPTY);
//			}
//
//			for (final Pair<String, String> behaviourPair : behaviours) {
//				final String condition = behaviourPair.getLeft();
//				final String behaviour = behaviourPair.getRight();
//				linesOnShift.add(Component.literal(condition).withStyle(GRAY));
//				linesOnShift.addAll(TooltipHelper.cutStringTextComponent(behaviour, palette.primary(), palette.highlight(), 1));
//			}
//
//			for (final Pair<String, String> actionPair : actions) {
//				final String condition = actionPair.getLeft();
//				final String action = actionPair.getRight();
//				linesOnCtrl.add(Component.literal(condition).withStyle(GRAY));
//				linesOnCtrl.addAll(TooltipHelper.cutStringTextComponent(action, palette.primary(), palette.highlight(), 1));
//			}
//
//			final boolean hasDescription = !linesOnShift.isEmpty();
//			final boolean hasControls = !linesOnCtrl.isEmpty();
//
//			if (hasDescription || hasControls) {
//				final String[] holdDesc = CreateLang.translateDirect("tooltip.holdForDescription", "$")
//					.getString()
//					.split("\\$");
//				final String[] holdCtrl = CreateLang.translateDirect("tooltip.holdForControls", "$")
//					.getString()
//					.split("\\$");
//				final MutableComponent keyShift = CreateLang.translateDirect("tooltip.keyShift");
//				final MutableComponent keyCtrl = CreateLang.translateDirect("tooltip.keyCtrl");
//				for (final List<Component> list : Arrays.asList(lines, linesOnShift, linesOnCtrl)) {
//					final boolean shift = list == linesOnShift;
//					final boolean ctrl = list == linesOnCtrl;
//
//					if (holdDesc.length != 2 || holdCtrl.length != 2) {
//						list.add(0, Component.literal("Invalid lang formatting!"));
//						continue;
//					}
//
//					if (hasControls) {
//						final MutableComponent tabBuilder = Component.empty();
//						tabBuilder.append(Component.literal(holdCtrl[0]).withStyle(DARK_GRAY));
//						tabBuilder.append(keyCtrl.plainCopy()
//							.withStyle(ctrl ? WHITE : GRAY));
//						tabBuilder.append(Component.literal(holdCtrl[1]).withStyle(DARK_GRAY));
//						list.add(0, tabBuilder);
//					}
//
//					if (hasDescription) {
//						final MutableComponent tabBuilder = Component.empty();
//						tabBuilder.append(Component.literal(holdDesc[0]).withStyle(DARK_GRAY));
//						tabBuilder.append(keyShift.plainCopy()
//							.withStyle(shift ? WHITE : GRAY));
//						tabBuilder.append(Component.literal(holdDesc[1]).withStyle(DARK_GRAY));
//						list.add(0, tabBuilder);
//					}
//
//					if (shift || ctrl)
//						list.add(hasDescription && hasControls ? 2 : 1, CommonComponents.EMPTY);
//				}
//			}
//
//			if (!hasDescription) {
//				linesOnCtrl.clear();
//				linesOnShift.addAll(lines);
//			}
//			if (!hasControls) {
//				linesOnCtrl.clear();
//				linesOnCtrl.addAll(lines);
//			}
//
//			return new ItemDescription(ImmutableList.copyOf(lines), ImmutableList.copyOf(linesOnShift), ImmutableList.copyOf(linesOnCtrl));
//		}
//	}
//
//	public static class Modifier implements TooltipModifier {
//		protected final Item item;
//		protected final Palette palette;
//		protected String cachedLanguage;
//		protected ItemDescription description;
//
//		public Modifier(final Item item, final Palette palette) {
//			this.item = item;
//			this.palette = palette;
//		}
//
//		@Override
//		public void modify(final ItemTooltipEvent context) {
//			if (checkLocale()) {
//				description = create(item, palette);
//			}
//			if (description == null) {
//				return;
//			}
//			context.getToolTip().addAll(1, description.getCurrentLines());
//		}
//
//		protected boolean checkLocale() {
//			final String currentLanguage = Minecraft.getInstance()
//				.getLanguageManager()
//				.getSelected();
//			if (!currentLanguage.equals(cachedLanguage)) {
//				cachedLanguage = currentLanguage;
//				return true;
//			}
//			return false;
//		}
//	}
}
