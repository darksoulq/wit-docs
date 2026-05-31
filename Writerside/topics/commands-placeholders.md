# Commands and Permissions
<link-summary>Reference for all WIT commands and their required permissions</link-summary>

The WIT plugin provides several commands for players to customize their own display preferences, as well as an administrative command to reload configurations and addons.

All player-facing commands are nested under the `/wit` base command.

### `/wit toggle`
Toggles the WIT display on or off for the executing player. When disabled, the player will no longer see information about the blocks or entities they look at, and they are removed from the internal tracking system to save server resources.
* **Permission:** `wit.default`

### `/wit type <type>`
Switches the visual display method for the executing player. The available types are drawn dynamically from the registered `InfoDisplay` manager.
* **Permission:** `wit.default`
* **Default Options:** `bossbar`, `actionbar` (Additional options appear here if addons register Custom Displays).

### `/wit progress <mode>`
Changes how progress (such as block breaking or entity health) is rendered. **This only applies to the `actionbar` display type**, as the BossBar has native progress rendering.
* **Permission:** `wit.default`
* **Modes:**
    * `OFF`: Hides the progress indicator entirely.
    * `BAR`: Displays a miniature pipe bar (e.g., `[|||||||   ]`).
    * `PERCENT`: Displays a raw percentage (e.g., `(75%)`).
    * `UNDERLINE`: Uses an underlined space trick to simulate a continuous bar.

### `/wit reload`
Reloads the `config.yml`, `values.yml`, all default handlers, item groups, and fires the reload callback for all registered `WITPlugin` addons.
* **Permission:** `wit.reload`

---

# PlaceholderAPI Integration
<link-summary>Using WIT data in other plugins via PlaceholderAPI</link-summary>

WIT ships with native support for [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI). If PlaceholderAPI is installed on your server, WIT will automatically register its expansion, allowing you to use WIT's contextual information inside other plugins (like Scoreboards or Chat).

All placeholders use the `%wit_<placeholder>%` format and require a player context to evaluate what they are currently looking at.

### Text Placeholders
Because WIT relies heavily on MiniMessage and Adventure components for colored and styled text, **these placeholders return raw MiniMessage strings** (e.g., `<gold>Super Furnace</gold>`). The plugin consuming the placeholder must support parsing MiniMessage tags for the colors to display correctly.

<table>
<tr>
<th>Placeholder</th>
<th>Description</th>
</tr>
<tr>
<td><code>&percnt;wit_looking_at&percnt;</code></td>
<td>Returns the core name of the block or entity the player is currently looking at.</td>
</tr>
<tr>
<td><code>&percnt;wit_looking_at_prefix&percnt;</code></td>
<td>Returns only the prefix information (e.g., ownership or tool requirements).</td>
</tr>
<tr>
<td><code>&percnt;wit_looking_at_suffix&percnt;</code></td>
<td>Returns only the suffix information (e.g., block states, crop age, or health).</td>
</tr>
<tr>
<td><code>&percnt;wit_looking_at_info&percnt;</code></td>
<td>Returns the fully combined string (Prefix + Name + Suffix), exactly as it appears on the player's WIT display.</td>
</tr>
</table>

### Settings Placeholders
These placeholders return the player's current internal WIT configuration.

<table>
<tr>
<th>Placeholder</th>
<th>Description</th>
</tr>
<tr>
<td><code>&percnt;wit_info_type&percnt;</code></td>
<td>Returns the string ID of the display type the player is currently using (e.g., <code>bossbar</code> or <code>actionbar</code>).</td>
</tr>
<tr>
<td><code>&percnt;wit_info_state&percnt;</code></td>
<td>Returns <code>enabled</code> if the player has WIT turned on, or <code>disabled</code> if they have toggled it off.</td>
</tr>
</table>