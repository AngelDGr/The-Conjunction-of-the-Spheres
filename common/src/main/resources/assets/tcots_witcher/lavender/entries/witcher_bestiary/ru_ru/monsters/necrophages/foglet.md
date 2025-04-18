```json
{
  "title": "Туманник",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/foglet",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:foglet_mutagen",
    "tcots_witcher:foglet_teeth"
  ],
  "ordinal": 4
}
```

Туманники могут появиться везде, где может возникнуть туман: если тумана нет, они вызовут его сами.
Быстро сгущающийся безветренный воздух безошибочно сигнализирует о присутствии туманника и подготовке к атаке.

У этих существ мощные руки и когти,
но что делает их по-настоящему опасными, так это мастерство обмана, 
запутывания и дезориентации.

;;;;;

Говорят, что туманники также обладают способностью создавать сложные иллюзии из
чистого тумана.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Болота (ночью)
- Леса (ночью, особенно тёмный лес)
- Горы (ночью)

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_fog.png,fit)

;;;;;

{red}**Поведение**{}

Туманник - опасный монстр-иллюзионист, при приближении к которому он начнёт
создавать вокруг себя густой туман, становясь невидимым и неуязвимым, сливаясь с туманом воедино.


В момент превращения он создаёт две иллюзии, 
не бойтесь этих иллюзий, так как их можно развеить одним точным ударом.

;;;;;

Туманник и две иллюзии будут атаковать вас и только когда они нанесут вам урон - станут видимыми и
снова уязвимыми, Воспользуйтесь моментом, чтобы {#0A880E}быстро убить туманника, пока он снова не растворился в тумане.{}

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
                                        tcots_witcher:foglet_teeth
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
                                        tcots_witcher:foglet_mutagen
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




![Туманник](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/foglet/foglet_full.png,fit)