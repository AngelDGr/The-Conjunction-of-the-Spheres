```json
{
  "title": "Nekker Warrior",
  "icon": "tcots-witcher:nekker_heart",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
    "tcots-witcher:nekker_warrior_mutagen"
  ],
  "ordinal": 2
}
```


Nekker Warriors are a particularly dangerous subspecies of the common Nekker.

These creatures' increased strength and resilience are what set them apart from their lesser kin.
Standing slightly taller and adorned with more pronounced spines and claws, 
Nekker Warriors lead their packs with a ferocity that makes them formidable adversaries.

;;;;;

Encounters with Nekker Warriors are never solitary affairs, as these are always surrounded by other Nekkers.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests
- Plains
- [Nekker nests](^tcots-witcher:misc/monster_nests), found in plains or forests
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_command.png,fit)

;;;;;

{red}**Behavior**{}

Just like the common Nekker, the Nekker Warrior digs a burrow to stay 
hidden when it doesn't have a target, jumping the moment it finds a prey.


Their increased strength makes them tougher opponents, sometimes able to break through a warrior's shield. 
As leaders, Nekker Warriors also boost the effectiveness of the Nekkers they command, making their packs more formidable.

;;;;;

Try to focus on it when fighting a Nekker swarm; without a leader, the Nekkers lose much of their strength.


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
                                        tcots-witcher:nekker_eye
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
                                        tcots-witcher:nekker_heart
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
                                        tcots-witcher:nekker_warrior_mutagen
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




![Nekker Warrior](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_full.png,fit)