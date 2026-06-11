plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.1-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge", "forge")
    constants["sodium"] = node.project.hasProperty("deps.sodium")
    constants["sodium_legacy"] = node.project.hasProperty("deps.sodium_legacy")
    constants["embeddium"] = node.project.hasProperty("deps.embeddium")
    constants["xycraft"] = node.project.hasProperty("deps.xycraft")
    swaps["mod_version_short"] = "\"" + property("mod_version") + "\";"
    replacements.string(current.parsed >= "1.21.11") {
        replace("com.mojang.blaze3d.platform.GlStateManager", "com.mojang.blaze3d.opengl.GlStateManager")
    }
    replacements.string(current.parsed >= "1.21.1") {
        replace("me.jellysquid", "net.caffeinemc")
    }
    replacements.string(current.parsed >= "1.21.1") {
        replace("new ResourceLocation", "ResourceLocation.fromNamespaceAndPath")
    }
}

tasks.register("publishAll") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods"))
}

tasks.register("publishAllModrinth") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishModrinth"))
}

tasks.register("publishAllCurseForge") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishCurseforge"))
}

stonecutter.tasks {
    order("publishMods")
    order("publishModrinth")
    order("publishCurseforge")
}

