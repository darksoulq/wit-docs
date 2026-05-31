# Custom Handlers
<link-summary>Guide to creating custom block and entity target handlers and managing addon lifecycles</link-summary>

The WIT (What Is That) API allows you to intercept what a player is looking at and provide entirely custom information to their display. This is incredibly useful for custom machines, custom entities, or blocks with unique NBT data.

### Addon Lifecycle (Reloading)
When a server administrator runs `/wit reload`, all internal handlers and caches are cleared. If your plugin registers custom handlers, it needs to know when this reload happens so it can re-register them.

To do this, have your main plugin class (or a dedicated manager) implement `WITPlugin` and register it using `API.registerAddon()`.

```Java
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.WITPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MyWITAddon extends JavaPlugin implements WITPlugin {

    @Override
    public void onEnable() {
        // Register this class to listen for WIT reloads
        API.registerAddon(this);
        
        // Register your handlers for the first time
        registerCustomHandlers();
    }

    @Override
    public void onDisable() {
        API.unregisterAddon(this);
    }

    @Override
    public void onWITReload() {
        // This is called automatically when /wit reload is executed.
        // You should re-register your handlers here, as WIT clears its internal lists on reload.
        registerCustomHandlers();
    }

    private void registerCustomHandlers() {
        // We will define this in the next section
    }
}
```

---

### The Info Object
Whenever you handle a block or entity, you must construct an `Info` object. This object structures the text that will be sent to the player's screen.

* `setName(Component)`: The primary name of the target (e.g., "Nuclear Reactor").
* `addPrefix(Component)`: Adds information *before* the name. (Often used for tool requirements or ownership).
* `addSuffix(Component)`: Adds information *after* the name. (Often used for states, energy levels, or health).

If a prefix or suffix is a `TextComponent` and is completely empty, the `Info` object will automatically ignore it, preventing awkward empty spaces in the UI.

---

### Creating a Custom Handler
Handlers are registered as a `BiFunction<Block, Player, Boolean>` (or `Entity` instead of `Block`).

When a player looks at a target, WIT loops through all registered handlers.
1. If your handler returns `false`, WIT assumes you don't care about this target and moves to the next handler (eventually falling back to the default Minecraft handler).
2. If your handler returns `true`, WIT stops checking immediately. You are taking full responsibility for updating the player's display.

#### Example: Custom Block Handler
Here is an example of a handler that intercepts a specific block and displays custom energy information.

```Java
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.Info;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public void registerCustomHandlers() {
    API.addBlockHandler((block, player) -> {
        // 1. Check if this is the block we want to handle
        if (block.getType() != Material.FURNACE) {
            return false; // Not a furnace, let another handler deal with it
        }

        // 2. We found our block! Build the Info object
        Info info = new Info();
        info.setName(Component.text("Super Smelter", NamedTextColor.GOLD));
        info.addSuffix(Component.text("Speed: 200%", NamedTextColor.YELLOW));

        // 3. Force the UI update for the player viewing the block
        // You can use API.updateBar(info, player) for standard updates,
        // or provide a progress float and TextColor to manipulate the BossBar/ActionBar styling.
        API.updateBar(info, 1.0f, NamedTextColor.GOLD, player);

        // 4. Return true to tell WIT we successfully handled this block
        return true; 
    });
}
```

#### Example: Custom Entity Handler
Entity handlers work exactly the same way, but provide an `Entity` instead of a `Block`.

```Java
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.Info;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Cow;

public void registerCustomHandlers() {
    API.addEntityHandler((entity, player) -> {
        if (!(entity instanceof Cow cow)) {
            return false;
        }

        Info info = new Info();
        info.setName(Component.text("Magical Cow", NamedTextColor.LIGHT_PURPLE));
        info.addSuffix(Component.text("Milk Ready: " + !cow.isBaby(), NamedTextColor.WHITE));

        API.updateBar(info, player);
        return true;
    });
}
```