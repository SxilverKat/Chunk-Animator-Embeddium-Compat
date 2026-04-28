# Chunk Animator Embeddium Compat

Chunk Animator Embeddium Compat allows the Chunk Animator mod to work with the Embeddium mod.

Chunk Animator crashes on startup when Embeddium is installed. Embeddium replaces vanilla's chunk-rendering pipeline with its own GPU-instanced per-region rendering. This makes Chunk Animator's mixin unable to find the vanilla call to hook to.

A Mixin plugin stops Chunk Animator's incompatible LevelRenderer mixin at load time when Embeddium is detected. New chunks are tracked, and their per-section offset is applied during Embeddium's render pass via a separate draw per animating chunk. The fast batched path still runs for every static section.

This has been tested with multiple shaders and Sodium/Embeddium add-ons.

## Configuration

- `enabled` — The master toggle.
- `animationDurationMs` — This is how long each chunk's animation lasts.
- `startOffsetX` / `startOffsetY` / `startOffsetZ` — Control the direction and distance to slide from.
- `easing` — This is animation curve options. `LINEAR`, `QUAD_OUT`, `CUBIC_OUT`, `QUART_OUT`, `QUINT_OUT`, `SINE_OUT`, `EXPO_OUT`, `CIRC_OUT`, `BACK_OUT`, `BOUNCE_OUT`.
- `disableAroundPlayer` + `playerRadius` — This suppress animations near the player.

## Requirements

- [Chunk Animator](https://www.curseforge.com/minecraft/mc-mods/chunk-animator) by Lumien231
- [Embeddium](https://www.curseforge.com/minecraft/mc-mods/embeddium) by FiniteReality

## Notes

This mod is client side only. Servers don't need it.

This mod replaces Chunk Animator's render hook with its own; you don't need to remove Chunk Animator. Both need to be installed.

## Links

- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/chunk-animator-embeddium-compat)

## License

MIT — see [LICENSE](LICENSE).
