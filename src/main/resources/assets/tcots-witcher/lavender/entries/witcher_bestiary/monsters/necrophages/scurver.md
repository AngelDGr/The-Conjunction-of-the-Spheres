```json
{
  "title": "Scurver",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/scurver",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:scurver_spine"
  ],
  "ordinal": 7
}
```

Scurvers are [rotfiends](^tcots-witcher:monsters/necrophages/rotfiend) larger cousins.
While fighting them one cannot afford to forget about their special boney spines, 
razor-sharp protuberances sticking out from their skeletons.


When a scurver is near death, the gasses and enzymes gathered within its body cause it
to explode.   


;;;;;

The spines go out at great speed, turning them into one last deadly weapon in their arsenal.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- Jungles (at night)

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_exploding.png,fit)

;;;;;

{red}**Behavior**{}

They are basically a stronger version of rotfiends.


Their main difference it's the spines that grows across all its body. If one of these spines hits you
it's going to do a lot of damage, and it's going to make you bleed, so make sure to use a shield when this happens to protect 
yourself from the resulting explosion (or get far enough away quickly).

;;;;;

If you manage kill the creature when it's in flames, you could retrieve its spines and use them as weapons.


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
                                        tcots-witcher:rotfiend_blood
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
                                        tcots-witcher:scurver_spine
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




![Scurver](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_full.png,fit)
