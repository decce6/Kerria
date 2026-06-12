package me.decce.kerria.compat.sodium;

//? if sodium_legacy || embeddium_legacy {
import com.google.common.collect.ImmutableList;
import me.decce.kerria.Kerria;
import me.decce.kerria.VersionCompatUtils;
import net.caffeinemc.mods.sodium.client.gui.options.OptionGroup;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpact;
import net.caffeinemc.mods.sodium.client.gui.options.OptionImpl;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.caffeinemc.mods.sodium.client.gui.options.control.SliderControl;
import net.caffeinemc.mods.sodium.client.gui.options.control.TickBoxControl;
import net.caffeinemc.mods.sodium.client.gui.options.storage.OptionStorage;
import net.minecraft.client.resources.language.I18n;

import java.util.ArrayList;
import java.util.List;

public class LegacySodiumPage extends OptionPage {
	public LegacySodiumPage() {
		//? 1.16.5 && sodium_legacy {
		/*super("Kerria", groups());
		*///? } else {
		super(VersionCompatUtils.literal("Kerria"), groups());
		//? }
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
				//? 1.16.5 && sodium_legacy {
				/*.setName(I18n.get("kerria.enabled"))
				.setTooltip(I18n.get("kerria.enabled.tooltip"))
				*///? } else {
				.setName(VersionCompatUtils.translatable("kerria.enabled"))
				.setTooltip(VersionCompatUtils.translatable("kerria.enabled.tooltip"))
				//? }
				.setImpact(OptionImpact.HIGH)
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().enabled = value;
				}, opts -> Kerria.getConfig().enabled)
				.build();
		general.add(enabledOption);
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE), "kerria.fastUpload")
				.setImpact(OptionImpact.HIGH)
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().fastUpload = value;
				}, opts -> Kerria.getConfig().fastUpload)
				.build());
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE), "kerria.fastLightTextureUpload")
				.setImpact(OptionImpact.HIGH)
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().fastLightTextureUpload = value;
				}, opts -> Kerria.getConfig().fastLightTextureUpload)
				.build());
		general.add(configure(OptionImpl.createBuilder(boolean.class, STORAGE), "kerria.cache")
				.setImpact(OptionImpact.HIGH)
				.setControl(TickBoxControl::new)
				.setBinding((opts, value) -> {
					Kerria.getConfig().cache = value;
				}, opts -> Kerria.getConfig().cache)
				.build());
		general.add(configure(OptionImpl.createBuilder(int.class, STORAGE), "kerria.bufferSize")
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
	
	private static <T, R> OptionImpl.Builder<T, R> configure(OptionImpl.Builder<T, R> builder, String name) {
		//? >=1.21.1 {
		builder.setEnabled(enabledOption::getValue);
		//? }
		//? 1.16.5 && sodium_legacy {
		/*builder.setName(I18n.get(name));
		builder.setTooltip(I18n.get(name + ".tooltip"));
		*///? } else {
		builder.setName(VersionCompatUtils.translatable(name));
		builder.setTooltip(VersionCompatUtils.translatable(name + ".tooltip"));
		//? }
		return builder;
	}
}
//?}
