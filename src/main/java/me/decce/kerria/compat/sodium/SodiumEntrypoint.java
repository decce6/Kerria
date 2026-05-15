package me.decce.kerria.compat.sodium;

//? if sodium {
import me.decce.kerria.Constants;
import me.decce.kerria.Kerria;
import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.StorageEventHandler;
import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SodiumEntrypoint implements ConfigEntryPoint {
	private final StorageEventHandler handler = () -> Kerria.getConfig().save();

	private final ResourceLocation enabledOption = ResourceLocation.parse("kerria:enabled");

	@Override
	public void registerConfigLate(ConfigBuilder builder) {
		var optionsBuilder = builder
				.registerOwnModOptions()
				.setVersion(Constants.MOD_VERSION_SHORT)
				.setNonTintedIcon(ResourceLocation.parse("kerria:kerria.png"))
				.setColorTheme(builder.createColorTheme().setBaseThemeRGB(6510886));
		optionsBuilder.addPage(builder.createOptionPage()
				.setName(Component.literal("General"))
				.addOption(builder.createBooleanOption(enabledOption)
						.setName(Component.translatable("kerria.enabled"))
						.setTooltip(Component.translatable("kerria.enabled.tooltip"))
						.setImpact(OptionImpact.HIGH)
						.setStorageHandler(this.handler)
						.setDefaultValue(true)
						.setBinding((value) -> {
							Kerria.getConfig().enabled = value;
						}, () -> Kerria.getConfig().enabled))
				.addOption(builder.createBooleanOption(ResourceLocation.parse("kerria:fast_upload"))
						.setImpact(OptionImpact.HIGH)
						.setName(Component.translatable("kerria.fastUpload"))
						.setEnabledProvider(cs -> cs.readBooleanOption(enabledOption), enabledOption)
						.setStorageHandler(this.handler)
						.setDefaultValue(true)
						.setTooltip(Component.translatable("kerria.fastUpload.tooltip"))
						.setBinding((value) -> {
							Kerria.getConfig().fastUpload = value;
						}, () -> Kerria.getConfig().fastUpload))
				.addOption(builder.createBooleanOption(ResourceLocation.parse("kerria:cache"))
						.setImpact(OptionImpact.HIGH)
						.setName(Component.translatable("kerria.cache"))
						.setStorageHandler(this.handler)
						.setDefaultValue(true)
						.setTooltip(Component.translatable("kerria.cache.tooltip"))
						.setBinding((value) -> {
							Kerria.getConfig().cache = value;
						}, () -> Kerria.getConfig().cache))
				.addOption(builder.createIntegerOption(ResourceLocation.parse("kerria:buffer_size"))
						.setName(Component.translatable("kerria.bufferSize"))
						.setTooltip(Component.translatable("kerria.bufferSize.tooltip"))
						.setStorageHandler(this.handler)
						.setImpact(OptionImpact.VARIES)
						.setDefaultValue(Constants.DEFAULT_BUFFER_SIZE)
						.setRange(256, 16 * 1024, 32)
						.setValueFormatter(i -> Component.literal(String.valueOf(i)))
						.setBinding((value) -> {
							Kerria.getConfig().bufferSize = value * 1024;
							Kerria.recreateBuffer();
						}, () -> Kerria.getConfig().bufferSize / 1024))
				.addOption(builder.createIntegerOption(ResourceLocation.parse("kerria:min_cache_size"))
						.setName(Component.translatable("kerria.minCacheSize"))
						.setTooltip(Component.translatable("kerria.minCacheSize.tooltip"))
						.setStorageHandler(this.handler)
						.setDefaultValue(Constants.DEFAULT_MIN_CACHE_SIZE)
						.setRange(32, 4096, 32)
						.setValueFormatter(i -> Component.literal(String.valueOf(i)))
						.setImpact(OptionImpact.VARIES)
						.setBinding((value) -> {
							Kerria.getConfig().minCacheSize = value;
							Kerria.recreateCache();
						}, () -> Kerria.getConfig().minCacheSize))
				.addOption(builder.createIntegerOption(ResourceLocation.parse("kerria:max_cache_size"))
						.setName(Component.translatable("kerria.maxCacheSize"))
						.setStorageHandler(this.handler)
						.setDefaultValue(Constants.DEFAULT_MAX_CACHE_SIZE)
						.setRange(8192, 65536 * 16, 32)
						.setValueFormatter(i -> Component.literal(String.valueOf(i)))
						.setTooltip(Component.translatable("kerria.maxCacheSize.tooltip"))
						.setImpact(OptionImpact.VARIES)
						.setBinding((value) -> {
							Kerria.getConfig().maxCacheSize = value;
							Kerria.recreateCache();
						}, () -> Kerria.getConfig().maxCacheSize)
				));
	}
}
//?}
