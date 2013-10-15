/**
 * 
 */
package com.salesforce.jirachi.analysis;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.io.Files;
import com.salesforce.jirachi.analysis.builder.CsvReportBuilder;

/**
 * @author scott_miao
 *
 */
public class CliMain {


  /**
   * @param args
   */
  public static void main(String[] args) {
    com.salesforce.jirachi.ingest.CliMain.setProperties();
    if (args == null || args.length == 0) printUsageAndExit();
    DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
    String arg = null;
    File outputFile = null;
    Date startUpdateDate = null;
    Date endUpdateDate = null;
    File committersListFilePath = null;
    int a = 0;
    int outputFilePathIdx = -1;
    while (a < args.length) {
      arg = args[a];

      if ("-s".equals(arg)) {
        a++;
        try {
          startUpdateDate = dft.parse(args[a]);
        } catch (ParseException e) {
          System.err.println("parse start-date:" + args[a] + " fail !!");
          e.printStackTrace();
          printUsageAndExit();
        }
      } else if ("-e".equals(arg)) {
        a++;
        try {
          endUpdateDate = dft.parse(args[a]);
        } catch (ParseException e) {
          System.err.println("parse end-date:" + args[a] + " fail !!");
          e.printStackTrace();
          printUsageAndExit();
        }
      } else if ("-c".equals(arg)) {
        a++;
        committersListFilePath = new File(args[a]);
      } else if (outputFilePathIdx < 0) {
        outputFilePathIdx = a;
      }
      a++;
    }
    if (outputFilePathIdx != args.length - 1) {
      System.err.println("outputFilePathIdx is at invliad position:" + outputFilePathIdx);
      printUsageAndExit();
    }
    outputFile = new File(args[outputFilePathIdx]);
    // load committers list
    String[] committers = loadCommittersList(committersListFilePath);
    // start to load report
    JiraAnalysisReportRepository repository = getReportRepository();
    JiraAnalysisReport report = null;
    try {
      report = repository.getJiraAnalisysReport(startUpdateDate, endUpdateDate, committers);
    } catch (Exception e) {
      System.err.println("generate report fail !!");
      e.printStackTrace();
      System.exit(1);
    }
    
    // build output file
    CsvReportBuilder builder = getReportBuilder(report, outputFile);
    try {
      builder.build();
    } catch (IOException e) {
      System.err.println("build output file fail !!");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static String[] loadCommittersList(File committersListFilePath) {
    String[] committers = null;
    String fileContent = null;
    if (committersListFilePath == null) return null;
    try {
      fileContent = Files.toString(committersListFilePath, Charset.forName("UTF-8"));
    } catch (IOException e) {
      System.err.println("get committers list fail, filepath:" + committersListFilePath);
      e.printStackTrace();
    }
    committers = fileContent.split("\n");
    return committers;
  }
  
  private static CsvReportBuilder getReportBuilder(JiraAnalysisReport report, File outputFile) {
    return new CsvReportBuilder(report, outputFile.getParent(), outputFile.getName());
  }
  
  private static JiraAnalysisReportRepository getReportRepository() {
    return new JiraAnalysisReportRepository();
  }

  private static void printUsageAndExit() {
    StringBuffer str = new StringBuffer();

    str.append(CliMain.class.getSimpleName()
        + " Usage: [-s <start-date>] [-e <end-date>] [-c <file-path-for-commiters>] output-result-file-path\n");
    str.append("  -s <start-date>, pattern: <yyyy-MM-dd>\n");
    str.append("  -e <end-date>, pattern: <yyyy-MM-dd>\n");
    str.append("  -c <file-path-for-commiters>, where to load the committers list\n");
    str.append("  output-result-file-path, where to output result file\n");
    System.err.println(str.toString());
    System.exit(1);
  }

}
