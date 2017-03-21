package com.jrvdev.vasl.version;

import com.jrvdev.vasl.board.BoardVersion;

import java.util.Hashtable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;


public class MasterVersion implements IMasterVersion {

    private final IMasterVersionLoader _masterVersionLoader;

    private final HashMap< String, BoardVersion > _versions = new HashMap< String, BoardVersion >();

    public MasterVersion(IMasterVersionLoader loader) {
        super();
        _masterVersionLoader = loader;
    }

    public void load() {
        Map< Object, Object > versions = _masterVersionLoader.Load();
        for (Map.Entry<Object, Object> version : versions.entrySet()) {
            _versions.put( version.getKey().toString(), new BoardVersion( version.getValue().toString() ));
		}
    }

    @Override public Set<Map.Entry<String,BoardVersion>> entrySet() {
        return _versions.entrySet();
    }

    @Override public Collection< BoardVersion > values() {
        return _versions.values();
    }

    @Override public Set< String > keySet() {
        return _versions.keySet();
    }

    @Override public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override public void putAll(Map<? extends String,? extends BoardVersion> m) {
        throw new UnsupportedOperationException();
    }

    @Override public BoardVersion putIfAbsent(String key, BoardVersion value) {
        throw new UnsupportedOperationException();
    }

    @Override public BoardVersion remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override public BoardVersion put( String key, BoardVersion value ) {
        throw new UnsupportedOperationException();
    }

    @Override public BoardVersion get( Object key ) {
        return _versions.get( key );
    }

    @Override public boolean containsKey( Object key ) {
        return _versions.containsKey( key );
    }

    @Override public boolean containsValue( Object value ) {
        return _versions.containsValue( value );
    }

    @Override public boolean isEmpty() {
        return _versions.isEmpty();
    }

    @Override public int size() {
        return _versions.size();
    }

    @Override public String toString() {
        return _versions.toString();
    }
}