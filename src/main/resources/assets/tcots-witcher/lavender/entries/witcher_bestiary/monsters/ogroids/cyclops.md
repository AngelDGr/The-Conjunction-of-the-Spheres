```json
{
"title": "Cyclops",
"icon": "minecraft:leather",
"category": "tcots-witcher:ogroids",
"associated_items": [

],
"ordinal": 3
}
```

Cyclopses can easily be recognized by the single eye located in the center of their foreheads. 

If for some reason that is not visible, other tell-tale signs are their enormous size, 
incredible strength and a seething hatred for all humans.

Cyclopses go where they want; any warrior should move to the side if a cyclops is charging forward.

;;;;;

They rarely flinch from anything a warrior can do to them, so they keep trying to fight no matter how many cuts from a blade they take.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/cyclops/cyclops_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Mountains
- Old Growth Taigas
- Snowy Plains

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/cyclops/cyclops_special.png,fit)

;;;;;

{red}**Behavior**{}

The Cyclops is a large but simple-minded creature. Its massive muscles allow it to deliver powerful punches with significant knockback and damage.

At longer ranges, a Cyclops can leap into the air, crashing down and knocking over anything near its landing site.

These solitary beings are ruthless, showing no hesitation in killing one of their own kind.

;;;;;

When battling a Cyclops, maintain a medium distance. If it starts to jump, either raise your shield or run for your life.


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
                                        minecraft:rabbit_hide
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




![Cyclops](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/cyclops/cyclops_full.png,fit)