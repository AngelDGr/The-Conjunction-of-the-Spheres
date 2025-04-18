```json
{
  "title": "Scurver",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/scurver",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:scurver_spine"
  ],
  "ordinal": 7
}
```

Scurvers are [rotfiends](^tcots_witcher:monsters/necrophages/rotfiend) larger cousins.
While fighting them one cannot afford to forget about their special boney spines, 
razor-sharp protuberances sticking out from their skeletons.


When a scurver is near death, the gasses and enzymes gathered within its body cause it
to explode.   


;;;;;

The spines go out at great speed, turning them into one last deadly weapon in their arsenal.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- Jungles (at night)

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_exploding.png,fit)

;;;;;

{red}**Behavior**{}

They are basically a stronger version of rotfiends.


Their main difference it's the spines that grows across all its body. If one of these spines hits you
it's going to do a lot of damage, and it's going to make you bleed, so make sure to use a shield when this happens to protect 
yourself from the resulting explosion (or get far enough away quickly).

;;;;;

If you manage {#0A880E}kill the creature when it's in flames, you could retrieve its spines and use them as weapons.{}


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
                        
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots_witcher:scurver_spine
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




![Scurver](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_full.png,fit)
