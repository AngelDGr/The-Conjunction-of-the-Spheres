```json
{
  "title": "Nekker",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/ogroids/nekker",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
    "tcots-witcher:nekker_heart",
    "tcots-witcher:nekker_eye"
  ],
  "ordinal": 1
}
```

A lone nekker is harmless. Five are dangerous. Ten can kill even a veteran monster slayer.


The nekkers basic tactic is to strike en masse.
They burrow from beneath the ground and swarm upon their prey. 
Though primitive, the tactic is surprisingly effective.
One must be ready to repel many foes at once.

;;;;;

They live in nests of several individuals.
So if you found one, rest assure that you are
going to find more.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests
- Plains
- Nekker nests, found in plains or forests
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_lunging.png,fit)

;;;;;

{red}**Behavior**{}

Be careful with this nasty creature.
As without a target, it's going to be buried
underground, waiting for a prey.
With its long claws, it's a great digger and fast
attacker, and with their tiny size can be a real problem.


If you get far enough, it will try to launch itself to you with a great force, this attack it's pretty common 
to these agile creatures.

;;;;;

Avoid being overwhelmed with the quantity of these tiny creatures, and you can manage to defeat them easily.


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
                                        tcots-witcher:nekker_eye
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
                                        tcots-witcher:nekker_heart
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




![Nekker](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_full.png,fit)

;;;;;

{dark_green}**Monster Nests**{}

These awful creatures live in nests made from dirt and bones around the world.
The nest will host more and more monsters until it's destroyed.


A monster nest it's very hard to break, and while it's well-known that a nest can be destroyed with an explosion, 
some rumours say that can also be destroyed with an efficiency enchantment strong enough. 

;;;;;

But breaking one has a reward, the Nekkers usually hide magic goods inside them.


![Monster Nest](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/monster_nest.png,fit)
