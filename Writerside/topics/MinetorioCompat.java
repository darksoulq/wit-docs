public static boolean handleBlock(Block block, Player player) {
    Info info = new Info();
    Device device = MTAPI.getDevice(block.getLocation());
    Mover mover = Mover.getMover(block.getLocation());
    String deviceName;

    float progress = 0;
    if (WITMinetorio.CONFIG.getBoolean("block-progress")) {
        if (Events.progressMap.containsKey(block)) {
            progress = Events.progressMap.get(block);
        }
    }

    if (device != null || mover != null) {
        if (mover != null) {
            deviceName = "Mover";
        } else {
            deviceName = device.getName();
        }
        for (Function<Block, Component> func : suffixBlock) {
            info.addSuffix(func.apply(block));
        }
        for (Function<Block, Component> func : prefixBlock) {
            info.addPrefix(func.apply(block));
        }
        if (!((TextComponent) info.getPrefix()).content().isEmpty()) {
            info.addPrefix(mm.deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
        }
        info.setName(Component.text(deviceName));
        if (!((TextComponent) info.getSuffix()).content().isEmpty()) {
            info.suffixSplit(mm.deserialize(Information.getValuesFile().getString("SPLITTER", " §f| ")));
        }
        API.updateBar(info, 1 - progress, player);
        return true;
    }
    return false;
}