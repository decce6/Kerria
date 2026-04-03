plugins {
    id("me.decce.kerria.gradle.kerria-common-conventions")
    id("net.neoforged.moddev.legacyforge") version "2.0.141"
    id("com.gradleup.shadow")
    id("me.modmuss50.mod-publish-plugin")
}

fun prop(name: String) = if (hasProperty(name)) findProperty(name) as String else throw IllegalArgumentException("$name not found")

legacyForge {
    version = prop("deps.forge")
}

dependencies {
    if (hasProperty("deps.sodium")) {
        compileOnly("${prop("deps.sodium")}")
    }
    if (hasProperty("deps.embeddium")) {
        compileOnly("maven.modrinth:embeddium:${prop("deps.embeddium")}")
    }

    jarJar("io.github.llamalad7:mixinextras-forge:0.5.3")
    implementation("io.github.llamalad7:mixinextras-common:0.5.3")

    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
}

val jijShadowJar = tasks.register<Jar>("jijShadowJar") {
    from(zipTree(tasks.shadowJar.map { it.archiveFile }))
    dependsOn(tasks.shadowJar)

    from(tasks.jarJar)
    dependsOn(tasks.jarJar)

    manifest.attributes("MixinConfigs" to "kerria.mixins.json")
}

mixin {
    val refmapFile = add(sourceSets["main"], "kerria.mixins.refmap.json")
    config("kerria.mixins.json")
    jijShadowJar.get().from(refmapFile) // Required for including the refmap in the built jar!
}

tasks {
    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    named<Jar>("jar") {
        archiveClassifier = "slim"
    }

    assemble.get().dependsOn(jijShadowJar)

    register<Copy>("buildAndCollect") {
        group = "build"
        dependsOn(jijShadowJar)
        from(jijShadowJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs"))
    }
}

publishMods {
    file = jijShadowJar.get().archiveFile
}