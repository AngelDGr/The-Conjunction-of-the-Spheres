```json
{
  "title": "Rotfiend",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/rotfiend",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:rotfiend_blood"
  ],
  "ordinal": 3
}
```

Rotfiends resemble decomposing human bodies that have been stripped of their skin. 


Their presence is given away by the overwhelming stench of the rot which gives them their name.
They usually feed in large groups and thus present a danger to lone travelers – especially considering their speed.

;;;;;

Be careful fellow traveler, these creatures can represent a serious threat to any inexperienced warrior.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/rotfiend/main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- Dark caves

  ![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/rotfiend/secondary.png,fit)

;;;;;

{red}**Behavior**{}

The rotfiend rests underground and only emerges when find a target, so be careful, as it can take you by surprise.
They are fast and dangerous, and it will try to lunge at you, so be faster than it.


Be warned of their worst trait; they are highly explosive, 
when they damaged enough its body thrashes around in uncontrollable tremors, leading to an aggressive explosion.

;;;;;

The resulting explosion will hurt a lot, but it might also kill nearby rotfiends, creating something of a chain reaction.


The only way to prevent this explosion is {#0A880E}killing it when the creature it's in flames.{}


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
                              tcots_witcher:cadaverine
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
                                        tcots_witcher:rotfiend_blood
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




![Rotfiend](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/rotfiend/full.png,fit)