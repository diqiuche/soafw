package com.kjt.service.common.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("entry")
public class Entry {

    @XStreamAsAttribute
    @XStreamAlias("key")
    private String key;

    @XStreamAlias("value")
    private String value;

    @XStreamImplicit(itemFieldName = "child")
    private List<Entry> childs = new ArrayList<Entry>();

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void add(Entry entry) {
        childs.add(entry);
    }

    public List<Entry> getChilds() {
        return childs;
    }

    public void setChilds(List<Entry> childs) {
        this.childs = childs;
    }

}
