```json
{
  "title": "Graveir",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/graveir",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:graveir_bone"
  ],
  "ordinal": 9
}
```

Graveirs are depraved, lecherous and treacherous bastards. 
They are one of the most massive necrophages, 
they have some bony combs on their head and short but cruel, 
thick claws.


Their teeth and thin tongue allow them to eat marrow — and the more rotten and rancid the marrow, 
the more it is to their liking.


;;;;;

Its most striking features include its small red glowing eyes, 
that causes fear in its enemy.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Underground
- Dark Caves


![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_punch.png,fit)

;;;;;

{red}**Behavior**{}

The Graveir is a formidable foe, capable of defeating seasoned warriors with powerful strikes that deliver significant knockback.

It can also unleash a devastating special move, slamming the ground to create a shockwave that deals heavy damage and knocks back nearby creatures.

Its thick, resilient skin grants high resistance to explosions, making it even tougher to defeat. 



;;;;;

Despite its immense strength, the Graveir's slow speed can be exploited in combat, providing opportunities 
for skilled fighters to gain the upper hand and ultimately overcome this formidable adversary.


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
                                        tcots-witcher:graveir_bone
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




![Graveir](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_full.png,fit)
