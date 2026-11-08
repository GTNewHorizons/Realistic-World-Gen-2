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
- Optional north-south temperature and east-west humidity climate bands
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
- Climate bands
- Biome and landmass placement offsets for migrating existing worlds
- Worldgen diagnostics

## Climate Bands

Optional replacement for random climate placement, off by default. Enable with `Enable Climate Bands` in the `climate bands` config section. Applies to both world types. Changing these settings after creating a world causes borders between old and new chunks.

From `Z = 0`, going north passes through cold then snow, and going south passes through hot. Bands mirror and repeat every snow + 2 x cold + hot blocks. Hot and wet alternate east to west within the hot band. Humidity does not affect snow or cold. `Biome Placement Offset X/Z` moves the origin.

| Setting | Default (blocks) | Effect |
| --- | --- | --- |
| Snow Band Width | 6000 | North-south width of snow |
| Cold Band Width | 6000 | North-south width of cold |
| Hot Band Width | 12000 | North-south width of the band shared by hot and wet |
| Humidity Band Width | 6000 | East-west width of each dry and wet column |
| Blend Width | 4000 | Width of the zone where climates interleave at a boundary |
| Boundary Warp Strength | 1500 | Small-scale waviness of boundaries |
| Band Drift Strength | 6000 | Large-scale bending and local width variation; 0 keeps bands straight |
| Band Drift Scale | 16000 | Distance over which drift changes; lower bends more tightly |

## Development

- `./gradlew runPreview`: offline `RWG2_CONTINENTS` debug map; reads `run/client/config/rwg2.cfg`, or pass `--args="--config=/absolute/path/to/rwg2.cfg"`
- `./gradlew calibrateOceanCoverage`: sample ocean coverage for current continent settings
- `./gradlew checkClimateBands`: check climate band placement logic
