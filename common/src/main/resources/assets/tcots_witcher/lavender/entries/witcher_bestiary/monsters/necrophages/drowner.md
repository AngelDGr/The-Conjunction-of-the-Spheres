```json
{
  "title": "Drowner",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/drowner",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:drowner_tongue",
    "tcots_witcher:drowner_brain"
  ],
  "ordinal": 1
}
```

One of the most common necrophages, they inhabit near bodies of water, like rivers and lakes, they are specially 
numerous in swamps.


A drowner resembles a corpse dredged from the bottom of a pond. It is sickly blue or green in color,
with slime and sludge oozing out of every pore and the acrid stench of rot wafting off of it.

;;;;;

It is often thought drowners arise from the bodies of those who drown in shallow water.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_main.png,fit)

;;;;;

{dark_green}**Habitat**{} 
- Swamps and Mangrove Swamps near the water
- Rivers and lakes
- Beaches near the water
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_swimming.png,fit)

;;;;;

{red}**Behavior**{}

The drowner enjoys killing other water creatures, but when it doesn't find a creature to attack, 
it will dig a hole in the ground and enter in.
These holes leave a tiny puddle.
If you get too close to the puddle, the drowner will emerge from it to attack you.


When they attack, they sometimes launch themselves to their target, be careful and try to avoid those lunges.

;;;;;

When they enter the water, they become more relaxed, so they 
are going to be slower and only attack once in a while,
trying to drown their target. 
__When they are swimming, and you are underwater__ they {#0A880E}can be killed easily with a good
crossbow shot.{}


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
                                        tcots_witcher:drowner_tongue
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
                                        tcots_witcher:drowner_brain
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
                                        tcots_witcher:water_essence
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




![Drowner](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_full.png,fit)