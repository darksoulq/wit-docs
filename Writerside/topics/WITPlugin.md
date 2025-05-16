# WITPlugin

To make you addon, you must implement WITPlugin in any of your classes (Main class recommended)

## Step 1: Implement the WITPlugin class:

```Java
public final class MyPlugin extends JavaPlugin implements WITPlugin {
    @Override
    public void onWITReload() {
    // Register stuff here, and call functions that you want to also run on /wit reload.
    }
} 
```

Thats basically it, in the next ones you will see how to register handlers, you need to register the handlers in the above overriden method, or they will be wiped on /wit reload.