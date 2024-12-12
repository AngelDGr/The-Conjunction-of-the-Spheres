```json
{
  "title": "Devourer",
  "icon": "tcots-witcher:devourer_teeth",
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
- Swamps (at night)

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/devourer/devourer_jump.png,fit)

;;;;;

{red}**Behavior**{}

This insatiable beast has great strength, with a medium group being able to take down an Iron Golem with ease, 
though its major flaw is its speed. 


Its main weapon is its powerful legs, which allow it to make large and strong jumps, 
pushing and damaging any nearby creature away with a force depending on the magnitude of the jump.

;;;;;

Take in account that this attack mechanism has a major flaw; {#0A880E}it needs to land in an enough solid ground to being able to make any damage.{}


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
                                        tcots-witcher:devourer_teeth
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




![Devourer](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/devourer/devourer_full.png,fit)
