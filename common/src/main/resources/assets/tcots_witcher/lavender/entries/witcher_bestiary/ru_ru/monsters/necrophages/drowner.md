```json
{
  "title": "Утопец",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/drowner",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:drowner_tongue",
    "tcots_witcher:drowner_brain"
  ],
  "ordinal": 1
}
```

Один из самых распространённых трупоедов, они обитают вблизи водоёмов, таких как реки, озёра, их особенно много на болотах.

Утопец похож на труп, поднятый со дна озера. Они имеют тошнотворный синий или зелёный цвет.
Из каждой поры на их кожи сочатся слизь и ил. Также от утопцев исходит едкий запах гнили.

;;;;;

Считается, что обычно утопцами становятся те везунчики, которые утонули на мелководье.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{} 
- Болота и мангровые болота у воды
- Реки и озёра
- Пляжи рядом с водой
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_swimming.png,fit)

;;;;;

{red}**Поведение**{}

Утопцы убивают других водных обитателей. Когда они не находят жертву, на которую можно напасть,
они выкапывают яму в земле и сидят внутри.
Снаружи эти норы похожи на маленькую лужу.

Если вы подойдёте к луже слишком близко, утопец нападёт на вас.

Когда они атакуют, то иногда набрасываются на свою цель. Остерегайтесь таких выпадов.

;;;;;

Находясь в воде они расслабляются, поэтому будут медленные и нападать станут лишь время от времени, пытаясь утопить свою цель. 
__Когда они плавают, а вы находитесь под водой__ их {#0A880E}можно легко убить прицельным выстрелом из арбалета.{}


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
                                        tcots_witcher:cadaverine
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
                                        tcots_witcher:drowner_tongue
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
                                        tcots_witcher:drowner_brain
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

;;;;;




![Утопец](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/drowner/drowner_full.png,fit)