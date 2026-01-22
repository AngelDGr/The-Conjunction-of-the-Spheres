# 1.1.0 - 1.21
### Additions
- Added Raven's, Manticore and Warrior's Leather headbands, for consistency with Witcher (More RPG Classes)
- Added Manticore School Medallion! The recipe is only available with Witcher (More RPG Classes) loaded
- Added Monster Resistance, Monster Damage, Bomb Cooldown Reduction, Extra Alcohol Refill and Potion Drink Time attributes

### Changes
- Complete rework for the models and textures of all enemies! I will continue with this more detailed art-style from now on
- Redone the armors models and textures!
- Changed translations keys for consistency with vanilla ones 
- Changed some effect ids
- Changed sound ids for consistency with vanilla ones
- Increased ghoul speed
- Ghoul will sprint when attacking
- Ghouls now can also burrow themselves on the ground like other necrophages
- Completely redone tooltip system
- While being invisible, Foglets and Foglings don't longer have footsteps sounds
- Increased foglet health from 25hp to 30hp
- Nekker height changed from 0.975 to 1.25
- Nekker warrior height changed from 1.30 to 1.45
- Renamed armors to "suit" and trousers to "pants", for consistency with Witcher (More RPG Classes)
- Modified armor attributes, for consistency with Witcher (More RPG Classes)
- Increased the rarity for manticore and raven's armors from uncommon to rare, for consistency with Witcher (More RPG Classes)
- Changed Manticore and Raven's armors recipes with Witcher (More RPG Classes) loaded, now uses dark iron ingot, meteorite silver and silver instead of vanilla ingots
- Modified full set tooltips to be like Witcher (More RPG Classes)
- Modified armor set bonuses, now they apply depending on how many pieces you have from the set
- Added new full set bonuses for manticore and raven's armor
- Added particles to the Winter’s Blade block so players can more easily tell that it’s important
- Changed crafting of the Alchemy Table of using only cobblestone to use items from the tag stone_crafting_materials 
- Bullvores now can charge against their prey regardless of distance
- Changed all the textures of the bestiary to match the new textures, changed the categories icons too, if you discover from where the symbols are from, I will give you a cookie

### Fixes
- Foglings attack correctly to their Foglet target
- Raven's chestplate now has the correct defense value

# 1.0.7 - 1.21
### Additions
- Added the *Bloedzuiger*, a necrophage from W1 that dies with a cadaverine explosion, spawn on swamps
- Added Bindweed, a new potion made with Bloedzuiger blood, it reduces the damage taken from poison, cadaverine, wither and any damage-over-time effect
- Added support for witcher eyes/toxicity face for both Just Expressions and Fresh Moves!
- - Added an option to the config to make the pupils move alongside the animated eyes, true by default
- - Added allay, tall_center, derp and enderman eye options

### Changes
- Changed the location of many textures
- Changed the subtitles translation to "subtitles.tcots_witcher.[sound_name]"
- The green cloud particle used on the Devil's Puffball explosion now is the same color as the poison effect
- The cadaverine effect now does double damage to necrophages
- Changed all the pictures of the books containing text, not it uses the enchantment table language, to fit better with translations
- Changed damage and speed values from swords to fit better with Witcher (More RPG Classes).
- - Moonblade speed reduced from 1.8 to 1.6 
- - Winter's Blade damage reduced from 9 to 8
- - Winter's Blade aard sign intensity, from Witcher (More RPG Classes), reduced from 4 to 3
- - Ardaenye damage reduced from 8 to 7.5
- - Ardaenye speed increased from 1.4 to 1.6
- Modified the swords tooltip position to fit better with Witcher (More RPG Classes).
- Cadaverine decaying flesh recipe now gives 3 bones instead of two, added the possibility to decay rotten flesh to get 2 bones
- Superior Rook and Superior Wolf stack size increased from 3 to 4

# 1.0.6 - 1.21
### Bugfixes
- Changed some internal logic for better compatibility with other mods.
- Fixed Manticore toxicity attribute don't changing to green color in NeoForge.

# 1.0.5 - 1.21
### Additions
- Added new and unique animations from Better Combat to all Witcher swords.
- Added many tags to determine in which biomes plants and monsters spawn.
- Added an option to activate 32x32 textures without needing Witcher (More RPG Classes) installed.

### Changes
- Changed the mod ID from `tcots-witcher` to `tcots_witcher` because NeoForge only accepts underscores.
- The mod now requires the Architectury API to work, for development reasons.
- Creative Tab icon changed, now uses an animated icon (because it looks way cooler)
- The Dimeritium bomb explosion now causes 5 damage to Vexes and Allays.
- Moonblade extra damage reduced from +50% to +25%; it now applies after all other damage. With Witcher (More RPG Classes) loaded, it decreases to 15%.
- Added a new goal to the Evoker: it now runs faster when affected by the Dimeritium effect.
- With Witcher (More RPG Classes) loaded, swords now have a speed similar to the ones from that mod.
- Removed "Relic Sword" tooltip from Winter's Blade for consistency.

### Bugfixes
- Added Enhanced Ogroid Oil and Superior Ogroid Oil to the compendium in the Alchemy Almanac.
- Fixed an inconsistency with the recipe book: stack numbers now appear on the right instead of the left.
- Fixed an issue where trinket tooltip color changes would crash if loaded on servers.
- Fixed Bomb formulae don't appearing on the creative menu.

# 1.0.4 - 1.21
### Additions
- Russian Translation added, thanks to kreoxoxygen!

# 1.0.3 - 1.21
### Bugfixes
- Fixed a compatibility issue

# 1.0.2 - 1.21
### Additions
- Added compatibility for Witcher (More RPG Classes), all these changes will only be present if the Witcher (More RPG Classes) mod its present:
- - All the swords will change their texture to fit the 32x32 textures from Witcher (More RPG Classes)
- - Armors and Swords will have new attributes
- - The recipes for the swords will use Witcher swords instead of vanilla ones
- - Removed the Winter's Blade (RPG) from spawning on Witcher Graves
- - Witcher Grave loot table modified; now you can found alchemy formulae, alcohol, Ard'aenye, and D'yaebl. Witcher medallions can spawn if the Witcher Medallions mod its installed
### Changes
- Swords damage reduced to better fit with the other Witcher swords
- Alchemy formula texture changed to better with the Witcher (More RPG Classes) diagrams
- Fixed an issue with the Ghoul
- With Better combat the swords will behave exactly like the swords from Witcher (More RPG Classes)

# 1.0.1 - 1.21
- Minor bugfixes

# 1.0.0 - 1.21
- Initial Release