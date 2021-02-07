package com.gufli.bookshelf.sidebar;

import com.gufli.bookshelf.entity.ShelfPlayer;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sidebar {

    private String title;
    private final List<String> lines = new ArrayList<>();

    private final Map<String, Function<ShelfPlayer, String>> replacers = new HashMap<>();

    public void addReplacer(String var, Function<ShelfPlayer, String> replacer) {
        replacers.put(var.toLowerCase(), replacer);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringEscapeUtils.unescapeJava(title);
    }

    public List<String> getLines() {
        return Collections.unmodifiableList(lines);
    }

    public void setLines(List<String> lines) {
        this.lines.clear();
        lines.forEach(line -> this.lines.add(StringEscapeUtils.unescapeJava(line)));
    }

    public void setLines(String... lines) {
        this.lines.clear();
        addLines(lines);
    }

    public void addLines(String... lines) {
        this.lines.clear();
        Arrays.asList(lines).forEach(line -> this.lines.add(StringEscapeUtils.unescapeJava(line)));
    }

    public List<String> getLines(ShelfPlayer player) {

        List<String> updated = new ArrayList<>();
        for ( String line : lines ) {
            updated.add(replace(player, line));
        }
        return updated;
    }

    private String replace(ShelfPlayer player, String str) {
        if ( str == null ) {
            return null;
        }

        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile("(\\{[^}]+\\})");
        Matcher m = p.matcher(str);

        while ( m.find() ) {
            String placeholder = m.group(1).toLowerCase();
            placeholder = placeholder.substring(1, placeholder.length()-1); // remove brackets { and }

            Function<ShelfPlayer, String> replacer = replacers.get(placeholder.toLowerCase());
            if ( replacer == null ) {
                continue;
            }

            String replacement = replacer.apply(player);
            if ( replacement == null ) {
                replacement = "";
            }

            m.appendReplacement(sb, replacement);
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
