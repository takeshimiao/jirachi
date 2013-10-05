package com.salesforce.jirachi.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJiraAnalysisReportRepository {

  private JiraAnalysisReportRepository repository = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    com.salesforce.jirachi.ingest.CliMain.setProperties();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    this.repository = new JiraAnalysisReportRepository();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGetJiraAnalisysReport() throws Exception {
    JiraAnalysisReport report = null;
    Calendar calendar = null;
    Date startUpdateDate = null;
    Date endUpdateDate = null;

    calendar = new GregorianCalendar(2013, 8, 1);
    startUpdateDate = calendar.getTime();

    calendar = new GregorianCalendar(2013, 8, 10);
    endUpdateDate = calendar.getTime();

    try {
      report =
          this.repository.getJiraAnalisysReport(startUpdateDate, endUpdateDate, new String[] {
              "apurtell", "ddas" });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

    // check results
    assertNotNull(report);
    assertEquals(startUpdateDate, report.getStartUpdateDate());
    assertEquals(endUpdateDate, report.getEndUpdateDate());

    JiraAnalysisReportEntry entry = null;
    for (int a = 0; a < report.size(); a++) {
      entry = report.getEntry(a);
      assertNotNull(entry);
      System.out.println("#" + a + ", entry=>" + entry);
    }
  }

}
