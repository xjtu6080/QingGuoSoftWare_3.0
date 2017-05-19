package com.example.xjtuse_pc.coursettable2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by XJTUSE-PC on 2017/4/18.
 */

public class SerializeMap implements Serializable {
    private Map<String, List<String>> map = new TreeMap<String, List<String>>();
    public Map<String, List<String>> getMap()
    {
        return map;
    }
    public void setMap(Map<String, List<String>> map)
    {
        this.map=map;
    }
}
