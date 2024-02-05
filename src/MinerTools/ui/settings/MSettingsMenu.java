package MinerTools.ui.settings;

import MinerTools.graphics.*;
import MinerTools.ui.tables.*;
import arc.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;

import static mindustry.ui.Styles.clearNoneTogglei;

public class MSettingsMenu extends Table implements Addable{
    private final Table settingTableCont = new Table();
    public Seq<MSettingTable> settingTables = new Seq<>();
    public MSettingTable modules, graphics, ui;
    private MSettingTable select;

    public MSettingsMenu(){
        addSettings();

        setup();
    }

    @Override
    public void addUI(){
        /* Use the setting category */
        Vars.ui.settings.addCategory("MinerTools", t -> t.add(this));
    }

    public void addSettings(){
        modules = new MSettingTable(Icon.list, "modules"){
        };

        graphics = new MSettingTable(Icon.image, "graphics"){
            {
                addCategory("unit", setting -> {
                    drawerCheck(setting, "enemyUnitIndicator", true);
                    drawerRadiusSlider(setting, "enemyUnitIndicatorRadius", 100, 25, 250);

                    drawerCheck(setting, "unitAlert", true);
                    drawerRadiusSlider(setting, "unitAlertRadius", 10, 5, 50);

                    drawerCheck(setting, "unitInfoBar", true);
                });

                addCategory("build", setting -> {
                    drawerCheck(setting, "turretAlert", true);
                    drawerRadiusSlider(setting, "turretAlertRadius", 10, 5, 50);

                    drawerCheck(setting, "itemTurretAmmoShow", true);

                    drawerCheck(setting, "overdriveZone", true);

                    setting.addCategory("info", info -> {
                        drawerCheck(info, "buildStatus", true);
                        drawerCheck(info, "buildHealthBar", true);

                        drawerCheck(info, "constructBuildInfo", true);
                        drawerCheck(info, "unitBuildInfo", true);
                    });
                });

                addCategory("select", setting -> {
                    drawerCheck(setting, "buildSelectInfo", true);
                    drawerCheck(setting, "itemBridgeLinksShow", true);
                });

                addCategory("player", setting -> {
                    drawerCheck(setting, "payloadDropHint", true);
                    drawerCheck(setting, "playerRange", true);
                });
            }

            public static void drawerCheck(MSettingTable table, String name, boolean def){
                table.checkPref(name, def, b -> MRenderer.updateEnable());
            }

            public static void drawerRadiusSlider(MSettingTable table, String name, int def, int min, int max){
                table.sliderPref(name, def, min, max, s -> {
                    MRenderer.updateSettings();
                    return s + "(Tile)";
                });
            }
        };

        ui = new MSettingTable(Icon.chat, "ui");

        settingTables.addAll(modules, graphics, ui);
    }

    private void setup(){
        top();

        table(t -> {
            t.table(table -> {
                table.add("MinerToolsSettings").center();
            }).growX().row();

            t.image().color(Pal.accent).minWidth(550f).growX();

            t.row();

            t.table(buttons -> {
                for(MSettingTable settingTable : settingTables){
                    buttons.button(settingTable.icon(), clearNoneTogglei, () -> {
                        settingTableCont.clear();

                        if(select != settingTable){
                            select = settingTable;
                            settingTableCont.add(settingTable).left();
                        }else{
                            select = null;
                        }
                    }).padLeft(4f).size(70f, 68f).checked(b -> select == settingTable);
                }
            }).pad(5f);
        }).top();

        row();

        add(settingTableCont).minSize(Core.graphics.getHeight() / 3f).top();
    }
}