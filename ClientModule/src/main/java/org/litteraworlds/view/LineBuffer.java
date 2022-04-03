package org.litteraworlds.view;

import java.util.ArrayList;

public class LineBuffer {
    ArrayList<String> lines = new ArrayList<>();

    public LineBuffer(){}

    public void putLineIntoBuffer(String line){
        lines.add(line);
    }
}
