package com.kjt.service.common.job.cluster.impl;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SimpleRecoveryStatefulJob
  extends SimpleRecoveryJob
{}
