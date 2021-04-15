rootProject.name = "brix"
enableFeaturePreview("ONE_LOCKFILE_PER_PROJECT")

include(
  "cli:api",
  "configLoader:api",
  "configLoader:yaml",
  "app"
)
