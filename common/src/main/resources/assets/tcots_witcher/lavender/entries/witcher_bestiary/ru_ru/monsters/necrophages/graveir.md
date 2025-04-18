```json
{
  "title": "Грайвер",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/graveir",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:graveir_bone"
  ],
  "ordinal": 9
}
```

Грайверы - развратные, вероломные и коварные ублюдки.
Они одни из самых крупных падальщиков, 
у них есть несколько костяных гребней на голове и короткие, и тупые когти.


Их зубы и тонкий язык позволяют им есть костный мозг - чем костный мозг более гнилой и прогорьклый, 
тем больше он им нравится.


;;;;;

У мерзких Грайверов в зубах содержится трупный яд. Так что, если вы встретитесь с ними в бою, остерегайтесь их клыков.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Под землёй
- В тёмных пещерах

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_toxic.png,fit)

;;;;;

{red}**Поведение**{}

Грайвер - грозный противник, способный победить опытных воинов мощными ударами, наносящими значительный урон.

Они могут наносить трупный яд как кислоту на свою цель, быстро растворяя их броню и отравляя. 


Его толстая кожа обеспечивает броню и высокую устойчивость к взрывам, что делает его более непобедимым.

;;;;;

Несмотря на огромную силу, {#0A880E}они медленно передвигаются, что даёт приемущество опытным бойцам 
в победе над этим грозным противником.{}


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
                                        tcots_witcher:graveir_bone
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




![Грайвер](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/graveir/graveir_full.png,fit)

