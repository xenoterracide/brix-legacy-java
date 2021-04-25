rootProject.name = "brix"
enableFeaturePreview("ONE_LOCKFILE_PER_PROJECT")

include(
  "util",
  "cli:api",
  "config-loader:spi",
  "config-loader:api",
  "config-loader:yaml",
  "app"
)
