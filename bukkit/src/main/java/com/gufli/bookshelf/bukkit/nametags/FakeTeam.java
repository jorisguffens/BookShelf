package com.gufli.bookshelf.bukkit.nametags;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FakeTeam {

    private final Set<String> members = new HashSet<>();

    private final String id;
    private final String prefix;
    private final String suffix;

    public FakeTeam(String id, String prefix, String suffix) {
        this.id = id;
        this.prefix = prefix == null ? "" : prefix;
        this.suffix = suffix == null ? "" : suffix;
    }

    public void addMember(String player) {
        members.add(player);
    }

    public void removeMember(String player) {
        members.remove(player);
    }

    public Set<String> members() {
        return Collections.unmodifiableSet(members);
    }

    public String id() { return id; }

    public String prefix() {
        return prefix;
    }

    public String suffix() {
        return suffix;
    }

    public boolean isSimilar(String prefix, String suffix) {
        return this.prefix.equals(prefix) && this.suffix.equals(suffix);
    }

}