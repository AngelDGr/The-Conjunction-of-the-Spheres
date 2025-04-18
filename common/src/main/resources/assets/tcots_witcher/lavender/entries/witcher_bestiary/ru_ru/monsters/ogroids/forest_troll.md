```json
{
  "title": "Лесные Тролли",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/forest_troll",
  "category": "tcots_witcher:ogroids",
  "associated_items": [
  ],
  "ordinal": 6
}
```

Пересечение зарослей густого леса не всегда лёгкая задача, ведь иногда костры на горизонте означают совсем не то, что рядом деревня, а то, что вам предстоит встреча лицом к лицу с лесным троллем, который может стать последним, кого вы увидите в своей жизни.



Но шансы есть...

;;;;;

У них нет надёжной элементальной брони, как у других троллей, поэтому вместо этого они пытаются компенсировать отсутствие защиты надевая примитивную одежду.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_main.png,fit)

;;;;;

Несмотря на то, что лесные тролли немного крупнее каменных или ледяных, они являются самыми спокойными из всех подвидов троллей.


{dark_green}**Среда обитания**{}
- Лес
- Тёмный лес

;;;;;

{red}**Поведение**{}

Лесные тролли быстрее, чем горные или ледяные тролли, они предпочитают ближний бой.
Иногда в целях защиты они закрываются своими толстыми и крепкими руками, хотя меткий удар острым топором может помочь пробить их защиту.


Эти тролли имеют способности к быстрой регенерации, они быстро оправляются от ран, если их не убить окончательно.

;;;;;

Оказавшись вдали от своей добычи, они бросаются в атаку на полной скорости, словно циклопы, уничтожая всё на своём пути.


В этом подвиде встречаются даже __бешенные__ тролли, но встречаются они довольно редко.

;;;;;

Лучшая стратегия против этих чудовищ - это [подружиться](^tcots_witcher:misc/befriending_troll) с ними, если это возможно, вместо того, чтобы 
вступать в бой.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_special.png,fit)

;;;;;

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
                                        minecraft:leather
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
                                        minecraft:bone
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
                                        minecraft:string
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
                                        minecraft:white_wool
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
                                        tcots_witcher:troll_mutagen
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




![Лесной Тролль](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/forest_troll/forest_troll_full.png,fit)