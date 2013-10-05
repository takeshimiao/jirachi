/**
 * 
 */
package com.salesforce.jirachi.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JIRA analysis report object.
 * @author scott_miao
 */
public class JiraAnalysisReport {

  // user selected start/end updateDates
  private Date startUpdateDate;
  private Date endUpdateDate;

  /**
   * rawData.
   * index[0] => String assigneeId;
   * index[1] => String assigneeName;
   * index[2] => boolean isCommitter;
   * index[3] => long solvedTicketCount;
   * index[4] => long slovedBlockerTicketCount;
   * index[5] => long slovedCriticalTicketCount;
   * index[6] => long slovedMajorTicketCount;
   * index[7] => long slovedMinorTicketCount;
   * index[8] => long slovedTrivialTicketCount;
   * index[9] => long slovedBugTicketCount;
   * index[10] => long slovedImprovementTicketCount;
   * index[11] => long slovedNewFeatureTicketCount;
   * index[12] => long slovedSubTaskTicketCount;
   * index[13] => long slovedTaskTicketCount;
   * index[14] => long slovedTestTicketCount;
   * 
   */
  private List<Object[]> rawData = new ArrayList<Object[]>();

  private Map<Integer, Map<String, Long>> indexTagCountMap =
      new HashMap<Integer, Map<String, Long>>();

  protected boolean addRawData(Object[] rawEntryData, Map<String, Long> tagCountMap) {
    rawData.add(rawEntryData);
    this.indexTagCountMap.put((rawData.size() - 1), tagCountMap);
    return true;
  }

  /**
   * @return the startUpdateDate
   */
  public Date getStartUpdateDate() {
    return startUpdateDate;
  }

  /**
   * @param startUpdateDate the startUpdateDate to set
   */
  public void setStartUpdateDate(Date startUpdateDate) {
    this.startUpdateDate = startUpdateDate;
  }

  /**
   * @return the endUpdateDate
   */
  public Date getEndUpdateDate() {
    return endUpdateDate;
  }

  /**
   * @param endUpdateDate the endUpdateDate to set
   */
  public void setEndUpdateDate(Date endUpdateDate) {
    this.endUpdateDate = endUpdateDate;
  }

  public JiraAnalysisReportEntry getEntry(int index) {
    JiraAnalysisReportEntry entry = null;
    Object[] rawEntry = null;
    rawEntry = rawData.get(index);

    entry = new JiraAnalysisReportEntry();
    entry.setAssigneeId((String) rawEntry[0]);
    entry.setAssigneeName((String) rawEntry[1]);
    entry.setCommitter((Boolean) rawEntry[2]);
    entry.setSolvedTicketCount(new Long("" + rawEntry[3]));
    entry.setSlovedBlockerTicketCount(new Long("" + rawEntry[4]));
    entry.setSlovedCriticalTicketCount(new Long("" + rawEntry[5]));
    entry.setSlovedMajorTicketCount(new Long("" + rawEntry[6]));
    entry.setSlovedMinorTicketCount(new Long("" + rawEntry[7]));
    entry.setSlovedTrivialTicketCount(new Long("" + rawEntry[8]));
    entry.setSlovedBugTicketCount(new Long("" + rawEntry[9]));
    entry.setSlovedImprovementTicketCount(new Long("" + rawEntry[10]));
    entry.setSlovedNewFeatureTicketCount(new Long("" + rawEntry[11]));
    entry.setSlovedSubTaskTicketCount(new Long("" + rawEntry[12]));
    entry.setSlovedTaskTicketCount(new Long("" + rawEntry[13]));
    entry.setSlovedTestTicketCount(new Long("" + rawEntry[14]));
    return entry;
  }

  /**
   * Get report entry size.
   * @return
   */
  public int size() {
    return this.rawData.size();
  }

}
