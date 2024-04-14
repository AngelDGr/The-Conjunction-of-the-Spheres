```json
{
  "title": "Foglet",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/foglet",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:foglet_mutagen",
    "tcots-witcher:foglet_teeth"
  ],
  "ordinal": 4
}
```

Foglets may appear wherever thick fog can arise: if no fog is forthcoming, they summon it themselves. 
Quickly thickening air undisturbed by the wind infallibly signals a foglet is present - and preparing an attack.

These creatures have powerful arms and claws, 
yet what makes them truly dangerous is their mastery of deception, 
beguilement and disorientation.

;;;;;

The foglets are additionally said to possess an ability to create complicated illusions made from
pure fog.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Swamps (at night)
- Forests (at night, especially Dark Forests)
- Mountains (at night)

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_fog.png,fit)

;;;;;

{red}**Behavior**{}

The Foglet it's a dangerous illusionist monster, when some target it's near it's going to
summon dense fog around it and become invisible and invincible, transforming in just fog.


At the moment of the transformation it's going to summon two illusions, 
don't be afraid of these illusions, as they can be dispelled with only one hit.

;;;;;

The foglet and the two illusions are going to attack you, and only when they hit you they will become 
visible and vulnerable again, take this moment to {#0A880E}kill it rapidly before transforms again in fog.{}

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
                                        tcots-witcher:foglet_teeth
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
                                        tcots-witcher:foglet_mutagen
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




![Foglet](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_full.png,fit)