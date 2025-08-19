```json
{
  "title": "Bloedzuiger",
  "icon_sprite": "tcots_witcher:witcher_bestiary/category/monsters/necrophages/bloedzuiger",
  "category": "tcots_witcher:necrophages",
  "associated_items": [
    "tcots_witcher:bloedzuiger_blood"
  ],
  "ordinal": 9
}
```

Travellers crossing swamps can be sure to encounter both [drowners](^tcots_witcher:monsters/necrophages/drowner) and bloedzuigers. 


The bloedzuiger feed on blood, but their gullets are particularly large and their stomachs are filled with acid, 
so they suck and digest both their victims' blood and intestines, causing terror among peasants.

;;;;;

His thick body contains great amount of cadaverine, causing a toxic explosion when killed.

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/bloedzuiger/bloedzuiger_main.png,fit)

;;;;;

{dark_green}**Habitat**{}
- Swamps
- Mangrove Swamps

![](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/bloedzuiger/bloedzuiger_special.png,fit)

;;;;;

{red}**Behavior**{}

A creeping horror with a corrosive bite. 
When it attacks, it can coat its prey with a vile acid called cadaverine, an acid that decays rapidly both flesh and armor.


Like the [rotfiend](^tcots_witcher:monsters/necrophages/rotfiend), the bloedzuiger is a walking bomb: when wounded enough, its body violently explodes, releasing a cloud of deadly cadaverine that harms all nearby monsters.

;;;;;

A cunning hunter knows how to turn this explosive weakness into a weapon, luring groups of necrophages into the blast to wipe them out in one fell swoop.
But if the warrior wants to prevent this explosion they need to {#0A880E}killing it when the creature it's in flames{}.


{blue}**Loot**{}
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
                                        tcots_witcher:bloedzuiger_blood
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




![Bloedzuiger](tcots_witcher:textures/gui/sprites/witcher_bestiary/entries/bloedzuiger/bloedzuiger_full.png,fit)