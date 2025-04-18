```json
{
  "title": "Ледяной Гигант",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/ice_giant",
  "category": "tcots_witcher:ogroids",
  "associated_items": [
    "tcots_witcher:giant_anchor"
  ],
  "ordinal": 7
}
```

Первое, что нужно знать - это то, что в наше время, такого рода могущественные первобытные чудовища, довольно редки и, возможно, находятся на грани вымирания.
Ледяной Гигант во многом напоминает гуманоида, но синий, словно иней, и выше дерева.
На первый взгляд может показаться, что он способен мыслить разумно, но до сих пор, все попытки связаться с ним 

;;;;;

заканчивались одинаково - быстрой и мучительной смертью.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_main.png,fit)

;;;;;

Некоторые утверждают, что его кожа голубая, потому что он был рождён из снега и льда, что явно невозномжно –
хоть он и способен выдержать самые холодные ветра севера.


;;;;;

{dark_green}**Среда обитания**{}
- *Неизвестно*


![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_special.png,fit)

;;;;;

{red}**Поведение**{}

Ледяной гигант - самый сильный и могущественный из всех известных огроидов. Его часто находят спящим в своём логове, где его лучше не тревожить, 
но если вы решите встретиться с ним лицом к лицу, то приготовьтесь к самой ожесточённой схватке в вашей жизни.

;;;;;

Если вы отчаянно ищете встречи со смертью, то лучше всего обратиться к картографу, чтобы он составил вам карту пещеры Ледяного гиганта.

В любом случае, будьте осторожны, поскольку выжившие в прошлых столкновениях сообщают, что у этих гигантов есть смертоносное оружие: массивный корабельный якорь.

;;;;;

Многие искали Ледяного гиганта в надежде прославиться убив его, но мало кто возвращался.
Легенды гласят о воинах, владевших мощным оружием под названием {#007b77}Клинок Зимы{} — реликвии,
которая, возможно, до сих пор спрятана в логове монстра.

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

![Ледяной Гигант](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ice_giant/ice_giant_full.png,fit)


