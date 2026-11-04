# Realistic World Gen 2

World generation mod for Minecraft 1.7.10 / GT New Horizons.

Requires EndlessIDs and GTNHLib.

## World Types

| | RWG2 Smooth (`RWG2_CONTINENTS`) | Realistic RC1 (`RWG2`) |
| --- | --- | --- |
| Landmass | Continents and islands from a distance field | No continents or islands |
| Coast terrain | Follows continent coastlines | Perlin ocean noise |
| Biomes | Land, ocean, island and coastal | Land only |
| Volcanoes | Yes | No |
| Lava caves | Yes | No |
| Geometry config | Yes | No |
| Landmass offsets | Yes | No |
| `runPreview` map | Yes | No |

Both share climate-based land biomes, biome size and biome offset settings, rivers, mountain river caves and river junction landmarks.

## Features

- Configurable continents, oceans and islands
- Climate-based biome placement with adjustable biome size
- Rivers blended into terrain
- Volcanoes on continents and large islands
- Lava cave and river junction landmarks
- Mountain river caves
- Toggles for caves, mineshafts, villages, underground lakes, emeralds and boulders

## Mod Support

- **Biomes:** Biomes O' Plenty, ExtrabiomesXL, Thaumcraft, ChromatiCraft
- **Twilight Forest:** portals on river junction landmarks
- **LootGames:** puzzles on river junction landmarks
- **Et Futurum Requiem:** cave vines and landmark decorations
- **Natura:** landmark glowshrooms
- **Witchery:** protected standalone structures
- **Railcraft:** abyssal geodes kept off ocean floors

## Commands

- `/rwg_noise get|perlin|opensimplex`: get or set noise type for upgraded worlds
- `/rwg_buginfo`: explain noise type choice for worlds from GTNH 2.2.0.0
- `/speed <0-10>`: set flight speed multiplier (can be disabled in config)

## Config

Editable in-game through the mod config screen:

- Biome IDs
- Feature toggles
- Continent, ocean and island geometry
- Landmark decorations
- Biome and landmass placement offsets for migrating existing worlds
- Worldgen diagnostics

## Development

- `./gradlew runPreview`: offline `RWG2_CONTINENTS` debug map
- `./gradlew calibrateOceanCoverage`: sample ocean coverage for current continent settings
