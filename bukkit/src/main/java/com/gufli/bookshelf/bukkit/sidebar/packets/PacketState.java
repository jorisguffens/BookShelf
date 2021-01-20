package com.gufli.bookshelf.bukkit.sidebar.packets;

public enum PacketState {

    /**
     * The objective was created
     */
    CREATE,

    /**
     * The objective was removed
     */
    REMOVE,

    /**
     * The display name of the objective was changed
     */
    UPDATE_DISPLAY_NAME;

    /**
     * Get the enum from his id
     *
     * @param stateId the id
     * @return the representing enum or null if the id not valid
     */
    public static PacketState fromId(int stateId) {
        return PacketState.values()[stateId];
    }
}