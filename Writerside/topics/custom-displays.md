# Custom Displays
<link-summary>Guide to creating custom UI rendering methods and overriding default displays</link-summary>

By default, WIT provides a BossBar and an ActionBar to display information to the player. However, you can create entirely custom displays (such as titles, scoreboards, or chat messages) by extending the `InfoDisplay` class and registering it.

### Creating a Custom Display
To create a custom display, you must extend `InfoDisplay` and provide a unique string ID in the constructor. You must then implement the methods that WIT calls when a player looks at a block or entity.

Here is an example of a custom display that sends a Title to the player's screen:

```Java
import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.display.InfoDisplay;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import java.time.Duration;

public class TitleDisplay extends InfoDisplay {

    public TitleDisplay() {
        // The ID players will use in `/wit type <id>`
        super("title"); 
    }

    @Override
    public void setBar(Player player, Info info) {
        if (info == null || info.getName() == null) {
            removeBar(player);
            return;
        }

        // Show the name as the main title, and prefix/suffix as the subtitle
        Title title = Title.title(
            info.getName(),
            info.getPrefix().append(info.getSuffix()),
            Title.Times.times(Duration.ZERO, Duration.ofSeconds(2), Duration.ofSeconds(1))
        );

        player.showTitle(title);
    }

    @Override
    public void setProgress(Player player, float value) {
        // Titles don't naturally support progress bars, so we leave this empty.
        // If you were building a Scoreboard display, you would update the score here.
    }

    @Override
    public void setColor(Player player, TextColor color) {
        // We handle colors directly in the Info components, so this can be ignored.
    }

    @Override
    public void removeBar(Player player) {
        player.clearTitle();
    }

    @Override
    public boolean isEmpty(Player player) {
        return false;
    }
}
```

### Registering the Display
Register your custom display during your addon's startup using `API.addDisplay()`. Remember to register this inside your `onWITReload()` method as well!

```Java
import com.github.darksoulq.wit.api.API;

public void registerDisplays() {
    API.addDisplay(new TitleDisplay());
}
```

Players can now switch to your custom display in-game by typing `/wit type title`.

---

# Default Displays
<link-summary>Reference for the built-in BossBar and ActionBar displays</link-summary>

WIT ships with two built-in display types that handle the `Info` object, colors, and progress floats differently.

### BossBar (`bossbar`)
* **Text:** Renders the `getCombined()` component (Prefix + Name + Suffix) as the title of the boss bar.
* **Progress:** Visually fills or depletes the boss bar.
* **Color:** Maps standard Adventure `TextColor` hexes to the closest vanilla `BossBarColor` (Red, Blue, Green, Yellow, Purple, White).

### ActionBar (`actionbar`)
* **Text:** Renders the `getCombined()` component above the hotbar.
* **Progress:** Renders based on the player's `/wit progress <mode>` setting.
    * `OFF`: Hides progress entirely.
    * `PERCENT`: Appends a percentage (e.g., `(75%)`).
    * `BAR`: Appends a miniature pipe bar (e.g., `[|||||||   ]`).
    * `UNDERLINE`: Uses an underlined space trick to simulate a continuous bar.
* **Color:** Applies the provided `TextColor` to the appended progress UI.
