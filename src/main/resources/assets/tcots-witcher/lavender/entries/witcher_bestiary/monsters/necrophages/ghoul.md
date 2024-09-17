```json
{
  "title": "Ghoul",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/ghoul",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:ghoul_blood"
  ],
  "ordinal": 2
}
```

In part, they resemble humans—yet on the whole, they are the utter negation of all that is human. 
Though they have arms and legs like men, they walk on all fours like wolves or foxes. 


Though they have eerily familiar faces, one searches them in vain for any sign of sentiment, reason or even a spark of consciousness. 

;;;;;

They are driven by one thing and one thing only: an insatiable craving for human flesh.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- [Ghoul nests](^tcots-witcher:misc/monster_nests), found in plains or forests
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_regen.png,fit)

;;;;;

{red}**Behavior**{}

Ghouls are fast creatures, they love the rotting flesh of corpses, 
so they usually hunt zombies, but they will not hesitate to hunt humans or even
meaty livestock like cows or pigs, as they can recover from eating flesh.

As other monsters, they sometimes launch themselves to their target.

If a ghoul is low on health, and you stop hitting it for some seconds, the ghoul is going to
start becoming enraged.

;;;;;

You can tell that they are in this mode because a red aura starts to surround them. Once enraged, they start
regenerating health at an alarming fast rate. This enraged mode only lasts some seconds, so {#0A880E}try to run
or just hit it rapidly.{}


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




![Ghoul](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_full.png,fit)
