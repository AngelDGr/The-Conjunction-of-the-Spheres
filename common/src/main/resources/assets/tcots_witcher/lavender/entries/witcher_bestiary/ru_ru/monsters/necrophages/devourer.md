```json
{
  "title": "Яга",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/devourer",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:devourer_teeth"
  ],
  "ordinal": 8
}
```

Яг часто называют ночными ведьмами, потому что они похожи на старых, уродливых женщин и славятся ведьминской жестокостью. 

Эти существа не прочь полакомиться человеческой плотью; хотя они охотно поедают трупы, прежде всего они жаждут свежей и тёплой плоти.

Ночью Яги охотятся группами, которые крестьяне называют шабашами ведьм.

;;;;;

Они любят дурачить и мучить своих жертв, но в сказках об их полуночных полётах на мётлах и пряничных домиках нет ни капли правды.  
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/devourer/main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- Болота (ночью)

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/devourer/secondary.png,fit)

;;;;;

{red}**Поведение**{}

Это ненасытное чудовище обладает огромной силой: средняя группа с лёгкостью справляется с железным големом, 
но их главная уязвимость - скорость. 


Их главное оружие - мощные ноги, которые позволяют им совершать сильные прыжки, 
отталкивая и нанося урон любому существу рядом с силой, зависящей от силы прыжка.

;;;;;

Примите во внимание главный недостаток этой атаки: {#0A880E}она должна приземлиться на достаточно твёрдую поверхность, чтобы нанести хоть какой-то урон.{}


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
                                        tcots_witcher:devourer_teeth
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




![Яга](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/devourer/full.png,fit)
