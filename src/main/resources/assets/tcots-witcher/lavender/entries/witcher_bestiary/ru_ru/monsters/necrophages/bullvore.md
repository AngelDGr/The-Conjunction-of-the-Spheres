```json
{
  "title": "Утковол",
  "icon": "tcots-witcher:bullvore_horn_fragment",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    
  ],
  "ordinal": 11
}
```

Утковола можно сравнить с грудой мышц, сдерживаемых мешком из жёсткой и эластичной кожи.
Его голова похожа на голову крупного быка, а во рту острые, как лезвия, зубы для раздирания плоти.

Признаками Хаоса, бросающимися в глаза, являются рога и маленькие отростки рук, растуще по всему его телу, которыми существо едва шевелит.

;;;;;

Это чудовище не любит компанию себе подобных, но 
иногда ему составляют компанию более мелкие и слабые твари, в особенности гнильцы.
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Подземелья
- Тёмные пещеры

*Он всегда окружён меньшими по размеру трупоедами*
![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_special.png,fit)

;;;;;

{red}**Поведение**{}

Утковол - самый крупный и мощный из известных трупоедов. Он - огромная проблема даже для железного голема. 
С его длинными, острыми, как бритва, когтями он наносит мощные удары, пробивающие любой щит.


Самая характерная атака это его таран.
Используя свои огромные рога, утковол пробивает всё на своём пути, без особого труда уничтожая листья и небольшие препятствия. 

;;;;;

Несмотря на всю его мощь, медлительность утковола может быть использована против него. 
Кроме того, {#0A880E}начав таранить, он не изменит направление, пока не дойдёт до конца.{}


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
                                        tcots-witcher:bullvore_horn_fragment
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




![Утковол](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/bullvore/bullvore_full.png,fit)
