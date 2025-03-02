```json
{
  "title": "Наккер Воин",
  "icon_sprite": "tcots-witcher:witcher_bestiary/category/monsters/ogroids/nekker_warrior",
  "category": "tcots-witcher:ogroids",
  "associated_items": [
    "tcots-witcher:nekker_warrior_mutagen"
  ],
  "ordinal": 2
}
```


Наккер Воин - особенно опасный подвид Наккера обыкновенного.

Повышенная сила и живучесть этих существ отличает их от своих меньших сородичей.
Они немного выше ростом и украшены более выраженными шипами и когтями. 
Наккеры Воины возглавляют свои стаи со свирепостью, которая делает их грозными противниками.

;;;;;

Встречи с Наккерами Воинами никогда не бывают одиночными, так как они всегда окружены другими Наккерами.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса
- Равнины
- [Гнёзда чудовищ](^tcots-witcher:misc/monster_nests), могут быть найдены на равнинах и в лесах.
  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_command.png,fit)

;;;;;

{red}**Поведение**{}

Как и Наккер обыкновенный, Наккер Воин роет нору, чтобы оставаться 
незамеченным, когда у него нет цели, и выпрыгивает, как только находит добычу.


Их возросшая сила делает их более выносливыми противникаи, иногда способными пробить щит своего врага.
Как лидеры, Наккеры Воины так же агрессивно управляют своими стаями, делая их более грозными.

;;;;;

Старайтесь сосредоточиться на этом, сражаясь с роем Наккеров; {#0A880E}без лидера Наккеры теряют большую часть своей силы.{}


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
                                        tcots-witcher:nekker_eye
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
                                        tcots-witcher:nekker_heart
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
                                        tcots-witcher:nekker_warrior_mutagen
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




![Наккер Воин](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/nekker_warrior/nekker_warrior_full.png,fit)