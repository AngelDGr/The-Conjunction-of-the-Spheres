```json
{
  "title": "Alghoul",
  "icon": "tcots_witcher:alghoul_bone_arrow",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:alghoul_bone_marrow"
  ],
  "ordinal": 3
}
```

Alghouls differ from normal [ghouls](^tcots-witcher:monsters/necrophages/ghoul) in size, strength, coloring and, most importantly, intelligence. Whereas ghouls are unfit to 
plan even the simplest ambush, alghouls are capable of forethought, and are thus much more dangerous.

Ghouls seem to possess wits enough to at least know a brighter mind when they see it, and so let alghouls lead their packs.

;;;;;

When encountering such a pack the alghoul should be eliminated as a first priority, leaving the other beasts for once their leader is gone.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/alghoul/alghoul_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- [Ghoul nests](^tcots-witcher:misc/monster_nests), found in plains or forests
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/alghoul/alghoul_spikes.png,fit)

;;;;;

{red}**Behavior**{}

They are a stronger version of [ghouls](^tcots-witcher:monsters/necrophages/ghoul), so they share a lot of similarities.


One main difference it's that when they start regenerating, their shout it's so powerful that it's going to push any
other monster nearby.


Once they stop shouting, they are going to grow their spikes, at this point any melee damage it's going to damage
the attacker too.

;;;;;

If the Alghoul has its spikes out, or it's in the middle of a combat, it can also enrage normal ghouls that are close to it, 
so {#0A880E}it's better focus on killing it before the other ghouls.{}


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
                              tcots-witcher:cadaverine
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
                                        tcots-witcher:ghoul_blood
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
                              tcots-witcher:alghoul_bone_marrow
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




![Alghoul](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/alghoul/alghoul_full.png,fit)