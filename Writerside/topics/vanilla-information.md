# Custom Vanilla Information
<link-summary>Injecting custom prefixes and suffixes into the default vanilla block and entity handlers</link-summary>

We have covered how to completely intercept and take over the display for a block or entity using Custom Handlers. However, if you return `true` in a custom handler, the player loses all default vanilla information (like break progress, crop age, or entity health).

If you just want to append a small piece of custom information to the *existing* vanilla display, you can inject functions directly into the default `MinecraftCompat` handler.

### Injecting Custom Information
The `MinecraftCompat` class maintains lists of functions that run every time a vanilla block or entity is looked at.

You can add your own logic to these lists to supply additional `Component` prefixes or suffixes.

* `MinecraftCompat.getBlockPrefix()`
* `MinecraftCompat.getBlockSuffix()`
* `MinecraftCompat.getEntityPrefix()`
* `MinecraftCompat.getEntitySuffix()`

<note>
Because the default <code>Info</code> object ignores empty text components, you should return <code>Component.empty()</code> whenever you do not want your custom information to appear on a specific block or entity.
</note>

---

### Example: Adding a Block Suffix
Let's say you have a custom plugin that applies "Radiation" to certain blocks, and you want players to see the radiation level when they look at them, alongside the standard vanilla break progress.

```Java
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.block.Block;

public void registerCustomHandlers() {
    // Inject a suffix function for blocks
    MinecraftCompat.getBlockSuffix().add((block) -> {
        
        // Example check: Does this block have custom radiation data?
        int radiationLevel = MyRadiationPlugin.getRadiation(block);
        
        if (radiationLevel > 0) {
            // Return the component to be appended to the UI
            return Component.text("Radiation: " + radiationLevel + " rads", NamedTextColor.GREEN);
        }
        
        // If it doesn't have radiation, return an empty component so nothing is added
        return Component.empty(); 
    });
}
```

---

### Example: Adding an Entity Prefix
Similarly, you can inject information before the name of an entity. This is commonly used for showing things like Factions, Guilds, or custom ownership.

```Java
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public void registerCustomHandlers() {
    // Inject a prefix function for entities
    MinecraftCompat.getEntityPrefix().add((entity) -> {
        
        // Check if the entity is a player
        if (entity instanceof Player targetPlayer) {
            String guildName = MyGuildPlugin.getGuild(targetPlayer);
            
            if (guildName != null) {
                return Component.text("[" + guildName + "] ", NamedTextColor.AQUA);
            }
        }
        
        // Not a player, or no guild found
        return Component.empty();
    });
}
```

### Reloading Considerations
Just like Custom Handlers, the `MinecraftCompat` lists are cleared whenever a server administrator runs `/wit reload` to ensure old configurations and caches are purged.

Make sure you are registering your block and entity injection functions inside the `onWITReload()` method of your `WITPlugin` addon so they persist after a reload!