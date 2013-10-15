/**
 * 
 */
package com.salesforce.jirachi.analysis.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.salesforce.jirachi.analysis.JiraAnalysisReport;
import com.salesforce.jirachi.analysis.JiraAnalysisReportEntry;
import com.salesforce.jirachi.ingest.JiraTagger;

/**
 * @author scott_miao
 *
 */
public class CsvReportBuilder {

  private JiraAnalysisReport report = null;
  private String pathName = null;
  private String fileName = null;

  private static String FILE_EXTENSION = ".csv";

  private static final String[] TAG_NAMES;
  static {
    Collection<String> tagNames = JiraTagger.getTagNames();
    TAG_NAMES = tagNames.toArray(new String[] {});
  }

  public CsvReportBuilder(JiraAnalysisReport report, String pathName, String fileName) {
    this.report = report;
    this.pathName = pathName;
    this.fileName = fileName;
  }

  /**
   * build File.
   * @return File contains the jiraAnalysisReport contents.
   * @throws IOException
   */
  public File build() throws IOException {
    File reportFile = new File(this.pathName, this.fileName + FILE_EXTENSION);
    BufferedWriter writer = null;
    // Files.touch(reportFile);
    // if (!reportFile.createNewFile()) {
    // reportFile.delete();
    // reportFile.createNewFile();
    // }
    try {
      writer = Files.newWriter(reportFile, Charset.forName("UTF-8"));
      fillData(writer);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw e;
    } finally {
      Closeables.closeQuietly(writer);
    }
    return reportFile;
  }

  private void fillData(BufferedWriter writer) throws IOException {
    JiraAnalysisReportEntry entry = null;
    StringBuffer str = null;
    try {
      this.fillTitle(writer);
      this.fillHeader(writer);
      for (int a = 0; a < this.report.size(); a++) {
        entry = this.report.getEntry(a);
        str = new StringBuffer();

        str.append(entry.getAssigneeId() + ",");
        str.append(entry.getAssigneeName() + ",");
        str.append(entry.isCommitter() + ",");
        str.append(entry.getSolvedTicketCount() + ",");
        str.append(entry.getSlovedBlockerTicketCount() + ",");
        str.append(entry.getSlovedCriticalTicketCount() + ",");
        str.append(entry.getSlovedMajorTicketCount() + ",");
        str.append(entry.getSlovedMinorTicketCount() + ",");
        str.append(entry.getSlovedTrivialTicketCount() + ",");
        str.append(entry.getSlovedBugTicketCount() + ",");
        str.append(entry.getSlovedImprovementTicketCount() + ",");
        str.append(entry.getSlovedNewFeatureTicketCount() + ",");
        str.append(entry.getSlovedSubTaskTicketCount() + ",");
        str.append(entry.getSlovedTaskTicketCount() + ",");
        str.append(entry.getSlovedTestTicketCount());

        // collect tag values
        for (String tagName : TAG_NAMES) {
          str.append("," + entry.getCountByTagName(tagName));
        }
        str.append("\n");
        writer.write(str.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  private void fillTitle(BufferedWriter writer) throws IOException {
    Date startUpdateDate = this.report.getStartUpdateDate();
    Date endUpdateDate = this.report.getEndUpdateDate();
    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
      if (startUpdateDate == null && endUpdateDate == null) {
        writer.write("No {start,end}UpdateDate set\n");
      } else if (startUpdateDate == null && endUpdateDate != null) {
        writer.write("No startUpdateDate set\nendUpdateDate," + dft.format(endUpdateDate) + "\n");
      } else if (startUpdateDate != null && endUpdateDate == null) {
        writer.write("startUpdateDate," + dft.format(startUpdateDate) + "\nNo endUpdateDate\n");
      } else {
        writer.write("startUpdateDate," + dft.format(startUpdateDate) + "\n");
        writer.write("endtUpdateDate," + dft.format(endUpdateDate) + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  private void fillHeader(BufferedWriter writer) throws IOException {
    StringBuffer str = new StringBuffer();
    str.append("assigneeId,assigneeName,isCommitter,solvedTicketCount,slovedBlockerTicketCount,");
    str.append("slovedCriticalTicketCount,slovedMajorTicketCount,slovedMinorTicketCount,");
    str.append("slovedTrivialTicketCount,slovedBugTicketCount,slovedImprovementTicketCount,");
    str.append("slovedNewFeatureTicketCount,slovedSubTaskTicketCount,slovedTaskTicketCount,");
    str.append("slovedTestTicketCount");

    for (String tagName : TAG_NAMES) {
      str.append("," + tagName);
    }
    str.append("\n");
    try {
      writer.write(str.toString());
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

}
