```json
{
  "title": "Ice Giant",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/ogroids/ice_giant",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
    "tcots-witcher:giant_anchor"
  ],
  "ordinal": 7
}
```

These kind of powerful, primeval monsters are quite rare these days, possibly being near extinction. 
The Ice Giant resembles an humanoid in many respects, but is blue as frost and taller than a tree. 
Though it seems capable of reason, all attempts to communicate with it to date have ended the 
same way – in a quick and painful death.

;;;;;

Some claim its skin is blue because it was born of snow and ice, which is clearly impossible –
though it's capable of withstanding the fiercest frosts.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- *Unknown*


![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_special.png,fit)

;;;;;

{red}**Behavior**{}

The Ice Giant is the most powerful ogroid known. Often found sleeping in its lair, it’s best left undisturbed, 
but if you choose to face it, prepare for a fierce fight.

If you’re seeking an Ice Giant’s lair, your best option is to find a cartographer for a map to its cave.

But beware, as survivors of past confrontations report that these giants favor a deadly weapon: a massive ship’s anchor.

;;;;;

Many have sought out an Ice Giant in hopes of slaying it, yet few return. 
Legends tell of these warriors wielding a powerful weapon called {#007b77}Winter’s Blade{}—a relic 
that might still lie hidden within the monster’s lair.


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




![Ice Giant](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_full.png,fit)
