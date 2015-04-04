/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 *
 * @author Kevin
 * @param <K>
 */
public class PrettyPrintingMap<K, V> {
    private final Map<K, V> map;

    public PrettyPrintingMap(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, V> entry = (Entry<K, V>) iter.next();
            sb.append(entry.getKey());
            sb.append('=');//.append('"');
            sb.append(entry.getValue());
           //sb.append('"');
            sb.append("\n");
            if (iter.hasNext()) {
         //   sb.append(',').append(' ');
            }
        }
        return sb.toString();

    }
}