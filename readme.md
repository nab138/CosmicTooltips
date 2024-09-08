# Cosmic Tooltips

A simple item tooltip mod for Cosmic Reach (Puzzle Loader).

Written very poorly because I have no experience with this kind of thing, so feel free to make fun of it.

## Features
- Tooltips when hovering over items in catalog and inventory/containers
- Press alt while hovering to see advanced info
- WAILA style display that shows the name and info of the block you're looking at

## Mod Developers:
*Puzzle Loader only*


If you would like to add custom tooltips, add this to the `dependencies` section of your build.gradle:
```gradle
mod files('libs/CosmicTooltips-1.2.4-bundle.jar')
```
You will need to place the jar in the libs folder in the root of your project.

### For items:
Implement `ITooltipItem` (one method, `String getTooltipText()`) on your item class (the one that impliments `IModItem`), and when alt is pressed it will show up

### For blocks:
Implement `ITooltipBlock` (one method, `String getTooltipText(BlockState blockState)`) on your block class (the one that impliments `IModBlock`), and when alt is pressed it will show up

## Credits

- [PuzzleLoader](https://github.com/PuzzleLoader/PuzzleLoader)
- [ExampleMod](https://github.com/PuzzleLoader/ExampleMod)
