 package com.yq.weixin;
 
 import java.io.PrintStream;
 import java.sql.CallableStatement;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.ResultSet;
 import java.sql.ResultSetMetaData;
 import java.sql.SQLException;
 import java.sql.Statement;
 import java.util.ArrayList;
 import java.util.Hashtable;
 import java.util.Vector;
 import javax.sql.DataSource;
 
 public class ConnDbb
 {
   private String driverName = "com.mysql.jdbc.Driver";
   private String url = "jdbc:mysql://localhost:3306/xiaheng?characterEncoding=UTF-8&amp;useUnicode=true";
   private String user = "root";
   private String password = "xiaheng136@@136";
 
   Connection conn = null;
   Statement stmt = null;
   DataSource pool;
 
   public void setDriverName(String newDriverName)
   {
     this.driverName = newDriverName;
   }
   public String getDriverName() {
     return this.driverName;
   }
   public void setUrl(String newUrl) {
     this.url = newUrl;
   }
   public String getUrl() {
     return this.url;
   }
   public void setUser(String newUser) {
     this.user = newUser;
   }
   public String getUser() {
     return this.user;
   }
   public void setPassword(String newPassword) {
     this.password = newPassword;
   }
   public String getPassword() {
     return this.password;
   }
 
   public Connection getConnection()
   {
     try
     {
       Class.forName(this.driverName);
       Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
       return conn;
     }
     catch (Exception e)
     {
       System.out.println(e);
     }return null;
   }
 
   public void setcon()
   {
     try
     {
       this.conn = getConnection();
       this.conn.setAutoCommit(false);
       this.stmt = this.conn.createStatement();
     }
     catch (Exception e) {
       System.out.println(e);
     }
   }
 
   public void addsql(String sqlstr)
   {
     try
     {
       this.stmt.addBatch(sqlstr);
     }
     catch (Exception e)
     {
       System.out.println(e);
     }
   }
 
   public int doBatch(int shu)
   {
     try
     {
       int[] updateCounts = this.stmt.executeBatch();
 
       if (shu == updateCounts.length) {
         this.conn.commit();
         this.conn.setAutoCommit(true);
         this.stmt.close();
         this.conn.close();
         return 1;
       }
 
       this.conn.rollback();
       this.conn.setAutoCommit(true);
       this.stmt.close();
       this.conn.close();
       return 0;
     }
     catch (Exception e)
     {
       try
       {
         this.conn.rollback();
         this.conn.setAutoCommit(true);
         this.stmt.close();
         this.conn.close();
         System.out.println(e);
       } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return 0;
   }
 
   public Vector queryTable(String sqlstr)
   {
     Vector pkv = new Vector();
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement(1005, 1007);
 
       ResultSet rs = this.stmt.executeQuery(sqlstr);
       ResultSetMetaData rsmd = rs.getMetaData();
 
       int num = rsmd.getColumnCount();
 
       while (rs.next())
       {
         Hashtable table = new Hashtable();
         for (int i = 1; i <= num; i++)
         {
           String key = rsmd.getColumnName(i);
           String value = rs.getString(i);
           if (value == null)
             value = "";
           table.put(key, value);
         }
         pkv.add(table);
       }
       this.stmt.close();
       this.conn.close();
     } catch (SQLException e) {
       System.out.println(e);
       try {
         this.stmt.close();
         this.conn.close();
       } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return pkv;
   }
 
   public ResultSet executeQuery(String sqlstr)
   {
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement();
       return this.stmt.executeQuery(sqlstr);
     } catch (Exception e) {
     }
     return null;
   }
 
   public void close()
   {
     try {
       this.stmt.close();
       this.conn.close();
     } catch (Exception ee) {
       System.out.println(ee);
     }
   }
 
   public int executeUpdate(String sqlstr)
   {
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement();
       this.stmt.executeUpdate(sqlstr);
       this.stmt.close();
       this.conn.close();
       return 1;
     } catch (Exception e) {
       try {
         this.stmt.close();
         this.conn.close();
         System.out.println(e);
       } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return 0;
   }
 
   public void execute(String sqlstr)
   {
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement();
       this.stmt.execute(sqlstr);
       this.stmt.close();
       this.conn.close();
     } catch (Exception e) {
       try {
         this.stmt.close();
         this.conn.close();
       } catch (Exception ee) {
         System.out.println(ee);
       }
     }
   }
 
   public int executeQueryNum(String sql)
   {
     Connection con = getConnection();
     Statement stmt = null;
     try {
       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery(sql);
       ResultSetMetaData rsmd = rs.getMetaData();
 
       int queryNum = rsmd.getColumnCount();
 
       rs.close();
       stmt.close();
       con.close();
       return queryNum;
     } catch (Exception e) {
       System.out.println(e.getMessage());
       try {
         stmt.close();
         con.close();
       } catch (Exception ex) {
         System.out.println(ex.getMessage());
       }
     }
     return -1;
   }
 
   public int executeCounts(String sql)
   {
     Connection con = getConnection();
     Statement stmt = null;
     try {
       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery(sql);
       if (rs != null) {
         rs.next();
         int queryNum = rs.getInt(1);
         rs.close();
         stmt.close();
         con.close();
         return queryNum;
       }
 
       stmt.close();
       con.close();
       return -1;
     }
     catch (Exception e) {
       System.out.println(e.getMessage());
       try {
         stmt.close();
         con.close();
       } catch (Exception localException1) {
       }
     }
     return -1;
   }
 
   private Hashtable getTheValue(ResultSet rs, ResultSetMetaData rsmd)
   {
     Hashtable hstab = new Hashtable();
     try {
       if (rs != null) {
         int maxrows = rsmd.getColumnCount();
         for (int i = 1; i <= maxrows; i++) {
           String key = rsmd.getColumnName(i);
           String value = rs.getString(i);
           if (value == null) {
             value = "";
           }
           hstab.put(key, value);
         }
       }
     } catch (Exception e) {
       System.out.println(e.getMessage());
       return hstab;
     }
     return hstab;
   }
 
   public ArrayList getList(String sqlstr, int page, int pageSize)
   {
     ArrayList pkv = new ArrayList();
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement(1005, 1007);
       ResultSet rs = this.stmt.executeQuery(sqlstr);
       ResultSetMetaData rsmd = rs.getMetaData();
       int num = rsmd.getColumnCount();
       int rowbegin = 0; int j = 0;
       rowbegin = (page - 1) * pageSize + 1;
       rs.absolute(rowbegin);
       if (rs.getRow() == 0) {
         rs.close();
         this.stmt.close();
         this.conn.close();
         return null;
       }
       while (j < pageSize) {
         Hashtable table = new Hashtable();
         for (int i = 1; i <= num; i++)
         {
           String key = rsmd.getColumnName(i);
           String value = rs.getString(i);
           if (value == null)
             value = "";
           table.put(key, value);
         }
         pkv.add(table);
         if (!rs.next()) break;
         j++;
       }
       this.stmt.close();
       this.conn.close();
     }
     catch (SQLException e) {
       try {
         this.stmt.close();
         this.conn.close(); } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return pkv;
   }
 
   public ArrayList getList(String sqlString)
   {
     ArrayList pkv = new ArrayList();
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement();
 
       ResultSet rs = this.stmt.executeQuery(sqlString);
       ResultSetMetaData rsmd = rs.getMetaData();
       int num = rsmd.getColumnCount();
       while (rs.next())
       {
         Hashtable table = new Hashtable();
         for (int i = 1; i <= num; i++)
         {
           String key = rsmd.getColumnName(i);
           String value = rs.getString(i);
           if (value == null)
             value = "";
           table.put(key, value);
         }
         pkv.add(table);
       }
       this.stmt.close();
       this.conn.close();
     }
     catch (SQLException e) {
       System.out.println(e);
       try {
         this.stmt.close();
         this.conn.close(); } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return pkv;
   }
 
   public Vector queryTable(String sqlstr, int page, int pageSize)
   {
     Vector pkv = new Vector();
     try
     {
       this.conn = getConnection();
       this.stmt = this.conn.createStatement(1005, 1007);
       ResultSet rs = this.stmt.executeQuery(sqlstr);
       ResultSetMetaData rsmd = rs.getMetaData();
       int num = rsmd.getColumnCount();
       int rowbegin = 0; int j = 0;
       rowbegin = (page - 1) * pageSize + 1;
       rs.absolute(rowbegin);
       while (j < pageSize) {
         Hashtable table = new Hashtable();
         for (int i = 1; i <= num; i++) {
           String key = rsmd.getColumnName(i);
           String value = rs.getString(i);
           if (value == null)
             value = "";
           table.put(key, value);
         }
         pkv.add(table);
         if (!rs.next()) break;
         j++;
       }
       this.stmt.close();
       this.conn.close();
     } catch (SQLException e) {
       System.out.println(e);
       try {
         this.stmt.close();
         this.conn.close(); } catch (Exception ee) {
         System.out.println(ee);
       }
     }
     return pkv;
   }
 
   public Hashtable getValue(String sql)
   {
     Hashtable hstab = new Hashtable();
 
     Connection con = getConnection();
     Statement stmt = null;
     try
     {
       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery(sql);
       ResultSetMetaData rsmd = rs.getMetaData();
       if (rs.next()) {
         hstab = getTheValue(rs, rsmd);
       }
       rs.close();
       stmt.close();
       con.close();
       return hstab;
     } catch (Exception e) {
       System.out.println(e.getMessage());
       try {
         stmt.close();
         con.close();
       } catch (Exception ex) {
         System.out.println(ex.getMessage());
       }
     }
     return hstab;
   }
 
   public String[] execProcedure(String procedureName, String[] inParam, int outParamCount)
   {
     Connection conn = getConnection();
     CallableStatement cstmt = null;
     String strProcedure = "";
 
     int nIn = 0;
 
     if (inParam != null) {
       nIn = inParam.length;
     }
 
     for (int i = 0; i < outParamCount; i++) {
       strProcedure = strProcedure + ",?";
     }
 
     if (strProcedure.length() > 0) {
       strProcedure = strProcedure.substring(1, strProcedure.length());
     }
 
     strProcedure = "{call " + procedureName + "(" + strProcedure + ")}";
     System.out.println(strProcedure);
     try
     {
       cstmt = conn.prepareCall(strProcedure);
 
       for (int i = 0; i < nIn; i++) {
         cstmt.setString(i + 1, inParam[i]);
       }
 
       for (int i = nIn; i <= nIn + outParamCount; i++) {
         cstmt.registerOutParameter(i + 1, 1111);
       }
 
       cstmt.executeUpdate();
 
       if (outParamCount == 0) {
         String[] strReturn = new String[1];
         strReturn[0] = "";
         cstmt.close();
         conn.close();
         return strReturn;
       }
       String[] strReturn = new String[outParamCount];
       for (int i = nIn; i < nIn + outParamCount; i++) {
         strReturn[(i - nIn)] = cstmt.getString(i + 1);
       }
       cstmt.close();
       conn.close();
       return strReturn;
     }
     catch (SQLException e)
     {
       String[] strReturn = new String[1];
       try {
         cstmt.close();
         conn.close();
       } catch (SQLException ee) {
         System.out.println(ee);
       }
       return strReturn;
     }
   }
 
   public ResultSet ProcedureQuery(String procedureName, String inParam1, String inParam2, String inParam3)
   {
     Connection conn = getConnection();
     CallableStatement cstmt = null;
     String strProcedure = "";
 
     int nIn = 0;
     strProcedure = "{call " + procedureName + "(?,?,?)}";
     try
     {
       cstmt = conn.prepareCall(strProcedure);
 
       cstmt.setString(1, inParam1);
       cstmt.setString(2, inParam2);
       cstmt.setString(3, inParam3);
       return cstmt.executeQuery();
     }
     catch (SQLException e)
     {
       String[] strReturn = new String[1];
       strReturn[0] = ("�洢����ִ�д���" + e);
       System.out.println(e);
       try {
         cstmt.close();
         conn.close();
       } catch (SQLException ee) {
         System.out.println(ee);
       }
     }
     return null;
   }
 
   public ResultSet ProcedureQuery_jian(String procedureName, String inParam1, String inParam2, String inParam3, String inParam4)
   {
     Connection conn = getConnection();
     CallableStatement cstmt = null;
     String strProcedure = "";
 
     int nIn = 0;
     strProcedure = "{call " + procedureName + "(?,?,?,?)}";
     try
     {
       cstmt = conn.prepareCall(strProcedure);
 
       cstmt.setString(1, inParam1);
       cstmt.setString(2, inParam2);
       cstmt.setString(3, inParam3);
       cstmt.setString(4, inParam4);
       return cstmt.executeQuery();
     }
     catch (SQLException e)
     {
       String[] strReturn = new String[1];
       strReturn[0] = ("�洢����ִ�д���" + e);
       System.out.println(e);
       try {
         cstmt.close();
         conn.close();
       } catch (SQLException ee) {
         System.out.println(ee);
       }
     }
     return null;
   }
 
   public ResultSet ProcedureQuery_jian2(String procedureName, String inParam1, String inParam2, String inParam3, String inParam4, int inParam5)
   {
     Connection conn = getConnection();
     CallableStatement cstmt = null;
     String strProcedure = "";
 
     int nIn = 0;
     strProcedure = "{call " + procedureName + "(?,?,?,?,?)}";
     try
     {
       cstmt = conn.prepareCall(strProcedure);
 
       cstmt.setString(1, inParam1);
       cstmt.setString(2, inParam2);
       cstmt.setString(3, inParam3);
       cstmt.setString(4, inParam4);
       cstmt.setInt(5, inParam5);
       return cstmt.executeQuery();
     }
     catch (SQLException e)
     {
       String[] strReturn = new String[1];
       strReturn[0] = ("�洢����ִ�д���" + e);
       System.out.println(e);
       try {
         cstmt.close();
         conn.close();
       } catch (SQLException ee) {
         System.out.println(ee);
       }
     }
     return null;
   }
 
   public int getId(String tableName, String fieldName)
   {
     Connection con = getConnection();
     Statement stmt = null;
     int id = -1;
     try {
       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery("SELECT nvl(MAX(" + fieldName + "),0) FROM " + tableName);
       if (rs.next()) {
         id = rs.getInt(1) + 1;
       }
       rs.close();
       stmt.close();
       con.close();
     } catch (Exception e) {
       System.out.println(e.getMessage());
       try {
         stmt.close();
         con.close();
       } catch (Exception ex) {
         System.out.println(ex.getMessage());
       }
     }
     return id;
   }
 
   public int getId(String tableName, String fieldName, String strWhere)
   {
     int nRet = 0;
     String strTmp = "";
     Connection con = getConnection();
     Statement stmt = null;
     try {
       stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery("SELECT Max(" + fieldName + ") KeyID FROM " + tableName + " " + strWhere);
       if (rs.next()) {
         strTmp = rs.getString("KeyID");
       }
       if (strTmp == null)
       {
         nRet = 1;
       }
       else {
         nRet = Integer.parseInt(strTmp) + 1;
       }
       rs.close();
       stmt.close();
       con.close();
       return nRet;
     } catch (Exception e) {
       System.out.println(e.getMessage());
       try {
         stmt.close();
         con.close();
       } catch (Exception ex) {
         System.out.println(ex.getMessage());
       }
     }
     return -1;
   }
 }

/* Location:           
 * Qualified Name:     com.yq.weixin.ConnDbb
 * JD-Core Version:    0.6.2
 */