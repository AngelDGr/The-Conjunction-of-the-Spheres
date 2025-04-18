```json
{
  "title": "Скальный Тролль",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/rock_troll",
  "category": "tcots_witcher:ogroids",
  "associated_items": [
    "tcots_witcher:cave_troll_liver",
    "tcots_witcher:troll_mutagen"
  ],
  "ordinal": 4
}
```
Если во время похода в горы вы заметите движущийся камень — не сомневайтесь в своём зрении.
Лучше приготовьте меч — скорее всего, перед вами Скальный Тролль.


Откровенно говоря, не все встречи с этими существами ведут к сражению — пусть тролли и не блещут умом, но здраво рассуждать способны.

;;;;;

Однако, как и другие монстры, некоторые из них откровенно враждебны, и с ними бывает невозможно договориться.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Горы
- Пещеры

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_special.png,fit)

;;;;;

{red}**Поведение**{}

Скальные Тролли — жестокие бойцы с огромной силой и защитой. Их каменное покрытые спины надёжно оберегает от фланговых атак. 
Убегать от них, честно говоря, затея скверная — они с удивительной силой и точностью метают камни.


Их массивные руки также позволяют им блокировать атаки в лоб, ведь при блокировке их руки становятся крепкими, как каменная стена. 

;;;;;

К счастью, многие Скальные Тролли не агрессивны и умеют [дружить с человеком](^tcots_witcher:misc/befriending_troll). 
Однако, существует печально известная порода __бешенных__ троллей. Они настроены крайне враждебно, их легко узнать по красным от ярости глазам.

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
                                        tcots_witcher:cave_troll_liver
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
                                        minecraft:cobblestone
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




![Скальный Тролль](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/rock_troll/rock_troll_full.png,fit)
