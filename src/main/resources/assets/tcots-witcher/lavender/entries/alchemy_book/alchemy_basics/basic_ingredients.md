```json
{
  "title": "Ingredients",
  "icon_sprite": "tcots-witcher:alchemy_almanac/category/basic_ingredients",
  "ordinal": 2
}
```

**{dark_green}Basic Ingredients{}**

*Pre-conjunction plants*
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
                                minecraft:sweet_berries
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
                                minecraft:glow_berries
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
                                minecraft:moss_block
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

        <!--Second Row-->
        <flow-layout direction="horizontal">
            <children>
                <stack-layout>
                    <children>
                        <item>
                            <stack>
                                minecraft:dandelion
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
                                minecraft:kelp
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
                                minecraft:brown_mushroom
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

        <!--Third Row-->
        <flow-layout direction="horizontal">
            <children>

                <stack-layout>
                    <children>
                        <item>
                            <stack>
                                minecraft:red_mushroom
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
                                minecraft:glow_lichen
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

        <!--Fourth Row-->
        <flow-layout direction="horizontal">
            <children>
                <stack-layout>
                    <children>
                        <item>
                            <stack>
                                minecraft:flowering_azalea
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
                                minecraft:beetroot
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
                                minecraft:fern
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

---

**{dark_green}Basic Ingredients{}**

*Post-conjunction plants*
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
                                        tcots-witcher:celandine
                                    </stack>
<tooltip-text>Celandine:
Grows in Plains, Forests,
and Meadows.</tooltip-text>
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
                                        tcots-witcher:crows_eye
                                    </stack>
<tooltip-text>Crow's Eye: 
Grows in Taigas, Snowy Taigas
and Groves.</tooltip-text>
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
                                        tcots-witcher:arenaria
                                    </stack>
<tooltip-text>Arenaria:
Grows in Taigas, Snowy Taigas
and Flower Forests.</tooltip-text>                                    
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
                                        tcots-witcher:allspice
                                    </stack>
<tooltip-text>Allspice:
Bought from herbalist
villagers.</tooltip-text>
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

                <!--Second Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:bryonia
                                    </stack>
<tooltip-text>Bryonia:
Grows in caves, sometimes
grows at surface level.</tooltip-text>
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
                                        tcots-witcher:ergot_seeds
                                    </stack>
<tooltip-text>Ergot Seeds:
Harvested from wheat,
similar to a poisonous potato.</tooltip-text>
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
                                        tcots-witcher:verbena
                                    </stack>
<tooltip-text>Verbena:
Grows in Plains, Flower Forests, 
Forests and Meadows.</tooltip-text>
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
                                        tcots-witcher:han_fiber
                                    </stack>
<tooltip-text>Han Fiber:
Grows in Swamps, Jungles 
and Savannas.</tooltip-text>
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

                <!--Third Row-->
                <flow-layout direction="horizontal">
                    <children>
                        
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:puffball
                                    </stack>
<tooltip-text>Puffball:
Grows in Taiga, Swamps 
and caves.</tooltip-text>
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
                                        tcots-witcher:sewant_mushrooms
                                    </stack>
<tooltip-text>Sewant Mushrooms:
Grows in Taigas, Dark Forests 
and caves.</tooltip-text>
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

---

;;;;;

---

**{dark_green}Basic Ingredients{}**

*Flower petals*

All of them can be harvested using shears in the respective plant
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
                                        tcots-witcher:poppy_petals
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
                                        tcots-witcher:allium_petals
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
                                        tcots-witcher:lily_of_the_valley_petals
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
                                        tcots-witcher:peony_petals
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

                <!--Second Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:azure_bluet_petals
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
                                        tcots-witcher:oxeye_daisy_petals
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
                                        tcots-witcher:bunch_of_leaves
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

---

;;;;;

---

**{dark_green}Advanced Ingredients{}**

*Alcohol*
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
                                        tcots-witcher:icy_spirit
                                    </stack>
<tooltip-text>Icy Spirit:
Bought from herbalist 
villagers.</tooltip-text>
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
                                        tcots-witcher:dwarven_spirit
                                    </stack>
<tooltip-text>Dwarven Spirit:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:alcohest
                                    </stack>
<tooltip-text>Alcohest:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:village_herbal
                                    </stack>
<tooltip-text>Village Herbal:
Bought from farmer
villagers.</tooltip-text>
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

                <!--Second Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:mandrake_cordial
                                    </stack>
<tooltip-text>Mandrake Cordial:
Bought from herbalist
villagers.</tooltip-text>
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
                                        tcots-witcher:cherry_cordial
                                    </stack>
<tooltip-text>Cherry Cordial:
Bought from herbalist
villagers.</tooltip-text>
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

---

;;;;;

---

**{dark_green}Advanced Ingredients{}**

*Pre-conjunction substances*
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
                                minecraft:gunpowder
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
                                minecraft:blaze_powder
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
                                minecraft:glowstone_dust
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
                                minecraft:bone_meal
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

        <!--Second Row-->
        <flow-layout direction="horizontal">
            <children>
                <stack-layout>
                    <children>
                        <item>
                            <stack>
                                minecraft:prismarine_crystals
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
                                minecraft:ghast_tear
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
                                minecraft:amethyst_shard
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

---

;;;;;

---

**{dark_green}Advanced Ingredients{}**

*Post-conjunction substances*
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
                                        tcots-witcher:aether
                                    </stack>
<tooltip-text>Aether:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:vitriol
                                    </stack>
<tooltip-text>Vitriol:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:vermilion
                                    </stack>
<tooltip-text>Vermilion:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:hydragenum
                                    </stack>
<tooltip-text>Hydragenum:
Crafted, see recipes.</tooltip-text>
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

                <!--Second Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:rubedo
                                    </stack>
<tooltip-text>Rubedo:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:quebrith
                                    </stack>
<tooltip-text>Quebrith:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:rebis
                                    </stack>
<tooltip-text>Rebis:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:nigredo
                                    </stack>
<tooltip-text>Nigredo:
Crafted, see recipes.</tooltip-text>
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

                <!--Third Row-->
                <flow-layout direction="horizontal">
                    <children>
                        <stack-layout>
                            <children>
                                <item>
                                    <stack>
                                        tcots-witcher:monster_fat
                                    </stack>
<tooltip-text>Monster Fat:
Obtained from Ravagers, Hoglins,
Zoglins, Polar Bears and Piglins
Brutes.</tooltip-text>
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
                                        tcots-witcher:alchemy_paste
                                    </stack>
<tooltip-text>Alchemy Paste:
Bought from herbalist
villagers.</tooltip-text>
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
                                        tcots-witcher:stammelfords_dust
                                    </stack>
<tooltip-text>Stammelford's Dust:
Crafted, see recipes.</tooltip-text>
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
                                        tcots-witcher:alchemists_powder
                                    </stack>
<tooltip-text>Alchemists' Powder:
Bought from herbalist
villagers.</tooltip-text>
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

---

```xml owo-ui
        <flow-layout direction="vertical">
    <children>
    </children>
</flow-layout>
```
