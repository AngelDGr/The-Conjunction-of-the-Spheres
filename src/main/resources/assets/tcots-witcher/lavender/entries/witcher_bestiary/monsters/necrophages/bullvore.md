```json
{
  "title": "Bullvore",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/bullvore",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    
  ],
  "ordinal": 11
}
```

The bullvore can be compared to a heap of muscles constrained by a sack of hard, elastic skin. 
Its head is that of a bovine's, yet its mouth is filled with sharp teeth adapted to rending flesh.

The visible mark of Chaos are the horns and vestigial hands the creature barely moves, growing all over its body.

;;;;;

This monster does not like the company of its own kind, but 
it is at times accompanied by smaller, weaker beasts, especially rotfiends.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Underground
- Dark Caves

*It's always surrounded by lesser necrophages
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_special.png,fit)

;;;;;

{red}**Behavior**{}

The Bullvore is the largest and most powerful necrophage known. It poses a severe challenge, even to an Iron Golem. 
With long, razor-sharp claws, it delivers powerful strikes that pierce any shield.


Its most distinctive attack is its charge. 
Using its massive horns, the Bullvore charges through anything in its path, destroying leaves and small obstacles effortlessly. 

;;;;;

Despite its power, the Bullvore’s slow speed can be exploited in combat. 
Additionally, once it starts its charge, it won’t change direction until it reaches the end.


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
                                        tcots-witcher:bullvore_horn_fragment
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




![Bullvore](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_full.png,fit)
