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

### For items & blocks:
1. Create a new class that implements `ITooltipItem` or `ITooltipBlock` then implements it's methods.
2. Override at `getItemID` for items or `getBlockID` for blocks, method will let CosmicTooltip to know what item this additional Text go to.
3. Override at `getTooltipText(Stack stack)` method will allow return text of whatever you want.

## To register your custom ToolTips
1. Create a new class that implements `ToolTipFactory`.
2. Create your class constructor.
3. In your constructor add `addTooltip(new YOUR CLASS)` in it, where YOUR CLASS has to implements `ITooltipItem` or `ITooltipBlock`;
4. In your mod `puzzle.mod.json` under `entrypoints` add `"tooltip": []`.
5. Now, we need to add the path of the class that implements `ToolTipFactory` to `"tooltip": []`, and now run your mod!

## Credits

- [PuzzleLoader](https://github.com/PuzzleLoader/PuzzleLoader)
- [ExampleMod](https://github.com/PuzzleLoader/ExampleMod)
