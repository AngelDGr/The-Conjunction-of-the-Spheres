```json
{
"title": "Циклопы",
"icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/ogroids/cyclops",
"category": "tcots_witcher:ogroids",
"associated_items": [

],
"ordinal": 3
}
```

Циклопа можно легко узнать по одному глазу, расположенному в центре их лба. 

Если по какой-то причине глаза не видно, то ещё один признак - это их огромные размеры, 
невероятная сила и кипящая ко всем людям ненависть.

Циклопы идут туда, куда хотят; любому храбрецу стоит отойти в сторону, если циклоп несётся вперёд.

;;;;;

Они не страшатся того, что с ними может сделать клинок воина, и продолжают сражаться, не смотря на колличество кровоточащих ран.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/cyclops/main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Горы
- Древние массивы тайги
- Снежные равнины

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/cyclops/secondary.png,fit)

;;;;;

{red}**Поведение**{}

Циклоп - крупное существо и, несмотря на размеры, слабоумное... За то мощные мышцы дают ему силу наносить сокрушительный урон.

На дальних дистанциях циклоп может высоко прыгнуть и при падении уничтожить всё живое в зоне приземления.


Циклопы - истинные одиночки, они безжалостно убивают себе подобных, не колеблясь.

;;;;;

Во время сражения с циклопом лучше держаться на средней дистанции. Если он начнёт прыгать, либо поднимите щит, что с небольшой вероятностью поможет вам выжить, либо бегите изо всех сил, в попытках спасти свою жизнь.


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
                                        minecraft:leather
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
                                        minecraft:rabbit_hide
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




![Циклопы](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/ogroid/cyclops/full.png,fit)