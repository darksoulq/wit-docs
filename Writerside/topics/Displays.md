# Displays

Displays are used to show information to the players, for example the default bossbar and actionbar implementation.

<procedure title="Making your own Display" id="making_your_own_display">
<step>Make a class which will be our display and extend me.darksoul.whatIsThat.display.InfoDisplay</step>
<step>implement the classes functions
<code-block lang="Java">
public class YourDisplay extends InfoDisplay {
    public YourDisplay(/*your args*/) {
    // Your stuff if needed
    super("displayName");
    }
    
    @Override
    public void setBar(Player player, String text) {
        // implement this
    }

    @Override
    public void removeBar(Player player) {
        // implement this
    }
}
</code-block>
</step>
<step>
Now register the Display.
<code-block lang="Java">
WITAPI.addDisplay(new YourDisplay());
</code-block>
</step>
<step>
Example
<code-block lang="Java" src="./BossBarDisplay.java"></code-block>
<code-block lang="Java">
WAILAManager.addDisplay(new BossBarDisplay()); //Uses internal WAILAManager.java
</code-block>
</step>
</procedure>