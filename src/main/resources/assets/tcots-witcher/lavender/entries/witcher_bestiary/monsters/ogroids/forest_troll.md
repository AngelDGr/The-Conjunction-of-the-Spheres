```json
{
  "title": "Forest Troll",
  "icon": "minecraft:oak_leaves",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
  ],
  "ordinal": 6
}
```

Transversing a dense forest isn't always an easy task, as sometimes those campfires in the horizon doesn't mean 
that a village it's near, but instead a forest troll it's ahead.


They don't have the same rocky defenses as other trolls, so instead
they try to counter their lack of defense wearing primitive clothing.

;;;;;

Even though forest troll are slightly larger than rock or ice trolls, they are
the calmest of the troll's subspecies.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forest
- Dark Forest

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_special.png,fit)

;;;;;

{red}**Behavior**{}

Quicker than rock or ice trolls, forest trolls favor melee combat.
They sometimes block their front with thick, sturdy arms, though a well-aimed strike with a sharp axe can shatter their defense. 

Capable of rapid regeneration, these trolls will recover from wounds unless utterly slain.

When distant from their prey, they charge at full speed, destroying anything in their path.

;;;;;

Even the __rabid__ breed it's also present in the subspecies, it's rare to find one.
The best strategy against these monster it's try to [befriend](^tcots-witcher:misc/befriending_troll) them if possible instead of 
engage in combat.

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
                                        minecraft:leather
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
                                        minecraft:bone
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
                                        minecraft:string
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
                                        minecraft:white_wool
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




![Forest Troll](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_full.png,fit)