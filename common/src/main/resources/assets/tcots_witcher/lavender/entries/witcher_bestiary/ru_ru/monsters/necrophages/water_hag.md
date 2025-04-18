```json
{
  "title": "Водная баба",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/water_hag",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:water_hag_mutagen",
    "tcots_witcher:water_hag_mud_ball"
  ],
  "ordinal": 5
}
```

Как и [утопленники](^tcots_witcher:monsters/necrophages/drowner), с которыми они делят охотничьи угодья, водные бабы
обитают вблизи неглубоких ручьёв рек и болот. 


Несмотря на свои размеры, они отлично плавают. Они плавают по густой грязи с поразительной ловкостью,
выпригивая на жертву, чтобы атаковать ее своими когтями.

;;;;;

Так же они умеют формировать из этой грязи шары, которые бросают, чтобы ослепить противника.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Болота и мангровые болота рядом с водой
- Реки и озёра

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_launching.png,fit)

;;;;;

{red}**Поведение**{}

Как и утопленники, когда у водной бабы нет цели,
она выкапывает в земле нору и сидит внутри. В этих отверстиях остаётся крошечная лужа.
Если вы подойдёте слишком близко к этой луже, из неё выскочит водная баба и нападёт на вас.


Если вы подойдёте к ней слишком близко, она кинет грязевой шар, который может вас ослепить.

;;;;;

Грязь, попавшая вам в глаза, через некоторое время смоется, а если вы окажетесь в воде, то ещё быстрее.
{#0A880E}Только не дайте себя обмануть, пока вы ослеплены.{}


{blue}**Добыча**{}
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
                                        tcots_witcher:water_hag_mud_ball
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

{dark_blue}**Мутаген**{}
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
                                        tcots_witcher:water_hag_mutagen
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




![Водная баба](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/water_hag/water_hag_full.png,fit)