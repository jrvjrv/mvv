package com.jrvdev.vasl.version;

import com.jrvdev.vasl.board.BoardVersion;

import java.util.Map;

public interface IMasterVersion extends  Map< String, BoardVersion > {
    void load();
}