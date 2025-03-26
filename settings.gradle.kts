rootProject.name = "tribot-script-template"

include("libraries:framework")
include("scripts:woodcuttingLumbridge")
include("scripts:ironSmelting")
include("scripts:cowKiller")
include("scripts:miningFull")
include("scripts:f2pRestricted")
include("scripts:blueDye")
include("scripts:logoutManager")
include("scripts:escapeF2P")
include("scripts:SMCowKiller")

include("scripts")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}