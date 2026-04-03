import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("me.decce.kerria.gradle.kerria-common-conventions")
    id("dev.architectury.loom") version "1.13-SNAPSHOT"
    id("com.gradleup.shadow")
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(name: String) = if (hasProperty(name)) findProperty(name) as String else throw IllegalArgumentException("$name not found")

// Need to shadow MixinExtras in <1.18.2
val jijMixinExtras = stonecutter.eval(stonecutter.current.version, ">=1.18.2")

val modid = prop("modid")

dependencies {
    minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")
    mappings(loom.officialMojangMappings())
    forge("net.minecraftforge:forge:${prop("deps.forge")}")

    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.3")
    implementation("io.github.llamalad7:mixinextras-common:0.5.3")
    if (jijMixinExtras) {
        include("io.github.llamalad7:mixinextras-forge:0.5.3")
        implementation("io.github.llamalad7:mixinextras-forge:0.5.3")
    }
    else {
        shade("io.github.llamalad7:mixinextras-common:0.5.3")
    }

    if (hasProperty("deps.sodium")) {
        compileOnly("${prop("deps.sodium")}")
    }
    if (hasProperty("deps.embeddium")) {
        compileOnly("maven.modrinth:embeddium:${prop("deps.embeddium")}")
    }
}

tasks {
    named<Jar>("jar") {
        archiveClassifier = "slim"
    }

    named<ShadowJar>("shadowJar") {
        if (!jijMixinExtras) {
            relocate("com.llamalad7.mixinextras", "me.decce.$modid)}.shadow.mixinextras")
        }
    }

    named<RemapJarTask>("remapJar") {
        dependsOn(shadowJar)
        inputFile = shadowJar.flatMap { it.archiveFile }
        archiveClassifier = ""
        manifest.attributes (
            "MixinConfigs" to "$modid.mixins.json"
        )
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        dependsOn(remapJar)
        from(remapJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs"))
    }
}

publishMods {
    file = tasks.remapJar.get().archiveFile
}