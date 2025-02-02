/*
 * SonarQube Apple Plugin - Enables analysis of Swift and Objective-C projects into SonarQube.
 * Copyright © 2022 inside|app (contact@insideapp.fr)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insideapp.sonarqube.apple.commons.cli;

import org.buildobjects.process.ProcBuilder;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.HashSet;
import java.util.Set;

public abstract class CommandLineToolRunner {

    private static final Logger LOGGER = Loggers.get(CommandLineToolRunner.class);
    private static final int COMMAND_TIMEOUT = 10 * 60 * 1000;
    private static final int DEFAULT_COMMAND_EXIT_CODE = 0;
    private final String command;

    protected CommandLineToolRunner(final String command) {
        this.command = command;
    }

    protected int[] exitCodes() {
        return new int[]{DEFAULT_COMMAND_EXIT_CODE};
    }

    private ProcBuilder build(String[] arguments) {
        ProcBuilder builtCommand = new ProcBuilder(command)
                .withArgs(arguments)
                .withTimeoutMillis(COMMAND_TIMEOUT)
                .withExpectedExitStatuses(exitCodes());
        LOGGER.debug("Command that will be executed: {}", builtCommand.getCommandLine());
        return builtCommand;
    }

    public final String execute(String[] arguments) throws Exception {
        return build(arguments).run().getOutputString();
    }

}
