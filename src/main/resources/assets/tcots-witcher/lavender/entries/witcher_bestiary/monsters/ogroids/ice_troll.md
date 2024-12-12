```json
{
  "title": "Ice Troll",
  "icon": "minecraft:packed_ice",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
  ],
  "ordinal": 5
}
```

Climbing to the top of snow-covered peaks is never a safe endeavor. 
One can slip and fall into a ravine, be buried in an avalanche – or stumble across ice trolls.


Unlike the rock trolls found at lower altitudes,
these permafrost-dwelling monsters treat nearly every man they encounter as a possible ingredient for a tasty meal.

;;;;;

Luckily ice trolls live atop high mountain ridges so inaccessible they rarely encounter any sentient being.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Snowy mountains
- Snowy biomes

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_special.png,fit)

;;;;;

{red}**Behavior**{}

Ice trolls are crueler than their rocky kin. 
Though they use similar tactics in battle, they are heartier and stronger than rock trolls and thus more dangerous. 

Like rock trolls, their backs are covered in thick protective armor, meaning one should never strike them from the rear.


Even though, there are a few reports that indicate some humans have been able to [befriend](^tcots-witcher:misc/befriending_troll) these monsters.

;;;;;

Lastly, never think of attacking them during a blizzard. At such times they draw power from the surrounding cold and fight with increased strength.

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
                                        minecraft:ice
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>

                            <horizontal-alignment>center</horizontal-alignment>
                            <vertical-alignment>center</vertical-alignment>
                        </stack-layout>
                        
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        minecraft:packed_ice
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>

                            <horizontal-alignment>center</horizontal-alignment>
                            <vertical-alignment>center</vertical-alignment>
                        </stack-layout>

                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        minecraft:blue_ice
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>

                            <horizontal-alignment>center</horizontal-alignment>
                            <vertical-alignment>center</vertical-alignment>
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
                                        tcots-witcher:troll_mutagen
                                    </stack>
                                    <set-tooltip-from-stack>true</set-tooltip-from-stack>
                                </item>
                            </children>
                            <padding>
                                <all>5</all>
                            </padding>

                            <horizontal-alignment>center</horizontal-alignment>
                            <vertical-alignment>center</vertical-alignment>
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




![Ice Troll](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_full.png,fit)
