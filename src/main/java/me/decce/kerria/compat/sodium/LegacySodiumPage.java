package me.decce.kerria.compat.sodium;

//? if sodium_legacy {
import com.google.common.collect.ImmutableList;
import me.decce.kerria.Kerria;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class LegacySodiumPage extends OptionPage {
	public LegacySodiumPage() {
		super(Component.literal("Kerria"), groups());
	}

	private static OptionImpl<?, Boolean> enabledOption;

	public static final OptionStorage<?> STORAGE = new OptionStorage<>() {
		@Override
		public Object getData() {
			return new Object();
		}

		@Override
		public void save() {
			Kerria.getConfig().save();
		}
	};
	
	private static ImmutableList<OptionGroup> groups() {
		List<OptionGroup> groups = new ArrayList<>();

		var general = OptionGroup.createBuilder();

		enabledOption = OptionImpl.createBuilder(boolean.class, STORAGE)
				.setName(Component.translatable("kerria.enabled"))
				.setTooltip(Component.translatable("kerria.enabled.tooltip"))
				.setImpact(OptionImpact.HIGH)
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().enabled = value;
				}, opts -> Kerria.getConfig().enabled)
				.build();
		general.add(enabledOption);
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE))
				.setImpact(OptionImpact.HIGH)
				.setName(Component.translatable("kerria.fastUpload"))
				.setTooltip(Component.translatable("kerria.fastUpload.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().fastUpload = value;
				}, opts -> Kerria.getConfig().fastUpload)
				.build());
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE))
				.setImpact(OptionImpact.HIGH)
				.setName(Component.translatable("kerria.fastLightTextureUpload"))
				.setTooltip(Component.translatable("kerria.fastLightTextureUpload.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().fastLightTextureUpload = value;
				}, opts -> Kerria.getConfig().fastLightTextureUpload)
				.build());
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE))
				.setImpact(OptionImpact.HIGH)
				.setName(Component.translatable("kerria.cache"))
				.setTooltip(Component.translatable("kerria.cache.tooltip"))
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().cache = value;
				}, opts -> Kerria.getConfig().cache)
				.build());
		general.add(configure(OptionImpl.createBuilder(int.class, STORAGE))
				.setName(Component.translatable("kerria.bufferSize"))
				.setTooltip(Component.translatable("kerria.bufferSize.tooltip"))
				.setImpact(OptionImpact.VARIES)
				.setControl(option -> new SliderControl(option, 256, 16 * 1024, 32, ControlValueFormatter.number()))
				.setBinding((opts, value) -> {
					Kerria.getConfig().bufferSize = value * 1024;
					Kerria.recreateBuffer();
				}, opts -> Kerria.getConfig().bufferSize / 1024)
				.build());

		groups.add(general.build());

		return ImmutableList.copyOf(groups);
	}
	
	private static <T, R> OptionImpl.Builder<T, R> configure(OptionImpl.Builder<T, R> builder) {
		//? >=1.21.1 {
		builder.setEnabled(enabledOption::getValue);
		//? }
		return builder;
	}
}
//?}
