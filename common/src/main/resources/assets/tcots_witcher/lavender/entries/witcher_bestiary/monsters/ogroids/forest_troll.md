```json
{
  "title": "Forest Troll",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/forest_troll",
  "category": "tcots_witcher:ogroids",
  "associated_items": [
  ],
  "ordinal": 5
}
```

Transversing a dense forest isn't always an easy task, as sometimes those campfires in the horizon doesn't mean 
that a village it's near, but instead a forest troll it's ahead.


They don't have the same rocky defenses as other trolls, so instead
they try to counter their lack of defense wearing primitive clothing.

;;;;;

Even though forest troll are slightly larger than rock or ice trolls, they are
the calmest of the troll's subspecies.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/forest_troll/main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forest
- Dark Forests

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/forest_troll/secondary.png,fit)

;;;;;

{red}**Behavior**{}

Quicker than rock or ice trolls, forest trolls favor melee combat.
They sometimes block their front with thick, sturdy arms, though a well-aimed strike with a sharp axe can shatter their defense. 

Capable of rapid regeneration, these trolls will recover from wounds unless utterly slain.

When distant from their prey, they charge at full speed, destroying anything in their path.

;;;;;

Even the __rabid__ breed it's also present in the subspecies, it's rare to find one.
The best strategy against these monster it's try to [befriend](^tcots_witcher:misc/befriending_troll) them if possible instead of 
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
                                        tcots_witcher:troll_mutagen
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




![Forest Troll](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/forest_troll/full.png,fit)