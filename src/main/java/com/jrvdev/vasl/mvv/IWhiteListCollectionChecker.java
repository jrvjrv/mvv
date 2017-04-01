package com.jrvdev.vasl.mvv;

import java.io.IOException;
import java.util.Set;

public interface IWhiteListCollectionChecker {
    Set<String> getBadNames() throws IOException;
}