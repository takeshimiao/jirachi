/**
 * 
 */
package com.salesforce.jirachi.analysis;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;

/**
 * A JIRA analysis report entry for {@link JiraAnalysisReport}.
 * @author scott_miao
 */
public class JiraAnalysisReportEntry {
  private String assigneeId;
  private String assigneeName;
  private boolean isCommitter;

  // ticket priority
  private long solvedTicketCount;
  private long slovedBlockerTicketCount;
  private long slovedCriticalTicketCount;
  private long slovedMajorTicketCount;
  private long slovedMinorTicketCount;
  private long slovedTrivialTicketCount;

  // ticket issue type
  private long slovedBugTicketCount;
  private long slovedImprovementTicketCount;
  private long slovedNewFeatureTicketCount;
  private long slovedSubTaskTicketCount;
  private long slovedTaskTicketCount;
  private long slovedTestTicketCount;

  // ticket tags
  private Map<String, Long> tagCountMap = new HashMap<String, Long>();

  /**
   * @return the assigneeId
   */
  public String getAssigneeId() {
    return assigneeId;
  }

  /**
   * @param assigneeId the assigneeId to set
   */
  public void setAssigneeId(String assigneeId) {
    this.assigneeId = assigneeId;
  }

  /**
   * @return the assigneeName
   */
  public String getAssigneeName() {
    return assigneeName;
  }

  /**
   * @param assigneeName the assigneeName to set
   */
  public void setAssigneeName(String assigneeName) {
    this.assigneeName = assigneeName;
  }

  /**
   * @return the isCommitter
   */
  public boolean isCommitter() {
    return isCommitter;
  }

  /**
   * @param isCommitter the isCommitter to set
   */
  public void setCommitter(boolean isCommitter) {
    this.isCommitter = isCommitter;
  }

  /**
   * @return the solvedTicketCount
   */
  public long getSolvedTicketCount() {
    return solvedTicketCount;
  }

  /**
   * @param solvedTicketCount the solvedTicketCount to set
   */
  public void setSolvedTicketCount(long solvedTicketCount) {
    this.solvedTicketCount = solvedTicketCount;
  }

  /**
   * @return the slovedBlockerTicketCount
   */
  public long getSlovedBlockerTicketCount() {
    return slovedBlockerTicketCount;
  }

  /**
   * @param slovedBlockerTicketCount the slovedBlockerTicketCount to set
   */
  public void setSlovedBlockerTicketCount(long slovedBlockerTicketCount) {
    this.slovedBlockerTicketCount = slovedBlockerTicketCount;
  }

  /**
   * @return the slovedCriticalTicketCount
   */
  public long getSlovedCriticalTicketCount() {
    return slovedCriticalTicketCount;
  }

  /**
   * @param slovedCriticalTicketCount the slovedCriticalTicketCount to set
   */
  public void setSlovedCriticalTicketCount(long slovedCriticalTicketCount) {
    this.slovedCriticalTicketCount = slovedCriticalTicketCount;
  }

  /**
   * @return the slovedMajorTicketCount
   */
  public long getSlovedMajorTicketCount() {
    return slovedMajorTicketCount;
  }

  /**
   * @param slovedMajorTicketCount the slovedMajorTicketCount to set
   */
  public void setSlovedMajorTicketCount(long slovedMajorTicketCount) {
    this.slovedMajorTicketCount = slovedMajorTicketCount;
  }

  /**
   * @return the slovedMinorTicketCount
   */
  public long getSlovedMinorTicketCount() {
    return slovedMinorTicketCount;
  }

  /**
   * @param slovedMinorTicketCount the slovedMinorTicketCount to set
   */
  public void setSlovedMinorTicketCount(long slovedMinorTicketCount) {
    this.slovedMinorTicketCount = slovedMinorTicketCount;
  }

  /**
   * @return the slovedTrivialTicketCount
   */
  public long getSlovedTrivialTicketCount() {
    return slovedTrivialTicketCount;
  }

  /**
   * @param slovedTrivialTicketCount the slovedTrivialTicketCount to set
   */
  public void setSlovedTrivialTicketCount(long slovedTrivialTicketCount) {
    this.slovedTrivialTicketCount = slovedTrivialTicketCount;
  }

  /**
   * @return the slovedBugTicketCount
   */
  public long getSlovedBugTicketCount() {
    return slovedBugTicketCount;
  }

  /**
   * @param slovedBugTicketCount the slovedBugTicketCount to set
   */
  public void setSlovedBugTicketCount(long slovedBugTicketCount) {
    this.slovedBugTicketCount = slovedBugTicketCount;
  }

  /**
   * @return the slovedImprovementTicketCount
   */
  public long getSlovedImprovementTicketCount() {
    return slovedImprovementTicketCount;
  }

  /**
   * @param slovedImprovementTicketCount the slovedImprovementTicketCount to set
   */
  public void setSlovedImprovementTicketCount(long slovedImprovementTicketCount) {
    this.slovedImprovementTicketCount = slovedImprovementTicketCount;
  }

  /**
   * @return the slovedNewFeatureTicketCount
   */
  public long getSlovedNewFeatureTicketCount() {
    return slovedNewFeatureTicketCount;
  }

  /**
   * @param slovedNewFeatureTicketCount the slovedNewFeatureTicketCount to set
   */
  public void setSlovedNewFeatureTicketCount(long slovedNewFeatureTicketCount) {
    this.slovedNewFeatureTicketCount = slovedNewFeatureTicketCount;
  }

  /**
   * @return the slovedSubTaskTicketCount
   */
  public long getSlovedSubTaskTicketCount() {
    return slovedSubTaskTicketCount;
  }

  /**
   * @param slovedSubTaskTicketCount the slovedSubTaskTicketCount to set
   */
  public void setSlovedSubTaskTicketCount(long slovedSubTaskTicketCount) {
    this.slovedSubTaskTicketCount = slovedSubTaskTicketCount;
  }

  /**
   * @return the slovedTaskTicketCount
   */
  public long getSlovedTaskTicketCount() {
    return slovedTaskTicketCount;
  }

  /**
   * @param slovedTaskTicketCount the slovedTaskTicketCount to set
   */
  public void setSlovedTaskTicketCount(long slovedTaskTicketCount) {
    this.slovedTaskTicketCount = slovedTaskTicketCount;
  }

  /**
   * @return the slovedTestTicketCount
   */
  public long getSlovedTestTicketCount() {
    return slovedTestTicketCount;
  }

  /**
   * @param slovedTestTicketCount the slovedTestTicketCount to set
   */
  public void setSlovedTestTicketCount(long slovedTestTicketCount) {
    this.slovedTestTicketCount = slovedTestTicketCount;
  }

  public Long getCountByTagName(String tagName) {
    if (this.tagCountMap.containsKey(tagName)) {
      return this.tagCountMap.get(tagName);
    }
    return null;
  }

  protected boolean setTagCountMap(Map<String, Long> tagCountMap) {
    this.tagCountMap = tagCountMap;
    return true;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("assigneeId", assigneeId)
        .add("assigneeName", assigneeName).add("isCommitter", isCommitter)
        .add("solvedTicketCount", solvedTicketCount)
        .add("slovedBlockerTicketCount", slovedBlockerTicketCount)
        .add("slovedCriticalTicketCount", slovedCriticalTicketCount)
        .add("slovedMajorTicketCount", slovedMajorTicketCount)
        .add("slovedMinorTicketCount", slovedMinorTicketCount)
        .add("slovedTrivialTicketCount", slovedTrivialTicketCount)
        .add("slovedBugTicketCount", slovedBugTicketCount)
        .add("slovedImprovementTicketCount", slovedImprovementTicketCount)
        .add("slovedNewFeatureTicketCount", slovedNewFeatureTicketCount)
        .add("slovedSubTaskTicketCount", slovedSubTaskTicketCount)
        .add("slovedTaskTicketCount", slovedTaskTicketCount)
        .add("slovedTestTicketCount", slovedTestTicketCount)
        .add("tagCountMap", tagCountMap)
        .toString();
  }

}
