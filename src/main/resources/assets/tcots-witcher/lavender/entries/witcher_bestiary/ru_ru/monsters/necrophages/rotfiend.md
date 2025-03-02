```json
{
  "title": "Гнилец",
  "icon": "tcots-witcher:rotfiend_blood",
  "category": "tcots-witcher:necrophages",
  "associated_items": [
    "tcots-witcher:rotfiend_blood"
  ],
  "ordinal": 3
}
```

Гнильцы напоминают разлагающееся человеческое тело, с которого сняли кожу. 


Понять что рядом гнилец не сложно - их выдаёт непреодолимая вонь гнили, которая и дала им название.
Они охотятся большими группами, а учитывая их скорость - они являются большой опасностью для одиноких путешественников.

;;;;;

Будь осторожен, путешественник, эти существа представляют огромную угрозу для любого неопытного воина.

![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rotfiend/rotfiend_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- Тёмные пещеры

  ![](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rotfiend/rotfiend_exploding.png,fit)

;;;;;

{red}**Поведение**{}

Гнильцы скрываются под землёй и нападают, когда находят цель. Будьте осторожны, они могут застать вас врасплох.
Они быстрые и опасные. Попытаются броситься на вас, так что будьте быстрее, чем они.


Помните об их худшей черте: они очень взрывоопасны,
Перед смертью тело гнильца начинает неконтроллируемо дрожать, что приводит к большому взрыву.

;;;;;

Если вы будете рядом во время взрыва - будет очень больно, но он может убить и гнильцов рядом, вызвав цепную реакцию.


Единственный способ предотвратить этот взрыв - это {#0A880E}убить гнильца, когда он горит.{}


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




![Гнилец](tcots-witcher:textures/gui/sprites/witcher_bestiary/entries/rotfiend/rotfiend_full.png,fit)