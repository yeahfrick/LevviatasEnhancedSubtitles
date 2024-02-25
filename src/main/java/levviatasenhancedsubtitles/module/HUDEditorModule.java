package levviatasenhancedsubtitles.module;

import levviatasenhancedsubtitles.setting.BooleanSetting;
import levviatasenhancedsubtitles.setting.KeybindSetting;
import org.lwjgl.input.Keyboard;


public class HUDEditorModule extends Module {
	public static final BooleanSetting showHUD=new BooleanSetting("Show HUD Panels","showHUD","Whether to show the HUD panels in the ClickGUI.",()->true,true);
	public static final KeybindSetting keybind=new KeybindSetting("Keybind","keybind","The key to toggle the module.",()->true,Keyboard.KEY_P);
	
	public HUDEditorModule() {
		super("HUDEditor","Module containing HUDEditor settings.",()->true,false);
		settings.add(showHUD);
		settings.add(keybind);
	}
}
