```json
{
  "title": "Наккер",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/nekker",
  "category": "tcots_witcher:ogroids",
  "associated_items": [
    "tcots_witcher:nekker_heart",
    "tcots_witcher:nekker_eye"
  ],
  "ordinal": 1
}
```

Одинокий наккер безвреден. Пятеро опасны. Десять могут убить даже матёрого истребителя чудовищ.


Основная тактика наккеров заключается в массированном нападении.
Может сложиться впечатление, что они появляются из ниоткуда, ведь в основном, они прячутся под землёй. 
Несмотря на примитивность, эта тактика на удивление эффективна.

;;;;;

При встрече с одним, нужно быть готовым дать отпор сразу многим врагам.
Они живут в гнёздах по несколько особей.
Так что, если вы нашли только одного - найдутся ещё.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса
- Равнины
- [Гнёзда чудовищ](^tcots_witcher:misc/monster_nests), встречаются на равнинах или в лесах
  ![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_lunging.png,fit)

;;;;;

{red}**Поведение**{}

Наккеров стоит остерегаться. Пока у него нет цели, он будет погребён под землёй в ожидании добычи.
Благодаря своим длинным когтям, он отлично копает и быстро
атакует, а из-за своих крошечных размеров может стать настоящей проблемой.


Если вы будете убегать, Наккер бросится на вас с огромной силой. Такая атака довольно распространена среди этих проворных существ.

;;;;;

Избегайте большого скопления этих крошечных существ, и вы легко справитесь с ними.


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
                                        tcots_witcher:nekker_eye
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
                                        tcots_witcher:nekker_heart
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




![Наккер](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/nekker/nekker_full.png,fit)
