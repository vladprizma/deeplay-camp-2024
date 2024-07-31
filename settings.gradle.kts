pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "deeplay-camp-2024"
include("client", "controller", "data-base", "model", "services", "utilities", "view")

val clientModules = listOf("model", "view-model", "view", "services", "utilitiesClient")

for (module in clientModules)
    include(":client:$module")
