package com.kjt.service.common.dao.generate.tool;

import javax.sql.DataSource;

/**
 * 从Sql server 2008获取表字段属性信息，注释信息 select b.[value] from sys.columns a left join
 * sys.extended_properties b on a.object_id=b.major_id and a.column_id=b.minor_id inner join
 * sysobjects c on a.column_id=c.id and a.[name]='列名' and c.[name]='表名' SELECT 表名=case when
 * a.colorder=1 then d.name else '' end, 表说明=case when a.colorder=1 then isnull(f.value,'') else ''
 * end, 字段序号=a.colorder, 字段名=a.name, 标识=case when COLUMNPROPERTY( a.id,a.name,'IsIdentity')=1 then
 * '√'else '' end, 主键=case when exists(SELECT 1 FROM sysobjects where xtype='PK' and name in (
 * SELECT name FROM sysindexes WHERE indid in( SELECT indid FROM sysindexkeys WHERE id = a.id AND
 * colid=a.colid ))) then '√' else '' end, 类型=b.name, 占用字节数=a.length,
 * 长度=COLUMNPROPERTY(a.id,a.name,'PRECISION'), 小数位数=isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0),
 * 允许空=case when a.isnullable=1 then '√'else '' end, 默认值=isnull(e.text,''),
 * 字段说明=isnull(g.[value],'') FROM syscolumns a left join systypes b on a.xusertype=b.xusertype inner
 * join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' left join syscomments e
 * on a.cdefault=e.id left join sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id
 * left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0 --where d.name='orders'
 * --如果只查询指定表,加上此条件 order by a.id,a.colorder
 * 
 * @author alexzhu
 *
 */
public class TabReader_SqlSvr extends TabReader {
 
  public TabReader_SqlSvr(DataSource dataSource, String dbName, String table) throws Exception {
    super(dataSource,dbName,table);
  }

  @Override
  protected String getSql() {
    return "SELECT ordinal_position=a.colorder,column_name=a.name,column_comment=cast(isnull(g.[value],'') as varchar(100)),"
        + "data_type=b.name,column_default=isnull(e.text,''),column_key=case when exists(SELECT 1 FROM sysobjects "
        + "where xtype='PK' and name in (SELECT name FROM sysindexes WHERE indid in(SELECT indid FROM sysindexkeys "
        + "WHERE id=a.id AND colid=a.colid))) then 'PRI' else 'no' end,is_nullable=case when a.isnullable=1 then 'yes' else "
        + "'no' end FROM syscolumns a left join systypes b on a.xusertype=b.xusertype inner join sysobjects d on a.id=d.id "
        + "and d.xtype='U' and d.name<>'dtproperties' left join syscomments e on a.cdefault=e.id left join "
        + "sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id left join sys.extended_properties f "
        + "on d.id=f.major_id and f.minor_id=0 where   d.name='"+_table.trim() + "' order by a.id,a.colorder";
  }
}
