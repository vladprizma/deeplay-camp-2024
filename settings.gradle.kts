rootProject.name = "deeplay-camp-2024"
include("client", "controller", "data-base", "model", "services", "utilities", "view")

val clientModules = listOf("model", "view-model", "view", "services", "utilitiesClient")

for (module in clientModules)
    include(":client:$module")
