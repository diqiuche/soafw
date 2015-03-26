package com.kjt.service.#{artifactId}.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kjt.service.common.job.IJob;
import com.kjt.service.common.job.impl.AbsJob;

public class JobImpl extends AbsJob implements IJob{

    public JobImpl(String id) {
        super(id);
    }
    
    public void excute(){

        List<String> datas = new ArrayList<String>();//@TODO 实现数据
        datas.add("alex.zhu");
        doProcess(datas);
    }
    
    private Date start = new Date();
    @Override
    protected void doProcess(Object datas) {
        System.err.println(datas);
        Date now = new Date();
        System.err.println("start: "+start);
        System.err.println("end: "+(start=now));
    }

}
