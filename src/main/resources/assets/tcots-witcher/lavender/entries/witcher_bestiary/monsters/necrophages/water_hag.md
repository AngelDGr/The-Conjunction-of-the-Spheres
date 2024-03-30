```json
{
  "title": "Water Hag",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/water_hag",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:water_hag_mutagen",
    "tcots-witcher:water_hag_mud_ball"
  ],
  "ordinal": 3
}
```

Like [drowners](^tcots-witcher:monsters/necrophages/drowner), with whom they often share hunting grounds, water hags
dwell near shallow streams, rivers and wetlands. 

Though bulky, they are excellent swimmers. They can even swim through thick mud with astonishing agility,
surfacing beside their victims to attack them with their sickle-shaped claws.

;;;;;

They are also able to form this mud into balls, which they toss to temporarily blind opponents.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Swamps and Mangrove Swamps, near the water
- Rivers and lakes

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_launching.png,fit)

;;;;;

{red}**Behavior**{}

Similar to the drowner, when the water hag doesn't find a creature to attack,
it will dig a hole in the ground and enter in. These holes leave a tiny puddle.
If you get too close to the puddle, the water hag will emerge from it to attack you.


If you get enough distance it's going to launch a mudball, be warned, this mudball can easily
blind you.

;;;;;

The mud in your eyes it's going to wash off after a little time, and it's going to wash faster if you are in water.
Just don't be fooled during the time you don't have enough vision.


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
                                        tcots-witcher:water_hag_mud_ball
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
                                        tcots-witcher:bunch_of_leaves
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
                                        tcots-witcher:water_hag_mutagen
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




![Water Hag](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_full.png,fit)