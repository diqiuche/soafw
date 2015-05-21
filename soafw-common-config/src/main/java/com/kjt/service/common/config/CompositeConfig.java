package com.kjt.service.common.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("configs")
public class CompositeConfig {

    @XStreamAlias("comment")
    private String comment;

    @XStreamImplicit(itemFieldName = "entry")
    private List<Entry> entrys = new ArrayList<Entry>();

    public List<Entry> getEntrys() {
        return entrys;
    }

    public void setEntrys(List<Entry> entrys) {
        this.entrys = entrys;
    }

    public void add(Entry entry) {
        entrys.add(entry);
    }

    public static void main(String[] args) {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        Entry entry = new Entry();
        entry.setKey("hello");
        entry.setValue("archlevel");
        CompositeConfig configs = new CompositeConfig();
        configs.add(entry);
        entry = new Entry();
        entry.setKey("easipay");
        configs.add(entry);

        Entry child = new Entry();
        child.setKey("hello");
        child.setValue("archlevel");

        entry.add(child);


        System.out.println(xStream.toXML(configs));
        
        
    }
}
