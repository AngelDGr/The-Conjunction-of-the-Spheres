```json
{
  "title": "Альгуль",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/alghoul",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:alghoul_bone_marrow"
  ],
  "ordinal": 3
}
```

Альгули отличаются от обычных [гулей](^tcots_witcher:monsters/necrophages/ghoul) размером, силой, окрасом и, самое важное - интеллектом. Если, гули неспособны 
спланировать самую простую засаду, то альгули весьма умны и, гораздо более опасны.

Гули, похоже, способны распознать более разумное существо, когда они его видят, и поэтому позволяют альгулям вести свои стаи.

;;;;;

При встрече с такой стаей в первую очередь стоит убить альгуля, оставив остальных чудовищ без вожака. Так уничтожить их будет гораздо проще.
![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/alghoul/main.png,fit)

;;;;;

{dark_green}**Среда обитания**{}
- Леса (ночью)
- Равнины (ночью)
- [Гнездо альгуля](^tcots_witcher:misc/monster_nests), найденое на равнине или в лесу.
  ![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/alghoul/secondary.png,fit)

;;;;;

{red}**Поведение**{}

Альгуль - сильная версия [гуля](^tcots_witcher:monsters/necrophages/ghoul), так что, они очень похожи.


Главное отличие в том, что когда они регененрируют, их крик становится настолько мощным, что отталкивает любого монстра поблизости.


После регенерации у них вырастают шипы, и после этого любой урон, нанесённый в ближнем бою, будет наносить урон и атакующему.

;;;;;

Если альгуль выпустил шипы или находится в центре боя, он также может разъярить обычных гулей, которые находятся рядом с ним,
поэтому {#0A880E}лучше сосредоточиться на том, чтобы убить его раньше других гулей.{}


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
                                        tcots_witcher:ghoul_blood
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
                              tcots_witcher:alghoul_bone_marrow
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




![Альгуль](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/necrophage/alghoul/full.png,fit)