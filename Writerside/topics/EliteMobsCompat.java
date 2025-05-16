public static boolean handleEntity(Entity entity, Player player) {
    if (MTAPI.isBiter(entity)) {
        Info info = new Info();
        info.setName(Component.text("Biter"));
        float health = 0;
        if (WITMinetorio.CONFIG.getBoolean("biter-health-progress")) {
            if (entity instanceof LivingEntity) {
                health = (float) Math.max(0f, Math.min(1f, ((LivingEntity) entity).getHealth() /
                        ((LivingEntity) entity).getAttribute(Attribute.MAX_HEALTH).getValue()));
            }
        }
        for (Function<Entity, Component> func : suffixEntity) {
            info.addSuffix(func.apply(entity));
        }
        API.updateBar(info, 1 - health, player);
    }
    return false;
}