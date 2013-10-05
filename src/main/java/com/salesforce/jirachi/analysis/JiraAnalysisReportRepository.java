/**
 * 
 */
package com.salesforce.jirachi.analysis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.salesforce.jirachi.ingest.JiraTagger;
import com.salesforce.jirachi.ingest.PhoenixUtils;

/**
 * Respository for {@link JiraAnalysisReport}
 * @author scott_miao
 */
public class JiraAnalysisReportRepository {
  private static final String SQL_GROUP_BY_ASSIGNEE_TICKET_COUNT_TEMPLATE_PREFIX = 
      "SELECT ASSIGNEE_ID, COUNT(*) ISSUES_COUNT " +
      "FROM JIRA_ISSUE " +
      "WHERE " +
      "ISSUE_KEY LIKE 'HBASE-%' ";
  
  private static final String SQL_GROUP_BY_ASSIGNEE_TICKET_COUNT_TEMPLATE_SUFFIX = 
      "GROUP BY ASSIGNEE_ID " +
      "ORDER BY ISSUES_COUNT DESC ";
  
  private static final String SQL_START_UPDATE_DATE = "AND UPDATEDATE > ? ";
  
  private static final String SQL_END_UPDATE_DATE = "AND UPDATEDATE < ? ";
  
  private static final String SQL_GET_ASSIGNEE_NAME_PREFIX =
      "SELECT ASSIGNEE " +
      "FROM JIRA_ISSUE " +
      "WHERE " +
 "ISSUE_KEY LIKE 'HBASE-%' ";
  
  private static final String SQL_GET_ASSIGNEE_NAME_SUFFIX = "LIMIT 1 ";

  private static final String SQL_VAR_TICKET_COUNT =
      "SELECT COUNT(*) ISSUE_COUNT " +
      "FROM JIRA_ISSUE " +
      "WHERE " +
 "ISSUE_KEY LIKE 'HBASE-%' ";

  private static final String SQL_ASSIGNEE_ID_EXISTS = "AND ASSIGNEE_ID = ? ";
  private static final String SQL_ASSIGNEE_ID_NOT_EXISTS = "AND ASSIGNEE_ID IS NULL ";

  private static final String SQL_PRIORITY_SUFFIX = "AND PRIORITY = ? ";
  
  private static final String SQL_ISSUETYPE_SUFFIX = "AND ISSUETYPE = ? ";

  private static final String SQL_TAG_SUFFIX_PATTERN = "AND {0} = true ";
  private static final MessageFormat SQL_TAG_SUFFIX_FMT = new MessageFormat(SQL_TAG_SUFFIX_PATTERN);

  /**
   * Get {@link JiraAnalysisReport} Object.
   * @param startUpdateDate start jira update date
   * @param endUpdateDate end jira update date
   * @param committers a list of committers to mark
   * @return
   * @throws Exception
   */
  public JiraAnalysisReport getJiraAnalisysReport(Date startUpdateDate, Date endUpdateDate,
      String[] committers) throws Exception {
    JiraAnalysisReport report = new JiraAnalysisReport();
    List<java.sql.Date> params = new ArrayList<java.sql.Date>();
    Set<String> committersSet = new HashSet<String>();
    Collection<String> tags = JiraTagger.getTagNames();

    StringBuffer sql = new StringBuffer();
    sql.append(SQL_GROUP_BY_ASSIGNEE_TICKET_COUNT_TEMPLATE_PREFIX);
    if (startUpdateDate != null) {
      sql.append(SQL_START_UPDATE_DATE);
      params.add(new java.sql.Date(startUpdateDate.getTime()));
    }

    if (endUpdateDate != null) {
      sql.append(SQL_END_UPDATE_DATE);
      params.add(new java.sql.Date(endUpdateDate.getTime()));
    }

    Connection con = null;
    PreparedStatement grpStat = null;
    ResultSet grpRs = null;
    sql.append(SQL_GROUP_BY_ASSIGNEE_TICKET_COUNT_TEMPLATE_SUFFIX);
    try {
      con = PhoenixUtils.getPhoenixConnection();

      grpStat = con.prepareStatement(sql.toString());
      int idx = 1;
      for (java.sql.Date param : params) {
        grpStat.setDate(idx++, param);
      }

      grpRs = grpStat.executeQuery();
      Object[] rawEntryData = null;
      // set unique set for committer ids
      if (committers != null && committers.length > 0) {
        for (String committer : committers) {
          committersSet.add(committer);
        }
      }

      Map<String, Long> tagCountMap = null;
      Long tagCount = null;
      String assigneeId = null;
      long reportEntryCount = 0L;
      while (grpRs.next()) {
        rawEntryData = new Object[15];
        rawEntryData[0] = grpRs.getString("ASSIGNEE_ID");
        rawEntryData[3] = grpRs.getString("ISSUES_COUNT");
        rawEntryData[2] = committersSet.contains(rawEntryData[0]) ? Boolean.TRUE : Boolean.FALSE;

        assigneeId = rawEntryData[0] != null ? "" + rawEntryData[0] : null;
        // get assignee name
        fillRawEntryData(con, rawEntryData, 1, SQL_GET_ASSIGNEE_NAME_PREFIX, "", "ASSIGNEE", null, null,
          assigneeId);

        // HEAD:fill priority info.
        // Blocker
        fillRawEntryData(con, rawEntryData, 4, SQL_VAR_TICKET_COUNT, SQL_PRIORITY_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Blocker");
        // Critical
        fillRawEntryData(con, rawEntryData, 5, SQL_VAR_TICKET_COUNT, SQL_PRIORITY_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Critical");
        // Major
        fillRawEntryData(con, rawEntryData, 6, SQL_VAR_TICKET_COUNT, SQL_PRIORITY_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Major");
        // Minor
        fillRawEntryData(con, rawEntryData, 7, SQL_VAR_TICKET_COUNT, SQL_PRIORITY_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Minor");
        // Trivial
        fillRawEntryData(con, rawEntryData, 8, SQL_VAR_TICKET_COUNT, SQL_PRIORITY_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Trivial");
        // TAIL:fill priority info.

        // HEAD:fill issuetype info.
        // Bug
        fillRawEntryData(con, rawEntryData, 9, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Bug");
        // Improvement
        fillRawEntryData(con, rawEntryData, 10, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Improvement");
        // New Feature
        fillRawEntryData(con, rawEntryData, 11, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "New Feature");
        // Sub-task
        fillRawEntryData(con, rawEntryData, 12, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Sub-task");
        // Task
        fillRawEntryData(con, rawEntryData, 13, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Task");
        // Test
        fillRawEntryData(con, rawEntryData, 14, SQL_VAR_TICKET_COUNT, SQL_ISSUETYPE_SUFFIX,
          "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId, "Test");
        // TAIL:fill issuetype info.

        // HEAD:fill tag counts
        tagCountMap = new HashMap<String, Long>();
        for (String tag : tags) {
          tagCount =
              (Long) fillRawEntryData(con, null, 0, SQL_VAR_TICKET_COUNT, SQL_TAG_SUFFIX_FMT.format(new String[]{tag}),
                "ISSUE_COUNT", startUpdateDate, endUpdateDate, assigneeId);
          tagCountMap.put(tag, tagCount);
        }
        report.setStartUpdateDate(startUpdateDate);
        report.setEndUpdateDate(endUpdateDate);

        report.addRawData(rawEntryData, tagCountMap);

        reportEntryCount++;
        // if (reportEntryCount % 500 == 0) {
          System.out.println("Current processing report entry count = " + reportEntryCount);
        // }

        // TAIL:fill tag counts
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      // do housekeeping
      if (grpRs != null) grpRs.close();
      if (grpStat != null) grpStat.close();
      if (con != null) con.close();
    }
    return report;
  }

  private static Object fillRawEntryData(Connection con, Object[] rawEntryData,
      int rawEntryDataIdx, String sqlSelectClause, String sqlWhereClause, String retrColLabel,
      Date startUpdateDate,
      Date endUpdateDate, String... queryParams)
      throws Exception {
    PreparedStatement stat = null;
    ResultSet rs = null;
    StringBuffer sql = null;
    int count = 1;
    Object retValue = null;
    String[] tmpQueryParams = null;
    try {
      sql = new StringBuffer();
      sql.append(sqlSelectClause);
      if (queryParams.length > 0 && queryParams[0] == null) {
        sql.append(SQL_ASSIGNEE_ID_NOT_EXISTS);
        if (queryParams.length > 1) {
          tmpQueryParams = new String[queryParams.length - 1];
          System.arraycopy(queryParams, 1, tmpQueryParams, 0, tmpQueryParams.length);
        } else {
          tmpQueryParams = new String[] {};
        }
      } else if (queryParams.length > 0) {
        sql.append(SQL_ASSIGNEE_ID_EXISTS);
        tmpQueryParams = queryParams;
      }
      sql.append(sqlWhereClause);

      if (startUpdateDate != null) sql.append(SQL_START_UPDATE_DATE);

      if (endUpdateDate != null) sql.append(SQL_END_UPDATE_DATE);

      stat = con.prepareStatement(sql.toString());
      for (String param : tmpQueryParams) {
        stat.setString(count++, param);
      }

      if (startUpdateDate != null)
        stat.setDate(count++, new java.sql.Date(startUpdateDate.getTime()));

      if (endUpdateDate != null)
        stat.setDate(count++, new java.sql.Date(endUpdateDate.getTime()));

      rs = stat.executeQuery();
      rs.next();
      retValue = rs.getObject(retrColLabel);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    } finally {
      // do housekeeping
      if (rs != null) rs.close();
      if (stat != null) stat.close();
    }
    if (rawEntryData != null) rawEntryData[rawEntryDataIdx] = retValue;
    return retValue;
  }

}
