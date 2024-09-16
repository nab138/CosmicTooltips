# Cosmic Tooltips

A simple item tooltip mod for Cosmic Reach (Puzzle Loader).

## Features
Why use this over the ones built into Cosmic Reach?
- Tooltips when hovering over items in catalog and inventory/containers
- Supports advanced tooltips with block id and blockstate info (Press f3 + h to toggle)
- Tooltips when selecting an item in the hotbar
- Fixes the spelling of aluminum `aluminium` -> `aluminum` (You can press f3 + b to undo this, if you hate yourself)
- WAILA style display that shows the name and info of the block you're looking at
- Can show custom data from mods

## Mod Developers:
*Puzzle Loader only*

This may seem complicated, but this is done to make sure that your mod will run fine without Cosmic Tooltips installed.

If you would like to add custom tooltips, add this to the `dependencies` section of your build.gradle:
```gradle
mod files('libs/CosmicTooltips-1.3.0-bundle.jar')
```
You will need to place the jar in the libs folder in the root of your project.

### For each item & block:
1. Create a new class that implements `ITooltipItem` or `ITooltipBlock` then implement it's methods.
2. Override `getItemID` for items or `getBlockID` for blocks to return the id of the item or block you want to add a tooltip to.
3. Override `getTooltipText(Stack stack)` to return the tooltip text for the item or block.

## To register your custom ToolTips
1. Create a new class that implements `ToolTipFactory`.
2. For every item or block you want to add tooltips to, put `addTooltip(new YOURCLASS)` in the constructor, where YOURCLASS implements `ITooltipItem` or `ITooltipBlock`.
3. In your mod's `puzzle.mod.json` under `entrypoints` add `"tooltip": []`.
4. Add the path of the class that implements `ToolTipFactory` to `"tooltip": []`.
5. Test your mod!

## Credits

- [PuzzleLoader](https://github.com/PuzzleLoader/PuzzleLoader)
- [ExampleMod](https://github.com/PuzzleLoader/ExampleMod)
- [SinfullySoul](https://github.com/SinfullySoul) for better custom mod tooltip support
