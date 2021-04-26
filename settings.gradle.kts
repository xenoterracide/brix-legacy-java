rootProject.name = "brix"
enableFeaturePreview("VERSION_CATALOGS")
include(
  "util",
  "cli:api",
  "config-loader:spi",
  "config-loader:api",
  "config-loader:yaml",
  "app"
)
