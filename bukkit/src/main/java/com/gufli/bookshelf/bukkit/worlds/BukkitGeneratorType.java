package com.gufli.bookshelf.bukkit.worlds;

public enum BukkitGeneratorType {
    DEFAULT("default", false),
    FLAT("flat", false),
    EMPTY("CleanroomGenerator:.", true);

    private String generator;
    private boolean custom;

    BukkitGeneratorType(String generator, boolean custom) {
        this.generator = generator;
        this.custom = custom;
    }

    public String getGenerator() {
        return this.generator;
    }

    public boolean isCustom() {
        return custom;
    }

    public static BukkitGeneratorType getGenerator(String v) {
        for ( BukkitGeneratorType gentype : values() ) {
            if ( gentype.name().equalsIgnoreCase(v) || gentype.getGenerator().equalsIgnoreCase(v) ) {
                return gentype;
            }
        }
        return null;
    }

}
