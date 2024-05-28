```json
{
  "title": "Devourer",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/devourer",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:devourer_teeth"
  ],
  "ordinal": 8
}
```

Devourers are often called night witches, because they resemble old, ugly women and are famous for their witch-like viciousness. 

These creatures gorge themselves on human flesh; although they willingly eat carcasses, above all they crave flesh that is fresh and warm.

Devourers hunt after dark in groups that peasants refer to as sabbaths.

;;;;;

They like to deceive their victims and torture them, but there is no truth to the tales of their midnight flights on broomsticks 
and their gingerbread houses.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/devourer/devourer_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- Rivers (at night)

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_exploding.png,fit)

;;;;;

{red}**Behavior**{}

Slow but strong creature, immune to stun.

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
                                        tcots-witcher:devourer_teeth
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