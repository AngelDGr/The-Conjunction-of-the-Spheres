```json
{
  "title": "Гуль",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/necrophages/ghoul",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:ghoul_blood"
  ],
  "ordinal": 2
}
```

Отчасти они похожи на людей, но по своей сути являются полным отрицанием всего человеческого. 
Руки и ноги у них похожи на человеческие, но ходят они на четвереньках, как волки и лисы. 


Хоть у них и до жути знакомые лица, вы напрасно ищете в них хоть какие-то признаки чувств, разума или хотя бы искру сознания.

;;;;;

Ими движет одно и только одно: неутолимая жажда человеческой плоти.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- [Гнёзда гулей](^tcots-witcher:misc/monster_nests), найденые на равнинах и в лесах.
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_regen.png,fit)

;;;;;

{red}**Поведение**{}

Гули - быстрые существа, они любят гниющую плоть трупов, 
поэтому обычно они охотятся на зомби, но без колебаний убивают и людей или даже
скот. Например, коров и свиней, так они могут восстанавливать силы, питаясь плотью.

Как и другие чудовища, гули иногда бросаются на свою цель.

Если у гуля мало здоровья и вы не будете атаковать его несколько секунд, гуль
начнёт впадать в ярость.

;;;;;

Вы можете определить, что они разъярены по окружающей их красной ауре. Разъярившись они начинают
восстанавливать здоровье с пугающей скоростью. Режим ярости длится несколько секунд, так что {#0A880E} попытайтесь убежать
или быстро наносите удары.{}


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
                      </stack-layout>
                      
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:ghoul_blood
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




![Гули](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/ghoul/ghoul_full.png,fit)
