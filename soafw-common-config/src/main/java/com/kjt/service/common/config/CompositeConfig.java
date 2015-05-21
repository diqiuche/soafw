package com.kjt.service.common.config;

import java.util.HashMap;
import java.util.Map;

import javassist.tools.reflect.Metalevel;
import ognl.Ognl;
import ognl.OgnlException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("configs")
public class CompositeConfig {

    @XStreamAlias("comment")
    private String comment;

    @XStreamImplicit(itemFieldName = "entry")
    private Map<String,Entry> entrys = new HashMap<String,Entry>();

    public Map<String,Entry> getEntrys() {
        return entrys;
    }

    public void setEntrys(Map<String,Entry> entrys) {
        this.entrys = entrys;
    }

    public void add(Entry entry) {
        entrys.put(entry.getKey(),entry);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

        //Metalevel wrapper = new Metalevel();
        
        System.out.println(xStream.toXML(configs));
        
        try {
            System.out.println(Ognl.getValue("entrys.#easipay.key",configs));
        } catch (OgnlException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
