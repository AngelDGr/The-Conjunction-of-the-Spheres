```json
{
  "title": "Grave Hag",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/grave_hag",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:grave_hag_mutagen"
  ],
  "ordinal": 6
}
```

Few monsters' names fit as well as the grave hags.
As one might guess, these creatures resemble aged, 
deformed women and loiter near graveyards and battlefields.
Grave hags feed on human corpses and in particular 
on the rotten marrow which they slurp from human bones using their long, prehensile tongues. 

;;;;;

With claws built for digging up buried bodies that serve just as well as weapons to rend flesh.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Forests (at night)
- Plains (at night)
- Dark caves

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_running.png,fit)

;;;;;

{red}**Behavior**{}

Even when it's similar in appearance to a [water hag](^tcots_witcher:monsters/necrophages/water_hag), 
a grave hag is way more aggressive and dangerous, these two don't share hunting grounds.


Instead of launching tiny mudballs, if you try to escape, it's going to run on it four limbs
and try to catch you at a tremendous speed.

;;;;;

It can also use its tongue as a weapon, making a sweep with it, causing a lot of knockback
and damaging a lot any shield that you could use.


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
                                        tcots_witcher:grave_hag_mutagen
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




![Grave Hag](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_full.png,fit)
