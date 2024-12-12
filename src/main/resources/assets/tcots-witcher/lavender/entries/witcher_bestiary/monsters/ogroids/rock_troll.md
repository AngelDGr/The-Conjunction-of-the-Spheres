```json
{
  "title": "Rock Troll",
  "icon": "minecraft:cobblestone",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
    "tcots-witcher:cave_troll_liver",
    "tcots-witcher:troll_mutagen"
  ],
  "ordinal": 4
}
```

If while hiking high in the mountains you come across a walking stone, do not think your eyes deceive you. 
Instead, draw your sword – for before you stands a rock troll.


True, not every encounter with these creatures ends in a fight - while not particularly intelligent, 
trolls are capable of reason.

;;;;;

Just like other monsters though, some are outright hostile and can't be reasoned with.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Mountains
- Caves

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_special.png,fit)


;;;;;

{red}**Behavior**{}

Rock trolls are vicious fighters, boasting both immense strength and defense, their stone-covered backs armors them too well for flanking tactics. 
Fleeing from a troll offers little safety, as they can throw stones with surprising speed and precision.


Their massive arms also allow them to block front attacks, making them as sturdy as a stone wall when blocking.

;;;;;

Thankfully, many rock trolls are not aggressive and can even [befriend humans](^tcots-witcher:misc/befriending_troll). 
However, a particular breed known as __rabid trolls__ is purely hostile, easily recognized by their red eyes.

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
                                        tcots-witcher:cave_troll_liver
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
                                        minecraft:cobblestone
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




![Rock Troll](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_full.png,fit)
