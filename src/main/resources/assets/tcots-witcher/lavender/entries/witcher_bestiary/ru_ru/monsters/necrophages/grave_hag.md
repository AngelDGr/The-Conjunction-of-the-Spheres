```json
{
  "title": "Кладбищенская баба",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/grave_hag",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:grave_hag_mutagen"
  ],
  "ordinal": 6
}
```

Многим чудовищам их имена не подходят, но не кладбищенской бабе.
Как можно догадаться, эти существа выглядят старыми 
изуродованными женщинами и бродят по кладбищам и полям битв.
Кладбищенские бабы питаются трупами, в особенности
гнилым костным мозгом, который они высасывают из человеческих костей своими длинными языками. 

;;;;;

Когти, предназначенные для раскапывания могил, служат неплохим оружием для разрывания плоти.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- Тёмные пещеры

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_running.png,fit)

;;;;;

{red}**Поведение**{}

Внешне похожая на [водную бабу](^tcots-witcher:monsters/necrophages/water_hag),
кладбищенская баба гораздо агрессивнее и опаснее. Эти двое не делятся местом охоты.


Если попробуешь убежать, то она не станет кидаться в тебя грязью, а на огромной скорости погонится 
за тобой на четырёх конечностях, пытаясь поймать.

;;;;;

Также она использует свой язык в качестве оружия, делая им взмах, она делает сильный удар
нанося урон любому щиту, который вы используете.



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
                                        tcots-witcher:grave_hag_mutagen
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




![Кладбищенская баба](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/grave_hag/grave_hag_full.png,fit)
