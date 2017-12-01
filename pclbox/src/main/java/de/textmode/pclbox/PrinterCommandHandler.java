package de.textmode.pclbox;

/*
 * Copyright 2017 Michael Knigge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

/**
 * This is the handler interface for every constructed {@link PrinterCommand}.
 */
public interface PrinterCommandHandler {

    /**
     * Handles the {@link PrinterCommand}.
     *
     * @param command   a concrete {@link PrinterCommand} to be handled.
     *
     * @throws IOException if an I/O error occurs.
     */
    void handlePrinterCommand(final PrinterCommand command) throws IOException;
}
