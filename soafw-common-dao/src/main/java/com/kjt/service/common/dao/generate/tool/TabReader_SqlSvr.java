package com.kjt.service.common.dao.generate.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

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
public class TabReader_SqlSvr {
  private JdbcTemplate jdbcTemplate;
  private String _table;
  private String _dbName;
  private DataSource dataSource;

  public TabReader_SqlSvr(DataSource dataSource, String dbName, String table) throws Exception {
    this.dataSource = dataSource;
    this._dbName = dbName;
    jdbcTemplate = new JdbcTemplate(dataSource);
    this._table = table;
  }

  class Col {
    private int index;
    private String name;
    private String comment;
    private String type;
    private String def;
    private String isKey;
    private String isNullable;

    Col(ResultSet rs) throws SQLException {
      index = rs.getInt("ordinal_position");
      name = rs.getString("column_name");
      comment = rs.getString("column_comment") == null ? "" : rs.getString("column_comment");
      type = rs.getString("data_type");
      def = rs.getString("column_default") == null ? "" : rs.getString("column_default");
      String key = rs.getString("column_key");
      isKey = key == null ? "no" : "PRI".equals(key) ? "yes" : "no";
      String nullable = rs.getString("is_nullable");
      isNullable = nullable == null ? "yes" : "no".equals(nullable) ? "no" : "yes";
    }

    public List toList() {
      List<Object> def_ = new ArrayList<Object>();
      def_.add(index);
      def_.add(name);
      def_.add(comment);
      def_.add(type);
      def_.add(null);
      def_.add(def);
      def_.add(isKey);
      def_.add(isNullable);
      return def_;
    }

    public int getIndex() {
      return index;
    }

    public void setIndex(int index) {
      this.index = index;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getComment() {
      return comment;
    }

    public void setComment(String comment) {
      this.comment = comment;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getDef() {
      return def;
    }

    public void setDef(String def) {
      this.def = def;
    }

    public String getIsKey() {
      return isKey;
    }

    public void setIsKey(String isKey) {
      this.isKey = isKey;
    }

    public String getIsNullable() {
      return isNullable;
    }

    public void setIsNullable(String isNullable) {
      this.isNullable = isNullable;
    }

  }

  class ColMapper implements ParameterizedRowMapper<Col> {
    @Override
    public Col mapRow(ResultSet rs, int arg1) throws SQLException {
      return new Col(rs);
    }

  }

  public Tab createDef(List<List<Object>> head) {
    String sql = "SELECT ordinal_position=a.colorder,column_name=a.name,column_comment=isnull(g.[value],''),"
        + "data_type=b.name,column_default=isnull(e.text,''),column_key=case when exists(SELECT 1 FROM sysobjects "
        + "where xtype='PK' and name in (SELECT name FROM sysindexes WHERE indid in(SELECT indid FROM sysindexkeys "
        + "WHERE id=a.id AND colid=a.colid))) then 'yes' else 'no' end,is_nullable=case when a.isnullable=1 then 'yes'else "
        + "'no' end FROM syscolumns a left join systypes b on a.xusertype=b.xusertypeinner join sysobjects d on a.id=d.id "
        + "and d.xtype='U' and d.name<>'dtproperties' left join syscomments e on a.cdefault=e.idleft join "
        + "sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id left join sys.extended_properties f "
        + "on d.id=f.major_id and f.minor_id=0 where   d.name='"+_table.trim() + " order by a.id,a.colorder";

    List<Col> defs = jdbcTemplate.query(sql, new Object[] {}, new ColMapper());
    int size = defs == null ? 0 : defs.size();
    int pkFieldNum = 0;
    Type pkFieldType = null;
    for (int i = 0; i < size; i++) {
      Col col = defs.get(i);
      if ("yes".equals(col.getIsKey())) {
        pkFieldNum++;
        pkFieldType = Type.get(col.type);
      }

      List<Object> col_ = col.toList();
      head.add(col_);
    }
    Tab tab = new Tab(_dbName, _table, head);
    tab.setPkFieldNum(pkFieldNum);
    tab.setPkFieldType(pkFieldType);
    return tab;
  }
}
