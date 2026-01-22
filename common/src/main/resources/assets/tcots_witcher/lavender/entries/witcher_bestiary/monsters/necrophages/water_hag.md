```json
{
  "title": "Water Hag",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/water_hag",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:water_hag_mutagen",
    "tcots_witcher:water_hag_mud_ball"
  ],
  "ordinal": 5
}
```

Like [drowners](^tcots_witcher:monsters/necrophages/drowner), with whom they often share hunting grounds, water hags
dwell near shallow streams, rivers and wetlands. 


Though bulky, they are excellent swimmers. They can even swim through thick mud with astonishing agility,
surfacing beside their victims to attack them with their sickle-shaped claws.

;;;;;

They are also able to form this mud into balls, which they toss to temporarily blind opponents.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/water_hag/main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Swamps and Mangrove Swamps near the water
- Rivers and lakes

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/water_hag/secondary.png,fit)

;;;;;

{red}**Behavior**{}

Similar to the drowner, when the water hag doesn't find a creature to attack,
it will dig a hole in the ground and enter in. These holes leave a tiny puddle.
If you get too close to the puddle, the water hag will emerge from it to attack you.


If you get enough distance it's going to launch a mudball, be warned, this mudball can easily
blind you.

;;;;;

The mud in your eyes it's going to wash off after a little time, and it's going to wash faster if you are in water.
{#0A880E}Just don't be fooled during the time you don't have enough vision.{}


{blue}**Loot**{}
```xml owo-ui
        <flow-layout direction="vertical">
            <children>
                <!--First Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots_witcher:water_hag_mud_ball
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>
                        </stack-layout>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots_witcher:water_essence
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>
                        </stack-layout>

                    </children>
                    <sizing>
                        <horizontal method="content">1</horizontal>
                        <vertical method="content">1</vertical>
                    </sizing>
                    <horizontal-alignment>center</horizontal-alignment>
                    <vertical-alignment>center</vertical-alignment>
                </flow-layout>
                
            </children>
            <horizontal-alignment>center</horizontal-alignment>
            <vertical-alignment>center</vertical-alignment>
        </flow-layout>
```

{dark_blue}**Mutagen**{}
```xml owo-ui
        <flow-layout direction="vertical">
            <children>
                <!--First Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots_witcher:water_hag_mutagen
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>
                        </stack-layout>
                        
                    </children>
                    <sizing>
                        <horizontal method="content">1</horizontal>
                        <vertical method="content">1</vertical>
                    </sizing>
                    <horizontal-alignment>center</horizontal-alignment>
                    <vertical-alignment>center</vertical-alignment>
                </flow-layout>
                
            </children>
            <horizontal-alignment>center</horizontal-alignment>
            <vertical-alignment>center</vertical-alignment>
        </flow-layout>
```

;;;;;




![Water Hag](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/water_hag/full.png,fit)