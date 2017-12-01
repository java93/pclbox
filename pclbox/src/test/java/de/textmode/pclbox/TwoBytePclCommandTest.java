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

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * JUnit-Tests for {@link TwoBytePclCommand}.
 */
public final class TwoBytePclCommandTest extends TestCase {

    private static final TwoBytePclCommand ESC_E_AT_1 = new TwoBytePclCommand(1, (byte) 0x45);
    private static final TwoBytePclCommand ESC_E_AT_2 = new TwoBytePclCommand(2, (byte) 0x45);
    private static final TwoBytePclCommand ESC_9_AT_1 = new TwoBytePclCommand(1, (byte) 0x39);
    private static final TwoBytePclCommand ESC_9_AT_2 = new TwoBytePclCommand(2, (byte) 0x39);

    /**
     * Checks the method hashCode.
     */
    public void testHashCode() {
        assertEquals(68, ESC_E_AT_1.hashCode());
        assertEquals(71, ESC_E_AT_2.hashCode());
        assertEquals(56, ESC_9_AT_1.hashCode());
        assertEquals(59, ESC_9_AT_2.hashCode());
    }

    /**
     * Checks the method equals.
     */
    public void testEquals() {
        assertFalse(ESC_E_AT_1.equals("DUMMY")); // equals with object of other class

        assertFalse(ESC_E_AT_1.equals(ESC_E_AT_2)); // same command, different offset
        assertFalse(ESC_E_AT_1.equals(ESC_9_AT_1)); // different command, same offset
        assertTrue(ESC_E_AT_1.equals(new TwoBytePclCommand(1, (byte) 0x45)));
    }

    /**
     * Checks the method getTextualDescription.
     */
    public void testGetTextualDescription() {
        assertEquals("Printer Reset", ESC_E_AT_2.getTextualDescription());
        assertEquals("Clear Horizontal Margins", ESC_9_AT_1.getTextualDescription());
        assertEquals("Unknown PCL-Command 3", new TwoBytePclCommand(1, (byte) 0x33).getTextualDescription());
    }

    /**
     * Checks the method toString.
     */
    public void testToString() {
        assertEquals("<esc>E@1", ESC_E_AT_1.toString());
        assertEquals("<esc>E@2", ESC_E_AT_2.toString());
        assertEquals("<esc>9@1", ESC_9_AT_1.toString());
        assertEquals("<esc>9@2", ESC_9_AT_2.toString());
    }

    /**
     * Checks the method toCommandString.
     */
    public void testToCommandString() {
        assertEquals("E", ESC_E_AT_1.toCommandString());
        assertEquals("9", ESC_9_AT_1.toCommandString());
    }

    /**
     * Checks the method toDisplayString.
     */
    public void testToDisplayString() {
        assertEquals("E", ESC_E_AT_1.toDisplayString());
        assertEquals("9", ESC_9_AT_1.toDisplayString());
    }

    /**
     * Checks the method toByteArray and writeTo.
     */
    public void testToBinary() throws Exception {
        final ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ESC_E_AT_1.writeTo(baos1);
        assertTrue(Arrays.equals(ESC_E_AT_1.toByteArray(), baos1.toByteArray()));

        final ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ESC_9_AT_1.writeTo(baos2);
        assertTrue(Arrays.equals(ESC_9_AT_1.toByteArray(), baos2.toByteArray()));
    }
}
