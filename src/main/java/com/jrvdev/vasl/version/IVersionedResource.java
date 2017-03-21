package com.jrvdev.vasl.version;

public interface IVersionedResource< V > {
    String getName();
    V getVersion();
}