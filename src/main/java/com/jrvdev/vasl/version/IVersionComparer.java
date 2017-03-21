package com.jrvdev.vasl.version;

public interface IVersionComparer<V> {
    boolean IsUpdatable( IVersionedResource<V> versionedResource);
    String VersionComparison(IVersionedResource<V> versionedResource);
}