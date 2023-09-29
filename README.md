# SlimeHUD

Adds a WAILA (What Am I Looking At) HUD for Slimefun items. Can be set to display in the bossbar or above the hotbar in `config.yml`. Additional energy/cargo information can also be toggled on or off in `config.yml`. Individual players can also toggle their WAILA HUD with `/slimehud toggle`.

The HUD lets you see what Slimefun item a block is without breaking or opening its menu. It also displays additional information depending on the block, such as network size, cargo channel, and energy generation. See the [wiki](https://schn.pages.dev/slimehud) for more details.

## Preview

<https://user-images.githubusercontent.com/101147426/182007545-474a6596-b4e2-4a92-bdab-c18ed2286a94.mp4>

## PlaceholderAPI

- `%slimehud_toggle%`  
Returns the current player's toggle state. Possible values are `true` or `false`.

## Limitations

- Minecraft only has 7 colors for the bossbar, compared to 16 for regular items.

## API

API documentation can be found [here](https://schn.pages.dev/slimehud/api-usage) on the wiki.

## Requirements

- Spigot or its derivatives
- Slimefun, of course

## Credits

Big thanks to Sefiraat for designing and creating the API!

*InfinityLib* by Mooy1  
*Lombok* by Project Lombok
