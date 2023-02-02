import sailpoint.object.*;
import sailpoint.api.*;
import sailpoint.connector.*;
import sailpoint.util.*;

boolean isAvailableByDatabase(
      SailPointContext context,
      Application application,
      String sql,
      String resultVariable) throws GeneralException {

        if (fvLog.isTraceEnabled()) {
          fvLog.trace(
            "Entering isAvailableByDatabase with context ["
              + (context == null ? "" : context.toString())
              + "] application ["
              + (application == null ? "" : application.toString())
              + "] sql ["
              + (sql == null ? "" : sql)
              + "] resultVariable ["
              + (resultVariable == null ? "" : resultVariable)
              + "]");
        }
        boolean result = false;
        if ((context != null) && (application != null) && (sql != null) && (resultVariable != null)) {

          //--- (HashMap of Attributes) Assemble JDBC Connection Object ---
          Attributes connectionAttributes = new Attributes();

          //--- DRIVER CLASS TO USE from Application ---
          connectionAttributes.put(JdbcUtil.ARG_DRIVER_CLASS, application.getAttributeValue(JdbcUtil.ARG_DRIVER_CLASS));

          //--- Connector URL for Application ---
          connectionAttributes.put(JdbcUtil.ARG_URL, application.getAttributeValue(JdbcUtil.ARG_URL));

          //--- Connector USERNAME for Connector ---
          connectionAttributes.put(JdbcUtil.ARG_USER, application.getAttributeValue(JdbcUtil.ARG_USER));

          //--- Connector PASSWORD for Connector ---
          connectionAttributes.put(
            JdbcUtil.ARG_PASSWORD,
            context.decrypt(application.getStringAttributeValue(JdbcUtil.ARG_PASSWORD)));

          //--- Connect to JDBC ---
          Connection connection = JdbcUtil.getConnection(connectionAttributes);

          //--- Execute Connector Query ---
          if (connection != null) {

            Statement statement;

            //--- Create SQL Query Object ---
            try {
              statement = connection.createStatement();
            } catch (SQLException e) {
              throw new GeneralException(e);
            }
            //--- Create Result Object to Store Query Return and Execute SQL Query ---
            if (statement != null) {
            
              ResultSet resultSet;
              
              try {
                resultSet = statement.executeQuery(sql);
              } catch (SQLException e) {
                throw new GeneralException(e);
              }
              
              //--- Set Position of results to -1 in SQL returned (factors in position Zero as a digit)  ---
              if (resultSet != null) {
                
                int userCount = -1;
                
                try {
                  while (resultSet.next()) {
                    userCount = resultSet.getInt(resultVariable);
                  }
                } catch (SQLException e) {
                  throw new GeneralException(e);
                }
                try{
                  resultSet.close(); //--- Exit Connector ---
                }catch(SQLException e){
                  throw new GeneralException(e);
                }
                if (userCount == 0) {
                  result = true;
                }
              } //--- Close Create Result Object ---
              
            try{
              statement.close();
            }catch(SQLException e){
              throw new GeneralException(e);
            }
          } //--- Close Execute Query ---
          
          try {
            connection.close();
          } catch (SQLException e) {
            throw new GeneralException(e);
          }
        }
      }
      if (fvLog.isTraceEnabled()) {
        fvLog.trace("Exiting isAvailableByDatabase [" + result + "]");
      }
      return result;
    }
