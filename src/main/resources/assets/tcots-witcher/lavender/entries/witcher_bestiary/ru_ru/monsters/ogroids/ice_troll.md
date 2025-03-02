```json
{
  "title": "Ледяной Тролль",
  "icon": "minecraft:packed_ice",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
  ],
  "ordinal": 5
}
```

Восхождение на вершины заснеженных пиков никогда не было безопасным занятием. 
Можно поскользнуться и упасть в овраг, быть погребённым под лавиной, ну, или наткнуться на ледяного тролля.

;;;;;




В отличие от скальных троллей, обитающих на более низких высотах, эти, обитающие в вечной мерзлоте монстры, рассматривают почти каждого встречного путника как возможный ингредиент для вкусного блюда.

;;;;;

К счастью, ледяные тролли живут на вершинах высоких горных хребтов, настолько недоступных, что ни одно разумное существо туда не пойдёт по своей воле.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Заснеженные горы
- Снежные биомы

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_special.png,fit)

;;;;;

{red}**Поведение**{}

Ледяные тролли более жестоки, чем их скалистые сородичи. 
Хотя они и используют схожую тактику в бою, они живучей и сильнее горных троллей и, следовательно, более опасны. 

Как и у скальных троллей, их спины покрыты толстой защитной бронёй, а это значит, что напасть на них с зади даже и пытаться не стоит.

;;;;;

Несмотря на то, что есть несколько случаев, указывающих на то, что некоторым людям удалось [подружиться](^tcots-witcher:misc/befriending_troll) с этими чудовищами.


И, наконец, никогда не вздумайте нападать на них во время снежной бури. В эти моменты они черпают энергию из окружающего их холода и сражаются с удвоенной силой.

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
                                        minecraft:ice
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
                                        minecraft:packed_ice
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
                                        minecraft:blue_ice
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
                                        tcots-witcher:troll_mutagen
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




![Ледяной Тролль](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ice_troll/ice_troll_full.png,fit)
