```json
{
  "title": "Болотник",
  "icon": "tcots-witcher:scurver_spine",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:scurver_spine"
  ],
  "ordinal": 7
}
```

Болотники - это дальние родственники [гнильцов](^tcots-witcher:monsters/necrophages/rotfiend).
В битве с ними нельзя забывать об их острых, как бритва, костяных шипах -  
выступах, торчащих из их скелета.


Когда Болотник близок к смерти, газы и ферменты, накопившиеся в его теле, вызывают
взрыв.   


;;;;;

Шипы вылетают с огромной скоростью, превращаясь в последнее смертоносное оружие в их арсенале.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- Джунгли (ночью)

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_exploding.png,fit)

;;;;;

{red}**Поведение**{}

По своей сути болотники являются более сильной версией гнильцов.


Их главное отличие - шипы, которые растут по всему телу. Если один из них попадёт в вас,
то сильно ранит и заставит истекать кровью, поэтому обязательно используйте щит, когда это произойдёт, чтобы защититься
от результата взрыва Болотника (или быстро отбегите на безопасное расстояние).

;;;;;

Если у вас получится {#0A880E}убить чудовище, когда оно будет гореть, вы сможете забрать его шипы как трофей и использовать их в качестве оружия.{}


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
                                        tcots-witcher:rotfiend_blood
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
                                        tcots-witcher:scurver_spine
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




![Болотник](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/scurver/scurver_full.png,fit)
