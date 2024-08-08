pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.0"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "deeplay-camp-2024"
include("client", "utilities", "reversi-server")

val serverModules = listOf("controller", "data-base", "services", "model")

for (module in serverModules)
    include(":reversi-server:$module")

val clientModules = listOf("model", "view-model", "view", "services", "utilitiesClient")

for (module in clientModules)
    include(":client:$module")
include("client:storage")
findProject(":client:storage")?.name = "storage"
