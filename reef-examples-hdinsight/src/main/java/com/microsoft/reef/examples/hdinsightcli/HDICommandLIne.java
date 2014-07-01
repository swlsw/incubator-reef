package com.microsoft.reef.examples.hdinsightcli;

import com.microsoft.reef.runtime.hdinsight.client.UnsafeHDInsightRuntimeConfiguration;
import com.microsoft.reef.runtime.hdinsight.client.yarnrest.ApplicationID;
import com.microsoft.reef.runtime.hdinsight.client.yarnrest.ApplicationState;
import com.microsoft.reef.runtime.hdinsight.client.yarnrest.HDInsightInstance;
import com.microsoft.tang.Tang;
import org.apache.commons.cli.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the HDInsight REST commandline
 */
public final class HDICommandLine {
  private static final Logger LOG = Logger.getLogger(HDICommandLine.class.getName());
  private static final String KILL = "kill";
  private static final String LOGS = "logs";
  private static final String LIST = "list";

  private final HDInsightInstance hdInsightInstance;
  private final Options options;

  @Inject
  public HDICommandLine(final HDInsightInstance hdInsightInstance) {
    this.hdInsightInstance = hdInsightInstance;
    final OptionGroup commands = new OptionGroup()
        .addOption(OptionBuilder.withArgName(KILL).hasArg().withDescription("Kills the given application").create(KILL))
        .addOption(OptionBuilder.withArgName(LOGS).hasArg().withDescription("Kills the given application").create(LOGS))
        .addOption(OptionBuilder.withArgName(LIST).withDescription("Kills the given application").create(LIST));
    this.options = new Options().addOptionGroup(commands);
  }

  public void run(final String[] args) throws Exception {
    final CommandLineParser parser = new PosixParser();

    final CommandLine line = parser.parse(options, args);
    if (line.hasOption(KILL)) {
      this.kill(line.getOptionValue(KILL));
    } else if (line.hasOption(LOGS)) {
      this.logs(line.getOptionValue(LOGS));
    } else if (line.hasOption(LIST)) {
      this.list();
    } else {
      throw new Exception("Unable to parse command line");
    }

  }

  private void kill(final String applicationId) {
    LOG.log(Level.INFO, "Killing application [{0}]", applicationId);
  }

  private void logs(final String applicationId) {
    LOG.log(Level.INFO, "Fetching logs for application [{0}]", applicationId);
  }

  private void list() throws IOException {
    LOG.log(Level.INFO, "Listing applications");
    final ApplicationID applicationID = this.hdInsightInstance.getApplicationID();
    System.out.println(applicationID);
    final List<ApplicationState> applications = this.hdInsightInstance.listApplications();
    for (final ApplicationState appState : applications) {
      System.out.println(appState.getId() + "\t" + appState.getName());
    }
  }


  public static void main(final String[] args) throws Exception {
    Tang.Factory.getTang()
        .newInjector(UnsafeHDInsightRuntimeConfiguration.fromEnvironment())
        .getInstance(HDICommandLine.class).run(args);
  }

}
